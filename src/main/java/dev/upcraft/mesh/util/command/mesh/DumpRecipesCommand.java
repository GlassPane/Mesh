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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.MeshApiOptions;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DumpRecipesCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("dump_recipes").executes(context -> {
            Path outDir = Mesh.getOutputDir().resolve("recipe_dump");
            MinecraftServer server = context.getSource().getServer();
            Map<RecipeType<?>, Set<Recipe<?>>> recipes = new IdentityHashMap<>();
            Collection<Recipe<?>> recipeMap = server.getRecipeManager().values();
            recipeMap.forEach(recipe -> recipes.computeIfAbsent(recipe.getType(), type -> new HashSet<>()).add(recipe));
            recipes.forEach((type, values) -> {
                Identifier typeID = Registry.RECIPE_TYPE.getId(type);
                Path outputFile = outDir.resolve(typeID.getNamespace()).resolve(typeID.getPath() + ".csv");
                try {
                    Files.createDirectories(outputFile.getParent());
                    Files.deleteIfExists(outputFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile))) {
                    writer.println("Name,Output");
                    values.stream().sorted(Comparator.comparing(Recipe::getId)).forEachOrdered(recipe -> writer.println(recipe.getId() + "," + Registry.ITEM.getId(recipe.getOutput().getItem())));
                    writer.println();
                    writer.flush();
                } catch (IOException e) {
                    Mesh.getLogger().error("unable to write recipe dump to file: " + outputFile.toAbsolutePath(), e);
                }
            });
            if (server.isSingleplayer() && MeshApiOptions.CLIENTSIDE_ENVIRONMENT) {
                Util.getOperatingSystem().open(outDir.toUri());
            }
            context.getSource().sendFeedback(new TranslatableText("command.mesh.debug.recipe_dump", recipeMap.size()), true);
            return recipeMap.size();
        }));
    }
}
