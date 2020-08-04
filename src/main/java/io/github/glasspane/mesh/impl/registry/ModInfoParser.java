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
import com.google.gson.JsonSyntaxException;
import io.github.glasspane.mesh.api.util.MeshModInfo;
import io.github.glasspane.mesh.impl.annotation.SerializedModInfo;
import io.github.glasspane.mesh.util.serialization.JsonUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public class ModInfoParser {

    private static Map<String, MeshModInfo> MOD_INFO;

    public static Map<String, MeshModInfo> getModInfo() {
        return MOD_INFO;
    }

    public static void setup() {
        ImmutableMap.Builder<String, MeshModInfo> builder = ImmutableMap.builder();
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
        mods.forEach(modContainer -> {
            Path meshMetaData = modContainer.getPath("mesh_annotations.json");
            try (Reader reader = Files.newBufferedReader(meshMetaData)) {
                MeshModInfo modInfo = JsonUtil.GSON.fromJson(reader, SerializedModInfo.class);
                builder.put(modContainer.getMetadata().getId(), modInfo);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Mod at '" + modContainer.getRootPath() + "' has an invalid mesh_annotations.json file!", e);
            } catch (NoSuchFileException ignore) {
                //ignore
            } catch (IOException e) {
                throw new RuntimeException("Failed to open mesh_annotations.json for mod at '" + modContainer.getRootPath() + "'!", e);
            }

        });
        MOD_INFO = builder.build();
    }
}
