/*
 * Mesh
 * Copyright (C) 2019 GlassPane
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
package com.github.glasspane.mesh.util.objects;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LazyReference<T> implements Supplier<T> {

    private Supplier<T> getter;
    private T object = null;

    public LazyReference(Supplier<T> getter) {
        this.getter = Validate.notNull(getter, "Supplier must not be NULL");
    }

    @Override
    public String toString() {
        return "LazyRef(" + this.get() + ")";
    }

    @Nullable
    @Override
    public T get() {
        if(this.getter != null) {
            this.object = getter.get();
            this.getter = null;
        }
        return this.object;
    }

    public void ifPresent(Consumer<T> action) {
        if(this.get() != null) {
            action.accept(this.object);
        }
    }
}
