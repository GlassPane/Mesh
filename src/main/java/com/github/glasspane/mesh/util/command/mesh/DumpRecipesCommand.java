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
package com.github.glasspane.mesh.util.command.mesh;

import com.github.glasspane.mesh.Mesh;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class DumpRecipesCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> append(LiteralArgumentBuilder<ServerCommandSource> $) {
        return $.then(CommandManager.literal("dump_recipes").executes(context -> {
            //FIXME use paths
            File outDir = new File(Mesh.getOutputDir(), "recipe_dump");
            MinecraftServer server = context.getSource().getMinecraftServer();
            Map<RecipeType<?>, Set<Recipe<?>>> recipes = new IdentityHashMap<>();
            Collection<Recipe<?>> recipeMap = server.getRecipeManager().values();
            recipeMap.forEach(recipe -> recipes.computeIfAbsent(recipe.getType(), type -> new HashSet<>()).add(recipe));
            recipes.keySet().forEach(type -> {
                Identifier typeID = Registry.RECIPE_TYPE.getId(type);
                File outputFile = new File(outDir, typeID.getNamespace() + "/" + typeID.getPath() + ".csv");
                outputFile.getParentFile().mkdirs();
                if(outputFile.exists()) {
                    outputFile.delete();
                }
                try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(outputFile))))) {
                    writer.println("Name,Output");
                    recipes.get(type).stream().sorted(Comparator.comparing(Recipe::getId)).forEachOrdered(recipe -> writer.println(recipe.getId() + "," + Registry.ITEM.getId(recipe.getOutput().getItem())));
                    writer.flush();
                }
                catch (IOException e) {
                    Mesh.getLogger().error("unable to write recipe dump to file: " + outputFile.getAbsolutePath(), e);
                }
                recipes.get(type).forEach(id -> {
                });
            });
            if(server.isSinglePlayer()) {
                Util.getOperatingSystem().open(outDir);
            }
            context.getSource().sendFeedback(new TranslatableText("command.mesh.debug.recipe_dump", recipeMap.size()), true);
            return recipeMap.size();
        }));
    }
}
