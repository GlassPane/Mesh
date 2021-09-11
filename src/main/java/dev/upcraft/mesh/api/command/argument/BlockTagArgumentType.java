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
package dev.upcraft.mesh.api.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.upcraft.mesh.api.command.MeshCommandExceptions;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.concurrent.CompletableFuture;

/**
 * @since 0.13.0
 */
public class BlockTagArgumentType extends IdentifierArgumentType {

    public static BlockTagArgumentType tag() {
        return new BlockTagArgumentType();
    }

    public static Tag<Block> getTag(CommandContext<ServerCommandSource> ctx, String name) throws CommandSyntaxException {
        Identifier id = IdentifierArgumentType.getIdentifier(ctx, name);
        Tag<Block> tag = ctx.getSource().getWorld().getTagManager().getOrCreateTagGroup(Registry.BLOCK_KEY).getTag(id);
        if (tag != null) {
            return tag;
        }
        throw MeshCommandExceptions.UNKNOWN_BLOCK_TAG.create(id);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(BlockTags.getTagGroup().getTagIds(), builder);
    }


}
