/*
 * Mesh
 * Copyright (C) 2019-2020 GlassPane
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
package io.github.glasspane.mesh.impl.vanity;

import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.MeshApiOptions;
import io.github.glasspane.mesh.impl.registry.MeshRegistries;
import io.github.glasspane.mesh.api.logging.MeshLoggerFactory;
import io.github.glasspane.mesh.api.util.vanity.VanityConfig;
import io.github.glasspane.mesh.api.util.vanity.VanityFeature;
import io.github.glasspane.mesh.api.util.vanity.VanityManager;
import io.github.glasspane.mesh.util.JsonUtil;
import io.github.glasspane.mesh.util.RegistryHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class VanityManagerImpl implements VanityManager {

    /**
     * special, hidden flag to disable all vanity restrictions
     */
    private static final boolean DEBUG_NO_RESTRICTIONS = MeshApiOptions.VANITY_DEBUG && Boolean.getBoolean("mesh.debug.vanity.norestrictions");
    private static final Logger logger = MeshLoggerFactory.createPrefixLogger(Mesh.MODID + "_vanity", "Mesh Vanity", () -> MeshApiOptions.VANITY_DEBUG);
    private static final Type GENERAL_CONFIG_TOKEN = new TypeToken<Map<Identifier, JsonObject>>() { }.getType();
    private static final Type USER_CONFIG_TOKEN = new TypeToken<Map<UUID, Map<Identifier, JsonElement>>>() { }.getType();
    public static final VanityManager INSTANCE = new VanityManagerImpl();
    private final Map<UUID, Set<Identifier>> unlockedFeatures = new HashMap<>();

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public Registry<VanityFeature<?>> getRegistry() {
        return MeshRegistries.VANITY_FEATURES;
    }

    @Override
    public <T extends VanityConfig<?>> boolean isAvailable(VanityFeature<T> feature, UUID uuid) {
        return DEBUG_NO_RESTRICTIONS || unlockedFeatures.get(uuid).contains(getRegistry().getId(feature)); //FIXME implement remote sync
    }

    @Override
    public <V extends JsonElement, T extends VanityConfig<V>> T getFeatureConfig(VanityFeature<T> feature, UUID uuid) {
        return feature.getConfig(uuid);
    }

    @Override
    public CompletableFuture<Void> parseRemoteConfig(String url) {
        return MeshApiOptions.VANITY_FEATURES_ENABLED ? CompletableFuture.supplyAsync(() -> {
            VanityManager.getLogger().debug("updating vanity info from {}", url);
            try(InputStreamReader input = new InputStreamReader(new URL(url).openStream())) {
                JsonObject json = JsonHelper.deserialize(JsonUtil.GSON, input, JsonObject.class);
                if(json == null) {
                    throw new JsonParseException("input object was null");
                }
                return json;
            }
            catch (JsonParseException | IOException e) {
                throw new RuntimeException("failed to parse " + url, e);
            }
        }).thenAcceptAsync(json -> {
            unlockedFeatures.clear();
            try {
                Map<Identifier, JsonObject> generalConfig = JsonUtil.GSON.fromJson(JsonHelper.getObject(json, "general", new JsonObject()), GENERAL_CONFIG_TOKEN);
                generalConfig.forEach((identifier, jsonObject) -> getRegistry().getOrEmpty(identifier).ifPresent(vanityFeature -> vanityFeature.readFeatureConfiguration(jsonObject)));
                Map<UUID, Map<Identifier, JsonElement>> userConfig = JsonUtil.GSON.fromJson(JsonHelper.getObject(json, "users", new JsonObject()), USER_CONFIG_TOKEN);
                userConfig.forEach((uuid, map) -> {
                    Registry<VanityFeature<?>> registry = getRegistry();
                    map.forEach((identifier, jsonElement) -> registry.getOrEmpty(identifier).ifPresent(vanityFeature -> vanityFeature.getConfig(uuid).updateFromRemote(jsonElement)));
                    unlockedFeatures.put(uuid, map.keySet().stream().filter(registry::containsId).collect(Collectors.toSet()));
                });
            }
            catch (JsonParseException e) {
                this.logger().error("unable to parse vanity data", e);
                unlockedFeatures.clear();
            }
        }, RegistryHelper.getMainThreadExecutor()) : CompletableFuture.runAsync(unlockedFeatures::clear, RegistryHelper.getMainThreadExecutor());
    }

    static {
        if(DEBUG_NO_RESTRICTIONS) {
            VanityManager.getLogger().warn("SUCCESSFULLY REMOVED INTERNAL RESTRICTIONS", Mesh.NO_LOGGER_PARAMS);
        }
    }
}
