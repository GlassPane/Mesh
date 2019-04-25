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
package com.github.glasspane.mesh.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.text.event.ClickEvent;
import net.minecraft.text.event.HoverEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;

public class MeshCommand {

    public static void init() {
        ServerStartCallback.EVENT.register(server -> register(server.getCommandManager().getDispatcher()));
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("mesh").requires(source -> source.hasPermissionLevel(4)).then(CommandManager.literal("tp").then(CommandManager.argument("dimension", DimensionArgumentType.create()).executes(context -> {
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
        })))).then(CommandManager.literal("debug").executes(context -> {
            context.getSource().sendFeedback(new StringTextComponent("debug"), false); //boolean: broadcast to OPs
            return 0;
        })).then(CommandManager.literal("hand").executes(context -> getItemInHand(context, Hand.MAIN)).then(CommandManager.literal("main").executes(context -> getItemInHand(context, Hand.MAIN))).then(CommandManager.literal("off").executes(context -> getItemInHand(context, Hand.OFF)))));
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
