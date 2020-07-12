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
package io.github.glasspane.mesh.util;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import java.util.Optional;
import java.util.function.BiConsumer;

public class RegistryHelper {

    public static final Identifier ID_REGISTRIES = new Identifier("registries");

    /**
     * apply a function to all current and future entries of a given {@link Registry}
     */
    public static <T> void visitRegistry(Registry<T> registry, BiConsumer<Identifier, T> visitor) {
        registry.getIds().forEach(id -> visitor.accept(id, registry.get(id)));
        RegistryEntryAddedCallback.event(registry).register((index, identifier, entry) -> visitor.accept(identifier, entry));
    }

    /**
     * apply a function to all current and future entries of a given {@link Registry}
     */
    @SuppressWarnings("unchecked")
    public static <T> void visitRegistry(Identifier registryName, BiConsumer<Identifier, T> visitor) {
        Optional<Registry<T>> optional = RegistryHelper.ID_REGISTRIES.equals(registryName) ? Optional.of((Registry<T>) Registry.REGISTRIES) : (Optional<Registry<T>>) Registry.REGISTRIES.getOrEmpty(registryName);
        optional.ifPresent(registry -> visitRegistry(registry, visitor));
    }

    /**
     * simple factory method that takes an {@link Identifier} instead of a String, for convenience
     */
    public static <T> DefaultedRegistry<T> newDefaultedRegistry(Identifier defaultValue, RegistryKey<Registry<T>> registryKey, Lifecycle lifecycle) {
        return new DefaultedRegistry<T>(defaultValue.toString(), registryKey, lifecycle);
    }

    /**
     * universal way of getting the global thread executor for this game instance
     */
    @SuppressWarnings("unchecked")
    public static <R extends Runnable> ReentrantThreadExecutor<R> getMainThreadExecutor() {
        return (ReentrantThreadExecutor<R>) FabricLoader.getInstance().getGameInstance();
    }
}
