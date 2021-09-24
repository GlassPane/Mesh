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
package dev.upcraft.mesh.util.command.mesh;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.upcraft.mesh.util.command.CommandHelper;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class GetExceptionCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("exception").requires(source -> source.hasPermissionLevel(2)).then(CommandManager.literal("get").executes(ctx -> getException(ctx, false)).then(CommandManager.argument("clearException", BoolArgumentType.bool()).executes(ctx -> {
            boolean clear = BoolArgumentType.getBool(ctx, "clearException");
            return getException(ctx, clear);
        }))).then(CommandManager.literal("clear").executes(ctx -> {
            ctx.getSource().sendFeedback(new TranslatableText("command.mesh.get_exception.clear"), true);
            return CommandHelper.clearException() ? 1 : 0;
        })));
    }

    private static int getException(CommandContext<ServerCommandSource> ctx, boolean clear) {
        Optional<Exception> opt = CommandHelper.getLastCommandException();
        if(opt.isEmpty()) {
            ctx.getSource().sendError(new TranslatableText("command.mesh.get_exception.empty"));
            return 0;
        }
        else {
            Exception ex = opt.get();
            MutableText text;
            if(ex instanceof CommandException cme) {
                text = cme.getTextMessage() instanceof MutableText mtxt ? mtxt : cme.getTextMessage().shallowCopy();
            }
            else if(ex instanceof CommandSyntaxException cse) {
                Text exceptionMessage = Texts.toText(cse.getRawMessage());
                text = exceptionMessage instanceof MutableText mtxt ? mtxt : exceptionMessage.shallowCopy();
            }
            else {
                String exMessage = ex.getMessage();
                if(exMessage.isBlank()) {
                    exMessage = "Unknown Exception";
                }
                text = new LiteralText(exMessage);
            }

            StringWriter sw = new StringWriter();
            try (PrintWriter printer = new PrintWriter(sw)) {
                ex.printStackTrace(printer);
            }

            text.styled(it -> it.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, sw.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, CommandHelper.getClickToCopyToClipboardText())));

            ctx.getSource().sendFeedback(text, false);

            if(clear) {
                CommandHelper.clearException();
            }
            return 1;
        }
    }
}
