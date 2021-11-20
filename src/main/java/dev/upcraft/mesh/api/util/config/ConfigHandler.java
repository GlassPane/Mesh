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
package dev.upcraft.mesh.api.util.config;

import dev.upcraft.mesh.util.config.ConfigHandlerImpl;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @since 0.17.0
 */
public interface ConfigHandler {

    static <T extends ConfigData> T getConfig(Class<T> configClass) {
        return getConfigHandler(configClass).get();
    }

    static <T extends ConfigData> Supplier<T> getConfigHandler(Class<T> configClass) {
        return ConfigHandlerImpl.getConfigHolder(configClass);
    }

    /**
     * convenience overload for {@link #registerConfig(String, Class, ConfigSerializer.Factory)}
     */
    static <T extends ConfigData> void registerConfig(String modid, Class<T> configClass) {
        registerConfig(modid, configClass, null);
    }

    /**
     * register a config class to AutoConfig, as well as let Mesh create a config GUI for it.
     * @param modid the mod ID for which to register the config
     * @param configClass the config class
     * @param serializerFactory an optional factory for config serializers, or {@code null} to use {@link #createDefaultConfigSerializerFactory()}
     */
    static <T extends ConfigData> void registerConfig(String modid, Class<T> configClass, @Nullable ConfigSerializer.Factory<T> serializerFactory) {
        ConfigHandlerImpl.registerConfig(modid, configClass, serializerFactory);
    }

    static <T extends ConfigData> boolean reloadConfig(Class<T> configClass) {
        return ConfigHandlerImpl.reloadConfig(configClass);
    }

    static Map<String, Class<? extends ConfigData>> getRegisteredConfigs() {
        return ConfigHandlerImpl.getRegisteredConfigs();
    }

    static void reloadAll() {
        ConfigHandlerImpl.reloadAll();
    }

    static <T extends ConfigData> ConfigSerializer.Factory<T> createDefaultConfigSerializerFactory() {
        return ConfigHandlerImpl.createDefaultConfigSerializerFactory();
    }

}
