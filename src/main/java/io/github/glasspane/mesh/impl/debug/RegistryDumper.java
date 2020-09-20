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
package io.github.glasspane.mesh.impl.debug;

import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.MeshApiOptions;
import io.github.glasspane.mesh.mixin.debug.accessor.RequiredTagListRegistryAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RegistryDumper {

    public static void dumpRegistries() {
        if(MeshApiOptions.CREATE_DATA_DUMP) { //TODO more granularity
            Mesh.getLogger().debug("creating data export");
            Path registryOutputDir = Mesh.getOutputDir().resolve("registry_dump");
            Mesh.getLogger().trace("dumping registries to {}", registryOutputDir::toAbsolutePath);
            Map<Registry<?>, AtomicInteger> registrySizes = new HashMap<>();
            dumpRegistry(registryOutputDir, Registry.REGISTRIES, new Identifier("registries"), registrySizes);
            Registry.REGISTRIES.getIds().forEach(registryName -> {
                registrySizes.computeIfAbsent(Registry.REGISTRIES, reg -> new AtomicInteger(0)).incrementAndGet();
                Registry<?> registry = Registry.REGISTRIES.get(registryName);
                dumpRegistry(registryOutputDir, registry, registryName, registrySizes);
            });

            Path tagOutputDir = Mesh.getOutputDir().resolve("tag_export");
            Mesh.getLogger().trace("exporting tag data to {}", tagOutputDir::toAbsolutePath);
            RequiredTagListRegistryAccessor.getRequiredTagLists().forEach((tagType, requiredTagList) -> {
                fix this shit
            });
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static <T> void dumpRegistry(Path outputDir, Registry<T> registry, Identifier registryName, Map<Registry<?>, AtomicInteger> registrySizes) {
        Path outputFile = outputDir.resolve(registryName.getNamespace() + "/" + registryName.getPath() + ".csv");
        try {
            Files.createDirectories(outputFile.getParent());
        } catch (IOException e) {
            Mesh.getLogger().catching(e);
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            List<Identifier> idList = new LinkedList<>(registry.getIds());
            idList.sort(Comparator.comparing(Identifier::toString));
            String types = "Namespace,Name,Raw ID";
            writer.write(types);
            if (registry == Registry.REGISTRIES) {
                writer.write(",Registry Size");
                writer.newLine();
                writer.write(String.format("%s,%s,%d,%d", registryName.getNamespace(), registryName.getPath(), -1, registrySizes.getOrDefault(Registry.REGISTRIES, new AtomicInteger(0)).get()));
            }
            writer.newLine();
            for (Identifier id : idList) {
                if (registry != Registry.REGISTRIES) {
                    registrySizes.computeIfAbsent(registry, reg -> new AtomicInteger(0)).incrementAndGet();
                }
                T entry = registry.get(id);
                AtomicInteger rawID = new AtomicInteger(registry.getRawId(entry));
                StringBuilder builder = new StringBuilder();
                builder.append(id.getNamespace()).append(",").append(id.getPath()).append(",").append(rawID);
                if (registry == Registry.REGISTRIES) {
                    builder.append(",").append(registrySizes.getOrDefault(entry, new AtomicInteger(0)));
                }
                writer.write(builder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            Mesh.getLogger().debug("unable to dump registry " + registryName, e);
        }
    }
}
