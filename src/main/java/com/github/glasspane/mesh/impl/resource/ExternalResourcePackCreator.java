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
package com.github.glasspane.mesh.impl.resource;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.util.JsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class ExternalResourcePackCreator implements ResourcePackProvider {

    private static final String PACK_NAME = Mesh.MODID + "_external";

    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        Path packDir = Mesh.getOutputDir().resolve("resources");
        if (Files.isDirectory(packDir)) {
            Path mcmeta = packDir.resolve("pack.mcmeta");
            if (!Files.exists(mcmeta)) {
                JsonObject meta = new JsonObject();
                JsonObject pack = new JsonObject();
                pack.addProperty("description", "Mesh extra resources");
                pack.addProperty("pack_format", 4);
                meta.add("pack", pack);
                try (Writer writer = Files.newBufferedWriter(mcmeta, StandardCharsets.UTF_8)) {
                    JsonUtil.GSON.toJson(meta, writer);
                } catch (IOException e) {
                    Mesh.getLogger().error("unable to create external resource meta file", e);
                }
            }
            Optional.ofNullable(ResourcePackProfile.of(PACK_NAME, true, () -> new DirectoryResourcePack(packDir.toFile()), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN)).ifPresent(consumer);
        }
    }
}
