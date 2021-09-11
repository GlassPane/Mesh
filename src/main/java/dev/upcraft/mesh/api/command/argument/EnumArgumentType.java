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

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.upcraft.mesh.api.command.MeshCommandExceptions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@ApiStatus.Experimental
public class EnumArgumentType<T extends Enum<T>> implements ArgumentType<T> {

    private final Map<String, T> valueLookup;
    private final Class<T> enumClass;

    private EnumArgumentType(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.valueLookup = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toMap(v -> (v instanceof StringIdentifiable ? ((StringIdentifiable) v).asString() : v.name()).toLowerCase(Locale.ROOT), UnaryOperator.identity()));
    }

    public static <E extends Enum<E>> EnumArgumentType<E> of(Class<E> enumClass) {
        return new EnumArgumentType<>(enumClass);
    }

    public static <E extends Enum<E>> E getEnumValue(CommandContext<ServerCommandSource> context, String name, Class<E> enumClass) {
        return context.getArgument(name, enumClass);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        String input = reader.getString().substring(i, reader.getCursor()).toLowerCase(Locale.ROOT);
        T result = valueLookup.get(input);
        if (result == null) {
            throw MeshCommandExceptions.INVALID_ENUM_ARGUMENT_EXCEPTION.create(input, enumClass.getSimpleName());
        }

        return result;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String typed = builder.getRemaining().toLowerCase(Locale.ROOT);
        valueLookup.forEach((s, t) -> {
            if (s.toLowerCase(Locale.ROOT).startsWith(typed)) {
                builder.suggest(s);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return valueLookup.keySet();
    }
}
