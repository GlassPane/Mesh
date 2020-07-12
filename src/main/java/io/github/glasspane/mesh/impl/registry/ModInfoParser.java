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
package io.github.glasspane.mesh.impl.registry;

import com.google.common.collect.ImmutableMap;
import io.github.glasspane.mesh.api.util.MeshModInfo;
import io.github.glasspane.mesh.impl.ModInfoImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ModInfoParser {

    private static Map<String, MeshModInfo> MOD_INFO;

    public static Map<String, MeshModInfo> getModInfo() {
        return MOD_INFO;
    }

    public static void setup() {
        ImmutableMap.Builder<String, MeshModInfo> builder = ImmutableMap.builder();
        List<ModMetadata> metadata = FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).collect(Collectors.toList());
        metadata.forEach(meta -> {
            Set<String> registryClasses = extractValues(meta, "mesh:registry");
            Set<String> dataGenerators = extractValues(meta, "mesh:data_generator");
            builder.put(meta.getId(), new ModInfoImpl(registryClasses, dataGenerators));
        });
        MOD_INFO = builder.build();
    }

    private static Set<String> extractValues(ModMetadata meta, String key) {
        return Optional.ofNullable(meta.getCustomValue(key))
                .map(CustomValue::getAsArray)
                .map(array -> StreamSupport.stream(array.spliterator(), false)
                        .map(CustomValue::getAsString)
                        .collect(Collectors.toSet())
                )
                .orElse(Collections.emptySet());
    }
}
