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
package com.github.glasspane.mesh.impl.registry;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.annotation.AutoRegistry;
import com.github.glasspane.mesh.api.registry.AutoRegistryHook;
import com.github.glasspane.mesh.api.registry.ItemBlockProvider;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class RegistryDiscoverer {

    public static void init() {
        Map<MutableRegistry<?>, Map<Class, Pair<String, Class>>> toRegister = new TreeMap<>(Comparator.comparing(Registry.REGISTRIES::getId));
        //noinspection deprecation
        FabricLoader.INSTANCE.getInitializers(AutoRegistryHook.class).stream().map(Object::getClass).sorted(Comparator.comparing(Class::getCanonicalName)).forEachOrdered(clazz -> {
            AutoRegistry ann = clazz.getAnnotation(AutoRegistry.class);
            if(ann != null) {
                if(Arrays.stream(ann.modsLoaded()).allMatch(net.fabricmc.loader.api.FabricLoader.getInstance()::isModLoaded)) {
                    String modid = ann.modid();
                    Class<?> type = ann.value();
                    Identifier registryName = new Identifier(ann.registry());
                    if(Registry.REGISTRIES.containsId(registryName)) {
                        MutableRegistry registry = Registry.REGISTRIES.get(registryName);
                        toRegister.computeIfAbsent(registry, k -> new TreeMap<>(Comparator.comparing(Class::getCanonicalName, String::compareTo))).put(clazz, new Pair<>(modid, type));
                    }
                    else {
                        Mesh.getLogger().warn("ignoring non-existent registry {} for class {}", registryName, clazz.getCanonicalName());
                    }
                }
                else {
                    Mesh.getLogger().debug("ignoring class {} for registration", clazz.getCanonicalName());
                    Mesh.getLogger().trace("Missing one or more loaded mods: {}", Arrays.toString(ann.modsLoaded()));
                }
            }
            else {
                Mesh.getLogger().error("ignoring class {}: no AutoRegistry annotation present!", clazz.getCanonicalName());
            }
        });
        //special treatment for the item and block registry
        for(MutableRegistry registry : new MutableRegistry[]{Registry.BLOCK, Registry.ITEM}) {
            Optional.ofNullable(toRegister.remove(registry)).ifPresent(map -> RegistryDiscoverer.registerEntries(registry, map));
        }
        toRegister.forEach(RegistryDiscoverer::registerEntries);
    }

    private static <T> void registerEntries(MutableRegistry<T> registry, Map<Class, Pair<String, Class>> entries) {
        entries.forEach((clazz, pair) -> {
            String modid = pair.getLeft();
            @SuppressWarnings("unchecked") Class<T> type = pair.getRight();
            for(Field f : clazz.getDeclaredFields()) {
                int modField = f.getModifiers();
                if(Modifier.isStatic(modField) && Modifier.isPublic(modField) && Modifier.isFinal(modField) && f.getAnnotation(AutoRegistry.Ignore.class) == null) {
                    try {
                        Optional.ofNullable(f.get(null)).filter((o) -> type.isAssignableFrom(o.getClass())).ifPresent(value -> {
                            Identifier name = new Identifier(modid, f.getName().toLowerCase(Locale.ROOT));
                            Mesh.getLogger().trace("registering: {}", name);
                            Registry.register(registry, name, type.cast(value));
                            if(registry == Registry.BLOCK && value instanceof ItemBlockProvider) {
                                Registry.register(Registry.ITEM, name, ((ItemBlockProvider) value).createItem());
                            }
                        });
                    }
                    catch (IllegalAccessException e) {
                        Mesh.getLogger().debug("unable to register entry {}: {}", new Identifier(modid, f.getName().toLowerCase(Locale.ROOT)), e.getMessage());
                    }
                }
            }
        });
    }
}
