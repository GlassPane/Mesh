package com.github.glasspane.mesh.util.command.mesh;

import com.github.glasspane.mesh.api.multiblock.MultiblockManager;
import com.github.glasspane.mesh.api.multiblock.MultiblockTemplate;
import com.github.glasspane.mesh.api.multiblock.MultiblockArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class MultiblockCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("multiblock").then(CommandManager.argument("multiblock", MultiblockArgumentType.create()).then(CommandManager.argument("pos", BlockPosArgumentType.create()).executes(context -> {
            MultiblockTemplate<?> template = MultiblockArgumentType.getMultiblockArgument(context, "multiblock");
            Map<BlockPos, BlockState> stateMap = template.getStateMap();
            context.getSource().sendFeedback(new StringTextComponent(MultiblockManager.getInstance().getRegistry().getId(template).toString()), false);

            //TODO display ghost map relative to player
            return 1;
        }))));
    }
}
