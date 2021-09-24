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
package dev.upcraft.mesh.mixin.impl.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import dev.upcraft.mesh.util.command.CommandHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(ExecuteCommand.class)
public abstract class MixinExecuteCommand {

    @Shadow
    private static ArgumentBuilder<ServerCommandSource, ?> addConditionLogic(CommandNode<ServerCommandSource> root, ArgumentBuilder<ServerCommandSource, ?> builder, boolean positive, ExecuteCommand.Condition condition) {
        return null;
    }

    @Shadow @Final private static SimpleCommandExceptionType CONDITIONAL_FAIL_EXCEPTION;

    @Inject(method = "addConditionArguments", at = @At("HEAD"))
    private static void injectExceptionCondition(CommandNode<ServerCommandSource> root, LiteralArgumentBuilder<ServerCommandSource> argumentBuilder, boolean positive, CallbackInfoReturnable<ArgumentBuilder<ServerCommandSource, ?>> cir) {
        argumentBuilder.then(CommandManager.literal("exception").then(addConditionLogic(root, CommandManager.argument("exception_message", StringArgumentType.string()), positive, context -> {
            String msg = StringArgumentType.getString(context, "exception_message");
            if(msg.isBlank()) {
                throw CONDITIONAL_FAIL_EXCEPTION.create();
            }
            return CommandHelper.getLastCommandException().filter(ex -> ex.getMessage().toLowerCase(Locale.ROOT).contains(msg)).isPresent();
        })));
    }
}
