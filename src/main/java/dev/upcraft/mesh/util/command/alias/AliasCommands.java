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
package dev.upcraft.mesh.util.command.alias;

import com.google.common.collect.Lists;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.util.command.CommandHelper;
import dev.upcraft.mesh.util.command.mesh.TpDimCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

public class AliasCommands {

    public static void init() {
        //FIXME both redirects are broken
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicatedServer) -> {
            dispatcher.register(CommandHelper.createRedirect(CommandManager.literal("day").requires(serverCommandSource -> Mesh.getConfig().commands.enableDayNightCommands), dispatcher.findNode(Lists.newArrayList("time", "set", "day"))));
            dispatcher.register(CommandHelper.createRedirect(CommandManager.literal("night").requires(serverCommandSource -> Mesh.getConfig().commands.enableDayNightCommands), dispatcher.findNode(Lists.newArrayList("time", "set", "night"))));

            TpDimCommand.registerAliases(dispatcher);
        });
    }
}
