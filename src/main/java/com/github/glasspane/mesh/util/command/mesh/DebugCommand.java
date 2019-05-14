package com.github.glasspane.mesh.util.command.mesh;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;

public class DebugCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("debug").executes(context -> {
            context.getSource().sendFeedback(new StringTextComponent("Hello there!"), false); //boolean: broadcast to OPs
            return 0;
        }));
    }
}
