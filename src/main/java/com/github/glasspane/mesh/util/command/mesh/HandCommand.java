package com.github.glasspane.mesh.util.command.mesh;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.text.event.ClickEvent;
import net.minecraft.text.event.HoverEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

public class HandCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("hand").executes(context -> getItemInHand(context, Hand.MAIN_HAND)).then(CommandManager.literal("main").executes(context -> getItemInHand(context, Hand.MAIN_HAND))).then(CommandManager.literal("off").executes(context -> getItemInHand(context, Hand.OFF_HAND))));
    }

    private static int getItemInHand(CommandContext<ServerCommandSource> context, Hand hand) throws CommandSyntaxException {
        ItemStack stack = context.getSource().getPlayer().getStackInHand(hand);
        if(!stack.isEmpty()) {
            TextComponent result = new TranslatableTextComponent("command.mesh.debug.helditem_name", stack.getDisplayName());
            result = result.append("\n").append(new TranslatableTextComponent("command.mesh.debug.helditem_id", Registry.ITEM.getId(stack.getItem())));
            result = result.append("\n").append(new TranslatableTextComponent("command.mesh.debug.helditem_count", stack.getAmount()));
            if(stack.hasTag()) {
                result = result.append("\n").append(new TranslatableTextComponent("command.mesh.debug.helditem_nbt", stack.getTag().toTextComponent()).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableTextComponent("command.mesh.debug.helditem_copy_nbt"))).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, stack.getTag().asString()))));
            }
            context.getSource().sendFeedback(result, false);
            return 1;
        }
        else {
            context.getSource().sendError(new TranslatableTextComponent("command.mesh.debug.error.helditem_empty"));
            return 0;
        }
    }
}
