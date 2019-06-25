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
package com.github.glasspane.mesh.client;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

public class RegistrySpriteAtlasHolder<T> extends SpriteAtlasHolder {

    private final Registry<T> registry;

    public RegistrySpriteAtlasHolder(Registry<T> registry, TextureManager textureManager, Identifier textureID, String spritePath) {
        super(textureManager, textureID, "textures/" + spritePath);
        this.registry = registry;
    }

    @Override
    protected Iterable<Identifier> getSprites() {
        return this.registry.getIds();
    }

    public Sprite getSprite(T t) {
        return this.getSprite(this.registry.getId(t));
    }

    @Override
    protected SpriteAtlasTexture.Data prepare(ResourceManager resourceManager, Profiler profiler) {
        return this.method_18668(resourceManager, profiler);
    }

    @Override
    protected void apply(SpriteAtlasTexture.Data data, ResourceManager resourceManager, Profiler profiler) {
        this.method_18666(data, resourceManager, profiler);
    }
}
