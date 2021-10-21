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
package dev.upcraft.mesh.util.itemgroup;

import dev.upcraft.mesh.Mesh;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

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
            list.add(new ItemStack(Blocks.LIGHT));
            list.add(Util.make(() -> {
                ItemStack stack = new ItemStack(Blocks.LIGHT);
                stack.getOrCreateSubNbt("BlockStateTag").putString(LightBlock.LEVEL_15.getName(), String.valueOf(10));
                return stack;
            }));
            list.add(Util.make(() -> {
                ItemStack stack = new ItemStack(Blocks.LIGHT);
                stack.getOrCreateSubNbt("BlockStateTag").putString(LightBlock.LEVEL_15.getName(), String.valueOf(5));
                return stack;
            }));
        }).build();
    }

    public ItemGroup getInstance() {
        return INSTANCE;
    }
}
