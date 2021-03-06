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
package dev.upcraft.mesh.impl.client.registry;

import dev.upcraft.mesh.api.registry.RenderLayerProvider;
import dev.upcraft.mesh.util.collections.RegistryHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class ClientRegistryProcessor {

    public static void init() {
        RegistryHelper.visitRegistry(Registry.BLOCK, (identifier, block) -> {
            if (block instanceof RenderLayerProvider) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, ((RenderLayerProvider) block).getRenderLayer());
            }
        });
    }
}
