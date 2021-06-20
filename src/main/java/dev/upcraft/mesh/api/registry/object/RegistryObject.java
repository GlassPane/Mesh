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
package dev.upcraft.mesh.api.registry.object;

import com.google.common.base.MoreObjects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public final class RegistryObject<T> implements Supplier<T> {

    private final Registry<T> registry;
    private final Identifier id;
    private T value;

    private RegistryObject(Registry<T> registry, Identifier id) {
        this.registry = Objects.requireNonNull(registry, "Registry must not be null");
        this.id = Objects.requireNonNull(id, "Object id must not be null");
    }

    public static <T> RegistryObject<T> of(Registry<T> registry, Identifier id) {
        return new RegistryObject<>(registry, id);
    }

    public boolean isPresent() {
        return value != null || registry.containsId(id);
    }

    @NotNull
    @Override
    public T get() {
        if (value == null) {
            value = registry.getOrEmpty(id).orElseThrow(() -> new NullPointerException("Object does not exist: " + id.toString()));
        }
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("registry", registry.getKey().getValue()).add("id", id).toString();
    }
}
