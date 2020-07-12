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
package io.github.glasspane.mesh.util.command.mesh;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

public class HandCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("hand")
                .executes(context -> getItemInHand(context, Hand.MAIN_HAND))
                .then(CommandManager.literal("main")
                        .executes(context -> getItemInHand(context, Hand.MAIN_HAND)))
                .then(CommandManager.literal("off")
                        .executes(context -> getItemInHand(context, Hand.OFF_HAND))));
    }

    private static int getItemInHand(CommandContext<ServerCommandSource> context, Hand hand) throws CommandSyntaxException {
        ItemStack stack = context.getSource().getPlayer().getStackInHand(hand);
        if(!stack.isEmpty()) {
            MutableText result = new TranslatableText("command.mesh.debug.helditem_name", stack.getName());
            result = result.append("\n").append(new TranslatableText("command.mesh.debug.helditem_id", Registry.ITEM.getId(stack.getItem())));
            result = result.append("\n").append(new TranslatableText("command.mesh.debug.helditem_count", stack.getCount()));
            if(stack.hasTag()) {
                //noinspection ConstantConditions
                result = result.append("\n")
                        .append(new TranslatableText("command.mesh.debug.helditem_nbt", stack.getTag().toText()).styled(style -> style
                                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("command.mesh.debug.helditem_copy_nbt")))
                                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, stack.getTag().asString()))));
            }
            context.getSource().sendFeedback(result, false);
            return 1;
        }
        else {
            context.getSource().sendError(new TranslatableText("command.mesh.debug.error.helditem_empty"));
            return 0;
        }
    }
}
