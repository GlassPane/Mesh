/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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
package io.github.glasspane.mesh.util.itemgroup;

import io.github.glasspane.mesh.Mesh;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MeshItemGroup {

    private static ItemGroup INSTANCE;

    public static void init() {
        INSTANCE = FabricItemGroupBuilder.create(new Identifier(Mesh.MODID, "additions")).icon(() -> new ItemStack(Blocks.COMMAND_BLOCK)).appendItems(list -> {
            list.add(new ItemStack(Blocks.BARRIER));
            list.add(new ItemStack(Blocks.COMMAND_BLOCK));
            list.add(new ItemStack(Blocks.REPEATING_COMMAND_BLOCK));
            list.add(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK));
            list.add(new ItemStack(Blocks.STRUCTURE_BLOCK));
            list.add(new ItemStack(Blocks.STRUCTURE_VOID));
        }).build();
    }

    public ItemGroup getInstance() {
        return INSTANCE;
    }
}
