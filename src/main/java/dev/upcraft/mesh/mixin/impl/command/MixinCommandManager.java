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
import com.mojang.brigadier.StringReader;
import dev.upcraft.mesh.util.command.CommandHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager {

    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)I"))
    private int extractCommandException(CommandDispatcher<ServerCommandSource> commandDispatcher, StringReader input, Object commandSourceObj, ServerCommandSource source, String command) throws Exception {
        int value = 0;
        Exception ex = null;
        try {
            value = commandDispatcher.execute(input, source);
        }
        catch (Exception e) {
            ex = e;
            throw e;
        }
        finally {
            CommandHelper.recordCommand(command, value, ex);
        }
        return value;
    }
}
