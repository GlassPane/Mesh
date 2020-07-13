package io.github.glasspane.mesh.api.util.config;

import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.AnnotatedSettings;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.SettingNamingConvention;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigBranch;
import io.github.glasspane.mesh.util.config.ConfigHandlerImpl;

import java.util.Map;

public interface ConfigHandler {

    JanksonValueSerializer SERIALIZER = new JanksonValueSerializer(false);
    AnnotatedSettings DEFAULT_CONFIG_SETTINGS = AnnotatedSettings.builder().useNamingConvention(SettingNamingConvention.SNAKE_CASE).build();

    static <T> T getConfig(Class<T> configClass) {
        return ConfigHandlerImpl.getConfig(configClass);
    }

    static ConfigBranch getConfigBranch(Class<?> configClass) {
        return ConfigHandlerImpl.getConfigBranch(configClass);
    }

    /**
     * @param modid      the mod ID for which to register the config. there can only be one config per mod ID.
     * @param configPath the filename of the config (without .json5 extension)
     */
    static void registerConfig(String modid, String configPath, Class<?> configClass) {
        ConfigHandlerImpl.registerConfig(modid, configPath, configClass);
    }

    /**
     * @param modid      the mod ID for which to register the config. there can only be one config per mod ID.
     */
    static void registerConfig(String modid, Class<?> configClass) {
        registerConfig(modid, modid, configClass);
    }

    static void saveConfig(Class<?> configClass) {
        saveConfig(configClass, getConfigBranch(configClass));
    }

    static void saveConfig(Class<?> configClass, ConfigBranch configBranch) {
        ConfigHandlerImpl.saveConfig(configClass, configBranch);
    }

    static <T> void reloadConfig(Class<T> configClass) {
        ConfigHandlerImpl.reloadConfig(configClass);
    }

    static Map<String, Class<?>> getRegisteredConfigs() {
        return ConfigHandlerImpl.getRegisteredConfigs();
    }

    static void reloadAll() {
        ConfigHandlerImpl.reloadAll();
    }

}
