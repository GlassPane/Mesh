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
package com.github.glasspane.mesh.util.command.mesh;

import com.github.glasspane.mesh.api.multiblock.MultiblockArgumentType;
import com.github.glasspane.mesh.api.multiblock.MultiblockManager;
import com.github.glasspane.mesh.api.multiblock.MultiblockTemplate;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class MultiblockCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("multiblock").then(CommandManager.argument("multiblock", MultiblockArgumentType.create()).then(CommandManager.argument("pos", BlockPosArgumentType.create()).executes(context -> {
            MultiblockTemplate<?> template = MultiblockArgumentType.getMultiblockArgument(context, "multiblock");
            Map<BlockPos, BlockState> stateMap = template.getStateMap();
            context.getSource().sendFeedback(new LiteralText(MultiblockManager.getInstance().getRegistry().getId(template).toString()), false);

            //TODO display ghost map relative to player
            return 1;
        }))));
    }
}
