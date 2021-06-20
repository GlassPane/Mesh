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
package dev.upcraft.mesh.api.util.vanity;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import dev.upcraft.mesh.impl.vanity.VanityManagerImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface VanityManager {

    String VANITY_URL = System.getProperty("mesh.debug.vanity.url", "https://static.upcraft.dev/minecraft/mods/vanity/mesh/v1.json"); //TODO copy to webserver!

    static Logger getLogger() {
        return getInstance().logger();
    }

    Logger logger();

    static VanityManager getInstance() {
        return VanityManagerImpl.INSTANCE;
    }

    Registry<VanityFeature<?>> getRegistry();

    <T extends VanityConfig<?>> boolean isAvailable(VanityFeature<T> feature, UUID uuid);

    default <V extends JsonElement, T extends VanityConfig<V>> Optional<T> getFeatureConfig(VanityFeature<T> feature, @Nullable PlayerEntity player) {
        return Optional.ofNullable(player).map(PlayerEntity::getGameProfile).map(GameProfile::getId).map(uuid -> getFeatureConfig(feature, uuid));
    }

    <V extends JsonElement, T extends VanityConfig<V>> T getFeatureConfig(VanityFeature<T> feature, UUID uuid);

    CompletableFuture<Void> parseRemoteConfig(String url);
}
