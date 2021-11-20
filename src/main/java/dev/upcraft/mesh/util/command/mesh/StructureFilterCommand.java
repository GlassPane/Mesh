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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.command.MeshCommandExceptions;
import dev.upcraft.mesh.api.command.argument.BlockTagArgumentType;
import dev.upcraft.mesh.api.util.MeshResourceListenerKeys;
import dev.upcraft.mesh.util.command.CommandHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.structure.StructureManager;
import net.minecraft.tag.Tag;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StructureFilterCommand {

    public static final IdentifiableResourceReloadListener RELOADER = new ReloadListener();

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("filter_structures").requires(src -> src.hasPermissionLevel(2)).then(CommandManager.literal("block").then(CommandManager.argument("block", BlockStateArgumentType.blockState()).executes(ctx -> {
            BlockStateArgument stateArg = BlockStateArgumentType.getBlockState(ctx, "block");
            List<Identifier> values = getStructureList(ctx, stateArg.getBlockState()::isOf);
            return toCommandSender(ctx, values);
        })).then(CommandManager.argument("file_name", StringArgumentType.word()).requires(source -> source.hasPermissionLevel(4)).executes(ctx -> {
            Path outputFile = normalizePath(StringArgumentType.getString(ctx, "file_name"));
            BlockStateArgument stateArg = BlockStateArgumentType.getBlockState(ctx, "block");
            List<Identifier> values = getStructureList(ctx, stateArg.getBlockState()::isOf);
            return toFile(ctx, values, outputFile);
        }))).then(CommandManager.literal("tag").then(CommandManager.argument("tag", BlockTagArgumentType.tag()).executes(ctx -> {
            Tag<Block> tag = BlockTagArgumentType.getTag(ctx, "tag");
            List<Identifier> values = getStructureList(ctx, tag::contains);
            return toCommandSender(ctx, values);
        }).then(CommandManager.argument("file_name", StringArgumentType.word()).requires(source -> source.hasPermissionLevel(4)).executes(ctx -> {
            Path outputFile = normalizePath(StringArgumentType.getString(ctx, "file_name"));
            Tag<Block> tag = BlockTagArgumentType.getTag(ctx, "tag");
            List<Identifier> values = getStructureList(ctx, tag::contains);
            return toFile(ctx, values, outputFile);
        })))));
    }

    private static Path normalizePath(String file_name) throws CommandSyntaxException {
        if(!file_name.endsWith(".txt")) {
            file_name += ".txt";
        }
        try {
            Path structureBaseDir = Mesh.getOutputDir().resolve("structures").toAbsolutePath();
            Path file = structureBaseDir.resolve(file_name).toAbsolutePath();
            if (!file.startsWith(structureBaseDir)) {
                throw MeshCommandExceptions.INVALID_FILE_PATH.create(file_name);
            }
            return file;
        } catch (InvalidPathException | IOError e) {
            if (e instanceof IOError) {
                Mesh.getLogger().error("IO error occurred trying to resolve structure filter export at " + file_name, e);
            }
            throw MeshCommandExceptions.INVALID_FILE_PATH.create(file_name);
        }
    }

    private static int toCommandSender(CommandContext<ServerCommandSource> ctx, List<Identifier> values) {
        if (values.isEmpty()) {
            ctx.getSource().sendError(new TranslatableText("command.mesh.filter_structures.fail"));
            return 0;
        }
        for (int i = 1; i <= values.size(); i++) {
            Identifier id = values.get(i - 1);
            Text message = new TranslatableText("command.mesh.filter_structures.list_item", i, new LiteralText(id.toString()).formatted(Formatting.WHITE)).styled(it -> it.withFormatting(Formatting.GRAY).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, CommandHelper.getClickToCopyToClipboardText())));
            ctx.getSource().sendFeedback(message, false);
        }
        return values.size();
    }

    private static int toFile(CommandContext<ServerCommandSource> ctx, List<Identifier> values, Path file) throws CommandSyntaxException {
        if (values.isEmpty()) {
            ctx.getSource().sendError(new TranslatableText("command.mesh.filter_structures.fail"));
            return 0;
        }
        try {
            Files.createDirectories(file.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(PrintStream out = new PrintStream(Files.newOutputStream(file))) {
            values.forEach(out::println);
        } catch (IOException e) {
            Mesh.getLogger().error("error writing file", e);
            throw MeshCommandExceptions.IO_EXCEPTION.create();
        }
        Text filePath = new LiteralText(file.toString()).formatted(Formatting.BLUE, Formatting.ITALIC);
        Text message = new TranslatableText("command.mesh.filter_structures.file", values.size(), filePath).styled(it -> it.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getParent().toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.mesh.open_folder.click"))));
        ctx.getSource().sendFeedback(message, false);
        return Command.SINGLE_SUCCESS;
    }

    private static List<Identifier> getStructureList(CommandContext<ServerCommandSource> ctx, Predicate<Block> filter) {
        var structureManager = ctx.getSource().getWorld().getStructureManager();
        return ((ReloadListener) RELOADER).getStructureNames().stream().filter(id -> {
            boolean ret = structureManager.getStructure(id).map(structure -> {
                NbtCompound nbt = new NbtCompound();
                structure.writeNbt(nbt);
                NbtList palette = nbt.getList("palette", NbtElement.COMPOUND_TYPE);
                for (int i = 0; i < palette.size(); i++) {
                    NbtCompound element = palette.getCompound(i);
                    Identifier blockName = new Identifier(element.getString("Name"));
                    if (Registry.BLOCK.getOrEmpty(blockName).filter(filter).isPresent()) {
                        return true;
                    }
                }
                return false;
            }).orElse(false);
            structureManager.unloadStructure(id);
            return ret;
        }).sorted(Comparator.comparing(Identifier::toString)).toList();
    }

    private static class ReloadListener implements SimpleSynchronousResourceReloadListener {

        private static final String PREFIX = "structures";
        private static final int PREFIX_LENGTH = PREFIX.length() + 1;
        private static final String SUFFIX = ".nbt";
        private static final int SUFFIX_LENGTH = SUFFIX.length();
        private Set<Identifier> structureNames = Collections.emptySet();

        @Override
        public Identifier getFabricId() {
            return MeshResourceListenerKeys.STRUCTURE_NAME_CACHE;
        }

        public Set<Identifier> getStructureNames() {
            return structureNames;
        }

        @Override
        public Collection<Identifier> getFabricDependencies() {
            return Collections.singleton(ResourceReloadListenerKeys.TAGS);
        }

        @Override
        public void reload(ResourceManager manager) {
            structureNames = manager.findResources(PREFIX, path -> path.endsWith(SUFFIX)).stream().map(path -> new Identifier(path.getNamespace(), path.getPath().substring(PREFIX_LENGTH, path.getPath().length() - SUFFIX_LENGTH))).collect(Collectors.toSet());
        }
    }

}
