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
import com.mojang.brigadier.tree.CommandNode;

public class CommandHelper {

    private CommandHelper() {
        // NO-OP
    }

    /**
     * creates a copy of the target node, as workaround for https://github.com/Mojang/brigadier/issues/46
     * @param command the command being built
     * @param target the command to be executed instead
     */
    public static <S> LiteralArgumentBuilder<S> createRedirect(LiteralArgumentBuilder<S> command, CommandNode<S> target) {
        // loosely based on https://github.com/VelocityPowered/Velocity/blob/8abc9c80a69158ebae0121fda78b55c865c0abad/proxy/src/main/java/com/velocitypowered/proxy/util/BrigadierUtils.java#L38
        var builder = command.requires(command.getRequirement().and(target.getRequirement())).forward(target.getRedirect(), target.getRedirectModifier(), target.isFork()).executes(target.getCommand());
        target.getChildren().forEach(builder::then);
        return builder;
    }
}
