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
package com.github.glasspane.mesh.impl.debug;

import com.github.glasspane.mesh.Mesh;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RegistryDumper {

    public static void dumpRegistries() {
        Mesh.getLogger().debug("dumping registry data...");
        File outputDir = new File(FabricLoader.getInstance().getGameDirectory(), "mesh/registry_dump");
        Mesh.getLogger().trace("dumping registries to {}", outputDir.getAbsolutePath());
        Map<Registry<?>, AtomicInteger> registrySizes = new HashMap<>();
        if(outputDir.exists() || outputDir.mkdirs()) {
            Registry.REGISTRIES.getIds().forEach(registryName -> {
                registrySizes.computeIfAbsent(Registry.REGISTRIES, reg -> new AtomicInteger(0)).incrementAndGet();
                Registry<?> registry = Registry.REGISTRIES.get(registryName);
                dumpRegistry(outputDir, registry, registryName, registrySizes);
            });
            dumpRegistry(outputDir, Registry.REGISTRIES, new Identifier("registries"), registrySizes);
        }
        else {
            Mesh.getLogger().debug("Error dumping registries!");
            Mesh.getLogger().trace("unable to create directory at {}", outputDir.getAbsolutePath());
        }
    }

    private static <T> void dumpRegistry(File outputDir, Registry<T> registry, Identifier registryName, Map<Registry<?>, AtomicInteger> registrySizes) {
        File outputFile = new File(outputDir, registryName.getNamespace() + "/" + registryName.getPath() + ".csv");
        if(!outputFile.exists() || outputFile.delete()) {
            outputFile.getParentFile().mkdirs();
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                List<Identifier> idList = new LinkedList<>(registry.getIds());
                Collections.sort(idList);
                String types = "Namespace,Name,Raw ID";
                writer.write(types);
                if(registry == Registry.REGISTRIES) {
                    writer.write(",Registry Size");
                    writer.newLine();
                    writer.write(String.format("%s,%s,%d,%d", registryName.getNamespace(), registryName.getPath(), -1, registrySizes.getOrDefault(Registry.REGISTRIES, new AtomicInteger(0)).get()));
                }
                writer.newLine();
                for(Identifier id : idList) {
                    if(registry != Registry.REGISTRIES) {
                        registrySizes.computeIfAbsent(registry, reg -> new AtomicInteger(0)).incrementAndGet();
                    }
                    T entry = registry.get(id);
                    AtomicInteger rawID = new AtomicInteger(registry.getRawId(entry));
                    StringBuilder builder = new StringBuilder();
                    builder.append(id.getNamespace()).append(",").append(id.getPath()).append(",").append(rawID);
                    if(registry == Registry.REGISTRIES) {
                        builder.append(",").append(registrySizes.getOrDefault(entry, new AtomicInteger(0)));
                    }
                    writer.write(builder.toString());
                    writer.newLine();
                }
            }
            catch (IOException e) {
                Mesh.getLogger().debug("unable to dump registry " + registryName, e);
            }
        }
        else {
            Mesh.getLogger().trace("unable to delete old registry file at {}, registry will not be dumped!", outputFile.getAbsolutePath());
        }
    }
}
