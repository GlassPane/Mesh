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

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class VanityFeature<T extends VanityConfig<?>> {

    protected final Map<UUID, T> configs = new HashMap<>();
    protected final Function<UUID, T> configFactory;

    public VanityFeature(Function<UUID, T> configFactory) {
        this.configFactory = configFactory;
    }

    public Optional<T> getConfigOrEmpty(PlayerEntity player) {
        return Optional.ofNullable(player.getGameProfile()).map(GameProfile::getId).map(this::getConfig);
    }

    public T getConfig(UUID uuid) {
        return configs.computeIfAbsent(uuid, this.configFactory);
    }

    public abstract void readFeatureConfiguration(JsonObject json);

}
