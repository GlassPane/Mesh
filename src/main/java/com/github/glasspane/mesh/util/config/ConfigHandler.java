package com.github.glasspane.mesh.util.config;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.util.JsonUtil;
import com.github.glasspane.mesh.util.reflection.ReflectionHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

@Deprecated
public class ConfigHandler {

    private static final Map<Class<?>, Object> CONFIG_OBJECTS = new IdentityHashMap<>();
    private static final Map<Class<?>, String> CONFIG_ID_LOOKUP = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> clazz) {
        if(!CONFIG_OBJECTS.containsKey(clazz)) throw new IllegalStateException("config not registered before accessing: " + clazz.getCanonicalName());
        return (T) CONFIG_OBJECTS.get(clazz);
    }

    /**
     * @param configName the filename of the config (without .json extension)
     */
    public static <T> T registerConfig(String configName, Class<T> configClass) {
        CONFIG_ID_LOOKUP.put(configClass, configName + ".json");
        return reloadConfig(configClass);
    }

    private static <T> T reloadConfig(Class<T> configClass) {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), CONFIG_ID_LOOKUP.get(configClass));
        if(!configFile.exists()) {
            try(FileWriter writer = new FileWriter(configFile)) {
                JsonUtil.GSON.toJson(configClass.newInstance(), writer);
            }
            catch (IOException | IllegalAccessException | InstantiationException e) {
                Mesh.getLogger().error("unable to write config file for {}", configClass.getCanonicalName());
                Mesh.getLogger().trace("file location: " + configFile.getAbsolutePath(), e);
            }
        }
        T config;
        try(FileReader reader = new FileReader(configFile)) {
            config = JsonUtil.GSON.fromJson(reader, configClass);
        }
        catch (IOException e) {
            Mesh.getLogger().error("unable to read config file from " + configFile.getAbsolutePath() + ", falling back to default values!", e);
            config = ReflectionHelper.newInstance(configClass);
        }
        CONFIG_OBJECTS.put(configClass, config);
        return config;
    }
}
