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
package dev.upcraft.mesh.util.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.command.argument.BlockTagArgumentType;
import dev.upcraft.mesh.util.command.mesh.*;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class MeshCommand {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicatedServer) -> {
            LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(Mesh.MODID).requires(source -> source.hasPermissionLevel(4));
            builder = TpDimCommand.append(builder);
            builder = HandCommand.append(builder);
            builder = DebugCommand.append(builder);
            builder = DumpRecipesCommand.append(builder);
            builder = DumpTagsCommand.append(builder);
            builder = StructureFilterCommand.append(builder);
            commandDispatcher.register(builder);
        });
        ArgumentTypes.register(new Identifier(Mesh.MODID, "block_tag").toString(), BlockTagArgumentType.class, new ConstantArgumentSerializer<>(BlockTagArgumentType::tag));
    }
}
