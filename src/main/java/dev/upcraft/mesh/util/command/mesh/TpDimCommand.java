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
package dev.upcraft.mesh.util.command.mesh;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.upcraft.mesh.Mesh;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Collection;

@Beta
public class TpDimCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("tp").then(CommandManager.argument("dimension", DimensionArgumentType.dimension()).executes(context -> {
            ServerWorld targetWorld = DimensionArgumentType.getDimensionArgument(context, "dimension");
            Identifier dimID = context.getArgument("dimension", Identifier.class);
            teleport(context.getSource().getPlayer(), targetWorld);
            context.getSource().sendFeedback(new TranslatableText("command.mesh.debug.tp_self", dimID), true);
            return Command.SINGLE_SUCCESS;
        }).then(CommandManager.argument("target", EntityArgumentType.entities()).executes(context -> {
            Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "target");
            ServerWorld targetWorld = DimensionArgumentType.getDimensionArgument(context, "dimension");
            Identifier dimID = context.getArgument("dimension", Identifier.class);
            entities.forEach(entity -> teleport(entity, targetWorld));
            context.getSource().sendFeedback(new TranslatableText("command.mesh.debug.tp_multiple", entities.size(), dimID), true);
            return entities.size();
        }))));
    }

    public static void registerAliases(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpdim").redirect(dispatcher.findNode(Lists.newArrayList(Mesh.MODID, "tp"))));
    }

    private static void teleport(Entity toTeleport, ServerWorld targetDimension) {
        if(toTeleport instanceof ServerPlayerEntity player) {
            player.teleport(targetDimension, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        }
        else {
            toTeleport.moveToWorld(targetDimension);
        }
    }
}
