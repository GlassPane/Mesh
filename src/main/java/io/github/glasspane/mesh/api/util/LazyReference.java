/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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
package io.github.glasspane.mesh.api.util;

import net.minecraft.util.Lazy;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An improved {@link Lazy} that doubles as a {@link Supplier}
 *
 * @param <T> the type of value held by this reference
 */
public class LazyReference<T> extends Lazy<T> implements Supplier<T> {

    public LazyReference(Supplier<T> getter) {
        super(Validate.notNull(getter, "Supplier must not be NULL"));
    }

    @Override
    public String toString() {
        return "LazyRef(" + this.get() + ")";
    }

    @Nullable
    @Override
    public T get() {
        // Override to work with obfuscation
        return super.get();
    }

    public void ifPresent(Consumer<T> action) {
        T value = this.get();
        if (value != null) {
            action.accept(value);
        }
    }
}
