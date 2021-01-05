/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package io.github.glasspane.mesh.util.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.AnnotatedSettings;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigBranch;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.util.config.ConfigHandler;
import io.github.glasspane.mesh.util.coremods.ReflectionHelper;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * used to load and store config objects.<br/>
 * serialization to/from json5 files.
 *
 */
@ApiStatus.Internal
public class ConfigHandlerImpl implements ConfigHandler {

    //private static final Executor EXECUTOR = Executors.newSingleThreadExecutor(r -> new Thread(r, Mesh.MOD_NAME + " Config Reloader Thread"));

    private static final Map<Class<?>, Object> CONFIG_OBJECTS = new HashMap<>();
    private static final Map<Class<?>, ConfigBranch> CONFIG_BRANCHES = new HashMap<>();
    private static final Map<Class<?>, Path> CONFIG_PATHS = new HashMap<>();
    private static final Map<Class<?>, Supplier<AnnotatedSettings>> SETTINGS_FACTORIES = new HashMap<>();
    private static final BiMap<String, Class<?>> CONFIG_ID_LOOKUP = HashBiMap.create(5);

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> clazz) {
        if (!CONFIG_OBJECTS.containsKey(clazz)) {
            throw new IllegalStateException("config not registered before accessing: " + clazz.getCanonicalName());
        }
        return (T) CONFIG_OBJECTS.get(clazz);
    }

    public static ConfigBranch getConfigBranch(Class<?> clazz) {
        if (!CONFIG_BRANCHES.containsKey(clazz)) {
            throw new IllegalStateException("config not registered before accessing: " + clazz.getCanonicalName());
        }
        return CONFIG_BRANCHES.get(clazz);
    }

    public static void registerConfig(String modid, String configPath, Class<?> configClass, Supplier<AnnotatedSettings> settingsFactory) {
        SETTINGS_FACTORIES.put(configClass, settingsFactory);
        refreshConfigObjects(configClass);
        CONFIG_ID_LOOKUP.put(modid, configClass);
        CONFIG_PATHS.put(configClass, FabricLoader.getInstance().getConfigDir().resolve(configPath + ".json5"));
        reloadConfig(configClass);
    }

    private static <T> void refreshConfigObjects(Class<T> configClass) {
        T config = ReflectionHelper.newInstance(configClass);
        CONFIG_OBJECTS.put(configClass, config);
        ConfigBranch branch = ConfigTree.builder().applyFromPojo(config, SETTINGS_FACTORIES.get(configClass).get()).build();
        CONFIG_BRANCHES.put(configClass, branch);
    }

    public static void saveConfig(Class<?> configClass, ConfigBranch configBranch) {
        Path configFile = CONFIG_PATHS.get(configClass);
        try {
            Files.createDirectories(configFile.getParent());
            Files.deleteIfExists(configFile);
            try (OutputStream stream = Files.newOutputStream(configFile)) {
                FiberSerialization.serialize(configBranch, stream, SERIALIZER);
            }
        } catch (IOException e) {
            Mesh.getLogger().error("unable to write config file for {} ({})", configClass.getCanonicalName(), CONFIG_ID_LOOKUP.inverse().get(configClass));
            Mesh.getLogger().trace("file location: " + configFile.toAbsolutePath(), e);
        }
    }

    public static <T> void reloadConfig(Class<T> configClass) {
        Path configFile = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_PATHS.get(configClass));
        if (!Files.exists(configFile)) {
            ConfigHandler.saveConfig(configClass);
        }

        try (InputStream stream = Files.newInputStream(configFile)) {
            FiberSerialization.deserialize(getConfigBranch(configClass), stream, SERIALIZER);
        } catch (IOException | ValueDeserializationException e) {
            Mesh.getLogger().error("unable to read config file " + CONFIG_PATHS.get(configClass).toAbsolutePath(), e);
            refreshConfigObjects(configClass); //fall back to default values
        }
    }

    public static void reloadAll() {
        ConfigHandler.getRegisteredConfigs().values().forEach(ConfigHandler::reloadConfig);
    }

    public static Map<String, Class<?>> getRegisteredConfigs() {
        return Collections.unmodifiableMap(CONFIG_ID_LOOKUP);
    }
}
