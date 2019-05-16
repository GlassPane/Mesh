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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;

public class TpDimCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("tp").then(CommandManager.argument("dimension", DimensionArgumentType.create()).executes(context -> {
            DimensionType dimType = DimensionArgumentType.getDimensionArgument(context, "dimension");
            context.getSource().getPlayer().changeDimension(dimType);
            context.getSource().sendFeedback(new TranslatableComponent("command.mesh.debug.tp_self", DimensionType.getId(dimType)), true);
            return 1;
        }).then(CommandManager.argument("target", EntityArgumentType.entities()).executes(context -> {
            Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "target");
            if(entities != null && !entities.isEmpty()) {
                DimensionType dimType = DimensionArgumentType.getDimensionArgument(context, "dimension");
                entities.forEach(entity -> context.getSource().getWorld().getServer().execute(() -> entity.changeDimension(dimType)));
                context.getSource().sendFeedback(new TranslatableComponent("command.mesh.debug.tp_multiple", entities.size(), DimensionType.getId(dimType)), true);
                return entities.size();
            }
            else {
                return 0;
            }
        }))));
    }
}
