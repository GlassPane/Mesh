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
package dev.upcraft.mesh.client;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

public class RegistrySpriteAtlasHolder<T> extends SpriteAtlasHolder implements IdentifiableResourceReloadListener {

    private final Registry<T> registry;
    private final Identifier reloaderID;

    public RegistrySpriteAtlasHolder(Identifier reloaderID, Registry<T> registry, TextureManager textureManager, Identifier textureID, String spritePath) {
        super(textureManager, textureID, spritePath);
        this.registry = registry;
        this.reloaderID = reloaderID;
    }

    @Override
    protected Stream<Identifier> getSprites() {
        return this.registry.getIds().stream();
    }

    public Sprite getSprite(T t) {
        return this.getSprite(this.registry.getId(t));
    }

    @Override
    public Identifier getFabricId() {
        return this.reloaderID;
    }
}
