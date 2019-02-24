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
package com.github.glasspane.mesh.util.reflection;

import com.github.glasspane.mesh.Mesh;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {

    @Nullable
    public static <T> Method getMethod(Class<T> clazz, String obfName, @Nullable String yarnName, Class... parameters) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(obfName, parameters);
        }
        catch (NoSuchMethodException e) {
            try {
                if(yarnName == null) {
                    throw e;
                }
                method = clazz.getDeclaredMethod(yarnName, parameters);
            }
            catch (NoSuchMethodException e1) {
                Mesh.getLogger().fatal(String.format("unable to find method %s (%s) in class %s", obfName, yarnName, clazz.getCanonicalName()), e);
            }
        }
        if(method != null) {
            method.setAccessible(true);
            return method;
        }
        else throw new IllegalStateException("reflection error: method");
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T getField(Class<T> clazz, @Nullable T instance, String obfName, @Nullable String yarnName) {
        Field f = null;
        try {
            f = clazz.getDeclaredField(obfName);
        }
        catch (NoSuchFieldException e) {
            try {
                if(yarnName == null) {
                    throw e;
                }
                f = clazz.getDeclaredField(yarnName);
            }
            catch (NoSuchFieldException e1) {
                Mesh.getLogger().fatal(String.format("unable to find field %s (%s) in class %s", obfName, yarnName, clazz.getCanonicalName()), e);
            }
        }
        if(f != null) {
            f.setAccessible(true);
            try {
                return (T) f.get(instance);
            }
            catch (IllegalAccessException e) {
                Mesh.getLogger().fatal(String.format("unable to access field %s in class %s", f.getName(), clazz.getCanonicalName()), e);
            }
        }
        throw new IllegalStateException("reflection error: field");
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            Mesh.getLogger().fatal("unable to instantiate " + clazz.getCanonicalName(), e);
            throw new IllegalStateException("reflection error: instance");
        }
    }
}
