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
package io.github.glasspane.mesh.util.coremods;

import io.github.glasspane.mesh.Mesh;
import org.apache.commons.lang3.Validate;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class MethodInvoker<T> {

    private final Method method;
    private MethodHandle methodHandle;

    public MethodInvoker(Method method) {
        this.method = method;
    }

    public MethodHandle getHandle() {
        if (methodHandle == null) {
            try {
                methodHandle = MethodHandles.lookup().unreflect(method);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("reflection error: method", e);
            }
        }
        return methodHandle;
    }

    public <V> V invokeStatic(Object... params) {
        Validate.isTrue(Arrays.equals(method.getParameterTypes(), Arrays.stream(params).map(Object::getClass).toArray(Class[]::new)), "method parameters do not match signature!");
        try {
            return (V) this.method.invoke(null, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Mesh.getLogger().fatal("error invoking method handle", e);
            throw new IllegalStateException("invocation error");
        }
    }

    public <V> V invoke(T instance, Object... params) {
        Validate.notNull(instance, "instance must not be null!");
        Validate.isTrue(Arrays.equals(method.getParameterTypes(), Arrays.stream(params).map(Object::getClass).toArray(Class[]::new)), "method parameters do not match signature!");
        try {
            return (V) this.method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Mesh.getLogger().fatal("error invoking method handle", e);
            throw new IllegalStateException("invocation error");
        }
    }
}
