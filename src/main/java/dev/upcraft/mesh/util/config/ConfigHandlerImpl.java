/*
 * Mesh
 * Copyright (C) 2019-2021 UpcraftLP
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
package dev.upcraft.mesh.util.config;

import dev.upcraft.mesh.api.util.config.ConfigHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * used to load and store config objects.<br/>
 * serialization to/from json5 files.
 */
@ApiStatus.Internal
public class ConfigHandlerImpl implements ConfigHandler {

    private static final Map<String, Class<? extends ConfigData>> REGISTERED_CONFIGS = new HashMap<>(5);

    public static <T extends ConfigData> T getConfig(Class<T> clazz) {
        return getConfigHolder(clazz).getConfig();
    }

    public static <T extends ConfigData> void registerConfig(String modid, Class<T> configClass, @Nullable ConfigSerializer.Factory<T> serializerFactory) {
        Class<? extends ConfigData> previous = REGISTERED_CONFIGS.put(modid, configClass);
        if(previous != null) {
            throw new IllegalStateException(String.format("Config class for mod %s registered twice! was %s before (now %s)", modid, previous.getCanonicalName(), configClass.getCanonicalName()));
        }
        AutoConfig.register(configClass, serializerFactory != null ? serializerFactory : ConfigHandler.createDefaultConfigSerializerFactory());
    }

    public static <T extends ConfigData> ConfigHolder<T> getConfigHolder(Class<T> clazz) {
        return Objects.requireNonNull(AutoConfig.getConfigHolder(clazz), () -> "config not registered before accessing: " + clazz.getCanonicalName());
    }

    public static <T extends ConfigData> boolean reloadConfig(Class<T> configClass) {
        return getConfigHolder(configClass).load();
    }

    public static void reloadAll() {
        REGISTERED_CONFIGS.values().forEach(ConfigHandler::reloadConfig);
    }

    public static Map<String, Class<? extends ConfigData>> getRegisteredConfigs() {
        return Collections.unmodifiableMap(REGISTERED_CONFIGS);
    }

    public static <T extends ConfigData> ConfigSerializer.Factory<T> createDefaultConfigSerializerFactory() {
        return JanksonConfigSerializer::new; // TODO possibly expand the config serializer with custom data types
    }
}
