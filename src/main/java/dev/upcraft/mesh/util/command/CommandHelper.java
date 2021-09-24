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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandException;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
        LiteralArgumentBuilder<S> builder = command.requires(command.getRequirement().and(target.getRequirement())).forward(target.getRedirect(), target.getRedirectModifier(), target.isFork()).executes(target.getCommand());
        target.getChildren().forEach(builder::then);
        return builder;
    }

    public static MutableText getCopyToClipboardText() {
        return new TranslatableText("chat.copy");
    }

    /**
     * @since 0.14.0
     */
    public static MutableText getClickToCopyToClipboardText() {
        return new TranslatableText("chat.copy.click");
    }

    /**
     * retrieves the most recent recorded exception thrown while executing commands
     * @see CommandException
     * @see CommandSyntaxException
     *
     * @since 0.14.0
     */
    public static Optional<Exception> getLastCommandException() {
        return Optional.ofNullable(lastCommandException);
    }

    @Nullable
    private static Exception lastCommandException = null;

    public static boolean clearException() {
        boolean result = lastCommandException != null;
        lastCommandException = null;
        return result;
    }

    /**
     * records a command that was used, its result, and the exception that was thrown, if any
     * @implNote if the exception is not {@code null}, the result parameter becomes meaningless and should always be {@code 0}
     *
     * @since 0.14.0
     */
    public static void recordCommand(String command, int result, @Nullable Exception e) {
        // TODO do more processing on commands
        if(e != null) {
            lastCommandException = e;
        }
    }
}
