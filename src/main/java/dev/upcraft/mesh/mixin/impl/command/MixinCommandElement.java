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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import dev.upcraft.mesh.util.command.CommandHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandFunction.CommandElement.class)
public abstract class MixinCommandElement {

    @Redirect(method = "execute(Lnet/minecraft/server/function/CommandFunctionManager;Lnet/minecraft/server/command/ServerCommandSource;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/ParseResults;)I"))
    private int extractCommandException(CommandDispatcher<ServerCommandSource> commandDispatcher, ParseResults<ServerCommandSource> parse) throws Exception {
        int value = 0;
        Exception ex = null;
        String command = parse.getReader().getString();
        try {
            value = commandDispatcher.execute(parse);
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            CommandHelper.recordCommand(command, value, ex);
        }
        return value;
    }
}
