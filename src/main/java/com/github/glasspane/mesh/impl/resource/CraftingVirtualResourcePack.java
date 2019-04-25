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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.AbstractFilenameResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class CraftingVirtualResourcePack implements ResourcePack {

    private static final Set<String> DATA_NAMESPACES = new HashSet<>();
    private static CraftingVirtualResourcePack INSTANCE = new CraftingVirtualResourcePack(); //FIXME get rid of this field!
    private final Map<Identifier, JsonElement> DATA_CACHE = new HashMap<>();

    private CraftingVirtualResourcePack() {
    }

    public static CraftingVirtualResourcePack getInstance() {
        return INSTANCE;
    }

    public void addResource(ResourceType resourceType, Identifier name, JsonElement data) {
        if(resourceType == ResourceType.DATA) {
            if(DATA_CACHE.containsKey(name)) {
                Mesh.getLogger().trace("Recipe {} already exists in datapack<. overwriting!", name);
            }
            DATA_CACHE.put(name, data);
            DATA_NAMESPACES.add(name.getNamespace());
        }
        else {
            throw new IllegalArgumentException("Resource type " + resourceType.getName() + " not implemented!");
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputStream openRoot(String path) throws IOException {
        throw new IllegalStateException("calling datapack clientside!");
    }

    @Override
    public InputStream open(ResourceType type, Identifier name) throws IOException {
        if(type == ResourceType.DATA) {
            if(DATA_CACHE.containsKey(name)) {
                return new ByteArrayInputStream(JsonUtil.GSON.toJson(DATA_CACHE.get(name)).getBytes(StandardCharsets.UTF_8));
            }
        }
        throw new IOException(name + " not found in virtual resource pack!");
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String var2, int var3, Predicate<String> predicate) {
        if(type == ResourceType.DATA) {
            return DATA_CACHE.keySet();
        }
        else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean contains(ResourceType type, Identifier key) {
        return type == ResourceType.DATA && DATA_CACHE.containsKey(key);
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        if(type == ResourceType.DATA) {
            return DATA_NAMESPACES;
        }
        return null;
    }

    @Nullable
    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> reader) throws IOException {
        JsonObject meta = new JsonObject();
        JsonObject pack = new JsonObject();
        pack.addProperty("description", "Mesh extra resources");
        pack.addProperty("pack_format", 4);
        meta.add("pack", pack);
        try(InputStream inputStream = new ByteArrayInputStream(JsonUtil.GSON.toJson(meta).getBytes(StandardCharsets.UTF_8))) {
            return AbstractFilenameResourcePack.parseMetadata(reader, inputStream);
        }
    }

    @Override
    public String getName() {
        return "Mesh/Virtual";
    }

    @Override
    public void close() {
        //NO-OP
    }
}
