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
package io.github.glasspane.mesh.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ReloadableResourceManager;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface SpriteAtlasHolderCreateCallback extends BiConsumer<ReloadableResourceManager, TextureManager> {

    Event<SpriteAtlasHolderCreateCallback> EVENT = EventFactory.createArrayBacked(SpriteAtlasHolderCreateCallback.class, callbacks -> (resourcemanager, textureManager) -> {
        for(SpriteAtlasHolderCreateCallback callback : callbacks) {
            callback.accept(resourcemanager, textureManager);
        }
    });
}
