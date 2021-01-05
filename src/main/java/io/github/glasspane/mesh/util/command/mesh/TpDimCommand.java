/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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

import com.google.common.annotations.Beta;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
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
            return 1;
        }).then(CommandManager.argument("target", EntityArgumentType.entities()).executes(context -> {
            Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "target");
            ServerWorld targetWorld = DimensionArgumentType.getDimensionArgument(context, "dimension");
            Identifier dimID = context.getArgument("dimension", Identifier.class);
            entities.forEach(entity -> teleport(entity, targetWorld));
            context.getSource().sendFeedback(new TranslatableText("command.mesh.debug.tp_multiple", entities.size(), dimID), true);
            return entities.size();
        }))));
    }

    private static void teleport(Entity toTeleport, ServerWorld targetDimension) {
        toTeleport.moveToWorld(targetDimension);
        //FabricDimensions.teleport(toTeleport, targetDimension, (teleported, destination, portalDir, horizontalOffset, verticalOffset) -> new BlockPattern.TeleportTarget(teleported.getPos(), Vec3d.ZERO, teleported.getHorizontalFacing().getHorizontal() * 90));
    }
}
