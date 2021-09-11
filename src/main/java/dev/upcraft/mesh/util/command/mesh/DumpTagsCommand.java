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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.command.MeshCommandExceptions;
import dev.upcraft.mesh.api.command.argument.EnumArgumentType;
import dev.upcraft.mesh.util.serialization.JsonUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.*;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class DumpTagsCommand {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("tag_export").then(CommandManager.argument("type", EnumArgumentType.of(Type.class)).executes(context -> {
            Type type = EnumArgumentType.getEnumValue(context, "type", Type.class);
            Path outputDir = Mesh.getOutputDir().resolve("tag_export").resolve(type.asString());
            Map<Identifier, ? extends Tag<?>> tagMap = type.getTagGroup().getTags();
            for (Identifier tagName : tagMap.keySet()) {
                Tag<?> tag = tagMap.get(tagName);
                Path outputFile = outputDir.resolve(String.format("%s/%s.json", tagName.getNamespace(), tagName.getPath()));
                try {
                    Files.createDirectories(outputFile.getParent());
                } catch (IOException e) {
                    Mesh.getLogger().error("unable to create directory", e);
                    throw MeshCommandExceptions.IO_EXCEPTION.create();
                }

                JsonObject result = new JsonObject();
                JsonArray array = new JsonArray();

                for (Object thing : tag.values()) {
                    @Nullable Identifier id = ((Registry) type.getRegistry()).getId(thing);
                    if (id != null) {
                        array.add(id.toString());
                    } else {
                        throw new IllegalStateException("Object not registered: " + thing);
                    }
                }
                result.add("values", array);
                try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
                    JsonUtil.GSON.get().toJson(result, writer);
                } catch (IOException e) {
                    Mesh.getLogger().error("error writing to file", e);
                    throw MeshCommandExceptions.IO_EXCEPTION.create();
                }
            }
            context.getSource().sendFeedback(new TranslatableText("command.mesh.debug.tag_export", tagMap.size()), true);
            return tagMap.size();
        })));
    }

    private enum Type implements StringIdentifiable {
        BLOCKS(new Identifier("blocks"), BlockTags::getTagGroup, () -> Registry.BLOCK), ITEMS(new Identifier("items"), ItemTags::getTagGroup, () -> Registry.ITEM), FLUIDS(new Identifier("fluids"), FluidTags::getTagGroup, () -> Registry.FLUID), ENTITY_TYPES(new Identifier("entity_types"), EntityTypeTags::getTagGroup, () -> Registry.ENTITY_TYPE);

        private final String name;
        private final Supplier<TagGroup<?>> groupGetter;
        private final Supplier<Registry<?>> registrySupplier;

        Type(Identifier name, Supplier<TagGroup<?>> groupGetter, Supplier<Registry<?>> registrySupplier) {
            this(String.format("%s/%s", name.getNamespace(), name.getPath()), groupGetter, registrySupplier);
        }

        Type(String name, Supplier<TagGroup<?>> groupGetter, Supplier<Registry<?>> registrySupplier) {
            this.name = name;
            this.groupGetter = groupGetter;
            this.registrySupplier = registrySupplier;
        }

        public TagGroup<?> getTagGroup() {
            return groupGetter.get();
        }

        public Registry<?> getRegistry() {
            return registrySupplier.get();
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
