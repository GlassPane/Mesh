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
package com.github.glasspane.mesh.impl.resource;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.util.JsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class ExternalResourcePackCreator implements ResourcePackCreator {

    private static final String PACK_NAME = Mesh.MODID + "_external";

    @Override
    public <T extends ResourcePackContainer> void registerContainer(Map<String, T> map, ResourcePackContainer.Factory<T> factory) {
        File packDir = new File(Mesh.getOutputDir(), "resources");
        if(packDir.isDirectory()) {
            File mcmeta = new File(packDir, "pack.mcmeta");
            if(!mcmeta.exists()) {
                JsonObject meta = new JsonObject();
                JsonObject pack = new JsonObject();
                pack.addProperty("description", "Mesh extra resources");
                pack.addProperty("pack_format", 4);
                meta.add("pack", pack);
                try(Writer writer = new OutputStreamWriter(new FileOutputStream(mcmeta), StandardCharsets.UTF_8)) {
                    JsonUtil.GSON.toJson(meta, writer);
                }
                catch (IOException e) {
                    Mesh.getLogger().error("unable to create external resource meta file", e);
                }
            }
            Optional.ofNullable(ResourcePackContainer.of(PACK_NAME, false, () -> new DirectoryResourcePack(packDir), factory, ResourcePackContainer.SortingDirection.TOP)).ifPresent(t -> map.put(PACK_NAME, t));
        }
    }
}
