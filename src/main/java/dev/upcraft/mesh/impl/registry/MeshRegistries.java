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
package dev.upcraft.mesh.impl.registry;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Lifecycle;
import dev.upcraft.mesh.api.annotation.AutoRegistry;
import dev.upcraft.mesh.api.util.vanity.VanityFeature;
import dev.upcraft.mesh.api.util.vanity.VanityManager;
import dev.upcraft.mesh.Mesh;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.ApiStatus;

@AutoRegistry.Register(value = Registry.class, modid = Mesh.MODID, registry = "root")
public class MeshRegistries {

    private static final RegistryKey<Registry<VanityFeature<?>>> VANITY_FEATURES_REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(Mesh.MODID, "vanity_features"));
    /**
     * use {@link VanityManager#getRegistry()} instead!
     */
    @ApiStatus.Internal
    public static final MutableRegistry<VanityFeature<?>> VANITY_FEATURES = createRegistry(new SimpleRegistry<>(VANITY_FEATURES_REGISTRY_KEY, Lifecycle.experimental()), RegistryAttribute.SYNCED);

    private static <V, T extends Registry<V>> T createRegistry(T registry, RegistryAttribute... attributes) {
        ((FabricRegistry) registry).build(ImmutableSet.copyOf(attributes));
        return registry;
    }
}
