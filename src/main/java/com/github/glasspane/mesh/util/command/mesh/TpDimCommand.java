package com.github.glasspane.mesh.util.command.mesh;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;

public class TpDimCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("tp").then(CommandManager.argument("dimension", DimensionArgumentType.create()).executes(context -> {
            DimensionType dimType = DimensionArgumentType.getDimensionArgument(context, "dimension");
            context.getSource().getPlayer().changeDimension(dimType);
            context.getSource().sendFeedback(new TranslatableTextComponent("command.mesh.debug.tp_self", DimensionType.getId(dimType)), true);
            return 1;
        }).then(CommandManager.argument("target", EntityArgumentType.entities()).executes(context -> {
            Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "target");
            if(entities != null && !entities.isEmpty()) {
                DimensionType dimType = DimensionArgumentType.getDimensionArgument(context, "dimension");
                entities.forEach(entity -> context.getSource().getWorld().getServer().execute(() -> entity.changeDimension(dimType)));
                context.getSource().sendFeedback(new TranslatableTextComponent("command.mesh.debug.tp_multiple", entities.size(), DimensionType.getId(dimType)), true);
                return entities.size();
            }
            else {
                return 0;
            }
        }))));
    }
}
