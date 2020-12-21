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
import com.google.gson.JsonParseException;
import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.util.MeshModInfo;
import io.github.glasspane.mesh.impl.annotation.SerializedModInfo;
import io.github.glasspane.mesh.util.serialization.JsonUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.launch.common.FabricLauncherBase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ModInfoParser {

    private static Map<String, MeshModInfo> MOD_INFO = null;

    public static Map<String, MeshModInfo> getModInfo() {
        if(MOD_INFO == null) {
            Mesh.getLogger().warn("mod info accessed too early!", new IllegalStateException("stacktrace"));
            setup();
        }
        return MOD_INFO;
    }

    public static void setup() {
        HashMap<String, MeshModInfo> map = new HashMap<>();
        try {
            // see how Fabric Loader parses fabric.mod.json files
            // if we don't do it this way, it will break in dev environments
            Enumeration<URL> annotationFiles = FabricLauncherBase.getLauncher().getTargetClassLoader().getResources("mesh_annotations.json");
            while (annotationFiles.hasMoreElements()) {
                URL target = annotationFiles.nextElement();
                try {
                    URI uri = target.toURI();
                    Mesh.getLogger().trace("parsing mod annotation data from {}", Paths.get(uri).toAbsolutePath());
                } catch (URISyntaxException e) {
                    Mesh.getLogger().catching(e);
                }

                try (Reader reader = new InputStreamReader(target.openStream())) {
                    MeshModInfo modInfo = JsonUtil.GSON.fromJson(reader, SerializedModInfo.class);
                    if(!FabricLoader.getInstance().isModLoaded(modInfo.getOwnerModID())) {
                        Mesh.getLogger().debug("Loading annotation config for mod {}, even tho the mod is not loaded!", modInfo::getOwnerModID);
                    }
                    if (map.putIfAbsent(modInfo.getOwnerModID(), modInfo) != null) {
                        Mesh.getLogger().warn("Mod {} already processed, ignorind duplicate annotation data.", modInfo::getOwnerModID);
                    }
                }
                catch (IOException | JsonParseException e) {
                    Mesh.getLogger().warn("unable to read annotations file!", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("unable to get annotations files!");
        }

//        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
//        mods.forEach(modContainer -> {
//
//            Path meshMetaData = modContainer.getPath("mesh_annotations.json");
//            try (Reader reader = Files.newBufferedReader(meshMetaData)) {
//                MeshModInfo modInfo = JsonUtil.GSON.fromJson(reader, SerializedModInfo.class);
//                builder.put(modContainer.getMetadata().getId(), modInfo);
//            } catch (JsonSyntaxException e) {
//                throw new RuntimeException("Mod at '" + modContainer.getRootPath() + "' has an invalid mesh_annotations.json file!", e);
//            } catch (NoSuchFileException ignore) {
//                //ignore
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to open mesh_annotations.json for mod at '" + modContainer.getRootPath() + "'!", e);
//            }
//
//        });
        MOD_INFO = ImmutableMap.copyOf(map); //we have to do it this way because ImmutableMap$Builder has no way to check for duplicates
    }
}
