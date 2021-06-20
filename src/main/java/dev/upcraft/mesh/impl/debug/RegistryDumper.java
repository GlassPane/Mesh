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
package dev.upcraft.mesh.impl.debug;

import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.MeshApiOptions;
import dev.upcraft.mesh.util.serialization.csv.CSVBuilder;
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
        if (MeshApiOptions.CREATE_DATA_DUMP) { //TODO more granularity
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
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static <T> void dumpRegistry(Path outputDir, Registry<T> registry, Identifier registryName, Map<Registry<?>, AtomicInteger> registrySizes) {
        Path outputFile = outputDir.resolve(String.format("%s/%s.csv", registryName.getNamespace(), registryName.getPath()));
        try {
            Files.createDirectories(outputFile.getParent());
        } catch (IOException e) {
            Mesh.getLogger().catching(e);
            return;
        }

        boolean rootRegistry = registry == Registry.REGISTRIES;

        String[] types = new String[]{"Namespace", "Name", "Raw ID"};
        if (rootRegistry) {
            types = new String[]{"Namespace", "Name", "Raw ID", "Registry Size"};
        }

        CSVBuilder builder = CSVBuilder.create(types);
        List<Identifier> idList = new LinkedList<>(registry.getIds());
        idList.sort(Comparator.comparing(Identifier::toString));

        if (rootRegistry) {
            builder.put(registryName.getNamespace(), registryName.getPath(), -1, registrySizes.getOrDefault(Registry.REGISTRIES, new AtomicInteger(0)).get());
        }

        for (Identifier id : idList) {
            T entry = registry.get(id);
            AtomicInteger rawID = new AtomicInteger(registry.getRawId(entry));

            CSVBuilder.Row row = builder.beginRow().put(id.getNamespace()).put(id.getPath()).put(rawID);
            if (rootRegistry) {
                row.put(registrySizes.getOrDefault(entry, new AtomicInteger(0)));
            } else {
                registrySizes.computeIfAbsent(registry, reg -> new AtomicInteger(0)).incrementAndGet();
            }
            row.end();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            builder.build(writer);
        } catch (IOException e) {
            Mesh.getLogger().debug("unable to dump registry " + registryName, e);
        }
    }
}
