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
package dev.upcraft.mesh.impl.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.MeshApiOptions;
import dev.upcraft.mesh.api.crafting.RecipeCreator;
import dev.upcraft.mesh.impl.resource.CraftingVirtualResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MeshRecipeManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    //FIXME too late to register tags!
    public static void reloadRecipes(Map<Identifier, JsonObject> map, Profiler profiler) {
        Mesh.getLogger().trace("reloading recipe registration...");
        profiler.push(Mesh.MODID + ":recipes");
        CraftingVirtualResourcePack.getInstance().clear();
        Set<Identifier> generated = new HashSet<>();
        FabricLoader.getInstance().getEntrypoints(Mesh.MODID + ":recipes", RecipeCreator.class).forEach(creator -> {
            creator.createRecipes(jsonProvider -> {
                if (map.putIfAbsent(jsonProvider.getRecipeId(), jsonProvider.toJson()) != null) {
                    Mesh.getLogger().debug("Ignoring duplicate recipe {} during reload. (from {})", jsonProvider.getRecipeId(), creator.getClass().getCanonicalName());
                } else {
                    //TODO generate advancements
                    generated.add(jsonProvider.getRecipeId());

                    if (MeshApiOptions.CREATE_VIRTUAL_DATA_DUMP) {
                        Path generatedOut = Mesh.getOutputDir().resolve("generated");
                        Path recipeFile = generatedOut.resolve("data/" + jsonProvider.getRecipeId().getNamespace() + "/recipes/" + jsonProvider.getRecipeId().getPath() + ".json");
                        try {
                            Files.createDirectories(recipeFile.getParent());
                            try (BufferedWriter writer = Files.newBufferedWriter(recipeFile)) {
                                GSON.toJson(jsonProvider.toJson(), writer);
                            }
                        } catch (IOException e) {
                            Mesh.getLogger().error("unable to write file " + recipeFile, e);
                        }
                    }
                }
            });
        });
        Mesh.getLogger().trace("registered {} recipes", generated.size());
        profiler.pop();
    }
}
