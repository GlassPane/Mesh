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
package io.github.glasspane.mesh.util.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.util.command.mesh.*;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class MeshCommand {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicatedServer) -> {
            LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal(Mesh.MODID).requires(source -> source.hasPermissionLevel(4));
            builder = TpDimCommand.append(builder);
            builder = HandCommand.append(builder);
            builder = DebugCommand.append(builder);
            builder = MultiblockCommand.append(builder);
            builder = DumpRecipesCommand.append(builder);
            builder = DumpTagsCommand.append(builder);
            commandDispatcher.register(builder);
        });
    }
}
