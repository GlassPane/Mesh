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
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;

import java.util.Map;
import java.util.Optional;

public class VirtualResourcePackCreator implements ResourcePackCreator {

    private static final String PACK_NAME = Mesh.MODID + "_virtual";
    @Override
    public <T extends ResourcePackContainer> void registerContainer(Map<String, T> map, ResourcePackContainer.Factory<T> factory) {
        Optional.ofNullable(ResourcePackContainer.of(PACK_NAME, false, CraftingVirtualResourcePack::getInstance, factory, ResourcePackContainer.SortingDirection.TOP)).ifPresent(t -> map.put(PACK_NAME, t));
    }
}
