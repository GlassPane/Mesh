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
package com.github.glasspane.mesh.registry;

import com.github.glasspane.mesh.Mesh;
import net.fabricmc.loader.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class RegistryEntryDiscoverer {

    //FIXME sort registries to run in order: block before item, everything else alphabetically
    public static void init() {
        Set<URL> mods = new HashSet<>();
        FabricLoader.INSTANCE.getModContainers().stream().map(ModContainer::getOriginFile).forEach(file -> {
            try {
                URL url = file.toURI().toURL();
                mods.add(url);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(mods).setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AutoRegistry.class);
        for(Class<?> clazz : classes) {
            AutoRegistry ann = clazz.getAnnotation(AutoRegistry.class);
            String modid = ann.modid();
            Class<?> type = ann.value();
            Identifier registryName = new Identifier(ann.registry());
            if(Registry.REGISTRIES.contains(registryName)) {
                ModifiableRegistry registry = Registry.REGISTRIES.get(registryName);
                for(Field f : clazz.getDeclaredFields()) {
                    int modField = f.getModifiers();
                    if(Modifier.isStatic(modField) && Modifier.isPublic(modField) && Modifier.isFinal(modField)) {
                        try {
                            Object value = f.get(null);
                            if(value != null && type.isAssignableFrom(value.getClass())) {
                                Identifier name = new Identifier(modid, f.getName().toLowerCase(Locale.ROOT));
                                if(Mesh.isDebugMode()) {
                                    Mesh.getDebugLogger().debug("registering: {}", name);
                                }
                                registry.register(name, type.cast(value));
                            }
                        }
                        catch (IllegalAccessException e) {
                            Mesh.getDebugLogger().error("unable to register entry {}: {}", new Identifier(modid, f.getName().toLowerCase(Locale.ROOT)), e.getMessage());
                        }
                    }
                }
            }
            else if(Mesh.isDebugMode()) {
                Mesh.getDebugLogger().info("ignoring non-existent registry {} for class {}", registryName, clazz.getCanonicalName());
            }
        }
    }
}
