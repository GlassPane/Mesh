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
package io.github.glasspane.mesh.util.command.alias;

import com.google.common.collect.Lists;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AliasCommands {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicatedServer) -> {
            if (true) { //TODO config check
                CommandNode<ServerCommandSource> day = dispatcher.findNode(Lists.newArrayList("time", "set", "day"));
                dispatcher.register(CommandManager.literal("day").redirect(day));
            }
        });
    }
}
