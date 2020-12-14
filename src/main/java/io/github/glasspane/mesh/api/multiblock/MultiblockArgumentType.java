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
package io.github.glasspane.mesh.api.multiblock;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MultiblockArgumentType implements ArgumentType<MultiblockTemplate<?>> {

    private static final DynamicCommandExceptionType INVALID_MULTIBLOCK_TYPE = new DynamicCommandExceptionType((obj) -> new TranslatableText("command.mesh.argument.multiblock.invalid", obj));
    private static final Collection<String> EXAMPLES = Lists.newArrayList("test:test_multiblock", "modid:multiblock_furnace");

    /**
     * use the factory method ({@link #create()}) instead!
     */
    private MultiblockArgumentType() {
        //NO-OP
    }

    public static MultiblockTemplate<?> getMultiblockArgument(CommandContext<ServerCommandSource> context, String name) {
        return (MultiblockTemplate<?>) context.getArgument(name, MultiblockTemplate.class);
    }

    public static MultiblockArgumentType create() {
        return new MultiblockArgumentType();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Registry<MultiblockTemplate<?>> registry = MultiblockManager.getInstance().getRegistry();
        return CommandSource.suggestIdentifiers(registry.stream().map(registry::getId), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public MultiblockTemplate<?> parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.fromCommandInput(reader);
        return MultiblockManager.getInstance().getRegistry().getOrEmpty(id).orElseThrow(() -> INVALID_MULTIBLOCK_TYPE.create(id));
    }
}
