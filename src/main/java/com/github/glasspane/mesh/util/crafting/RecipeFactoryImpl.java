/*
 * Mesh
 * Copyright (C) 2019 GlassPane
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
package com.github.glasspane.mesh.util.crafting;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.util.crafting.recipe.*;
import com.github.glasspane.mesh.util.serialization.JsonUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

public class RecipeFactoryImpl implements RecipeFactory {

    private static final Identifier TYPE_FURNACE = new Identifier("smelting");
    private static final Identifier TYPE_BLAST_FURNACE = new Identifier("blasting");
    private static final Identifier TYPE_SMOKER = new Identifier("smoking");
    private static Set<RecipeFactoryImpl> RECIPE_FACTORIES = new HashSet<>();
    private final String modid;
    private final File resourcesDir;
    private final Map<Identifier, Recipe> RECIPES = new HashMap<>();

    RecipeFactoryImpl(String modid, File resourcesDir) {
        this.modid = modid;
        this.resourcesDir = resourcesDir;
    }

    static void addFactory(RecipeFactoryImpl factory) {
        RECIPE_FACTORIES.add(factory);
    }

    public static void createRecipes() {
        if(!Mesh.isDevEnvironment()) {
            return;
        }
        Mesh.getDebugLogger().info("Development environment detected, creating recipe files...");
        RECIPE_FACTORIES.forEach(RecipeFactoryImpl::saveRecipes);
        RECIPE_FACTORIES.clear();
    }

    private void saveRecipes() {
        this.RECIPES.forEach((id, recipe) -> {
            File outputFile = modid.equals(id.getNamespace()) ? this.resourcesDir : new File(this.resourcesDir.getParentFile(), this.modid);
            outputFile = new File(outputFile, "recipes/" + id.getPath() + ".json");
            if(outputFile.exists()) {
                Mesh.getDebugLogger().warn("overriding recipe file: {}", outputFile.getAbsolutePath());
                outputFile.delete();
            }
            outputFile.getParentFile().mkdirs();
            try(FileWriter writer = new FileWriter(outputFile)) {
                writer.write(JsonUtil.GSON.toJson(recipe));
            }
            catch (IOException e) {
                Mesh.getDebugLogger().error("unable to write recipe", e);
            }
        });
    }

    //FIXME implement
    @Override
    public RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, String recipeGroup, Object... recipe) {
        return this.appendRecipe(new ShapedRecipe(output, name, recipeGroup, recipe));
    }

    @Override
    public RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this.appendRecipe(new SmeltingRecipe(TYPE_FURNACE, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this.appendRecipe(new SmeltingRecipe(TYPE_BLAST_FURNACE, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this.appendRecipe(new SmeltingRecipe(TYPE_SMOKER, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, String group, Object... ingredients) {
        return this.appendRecipe(new ShapelessRecipe(output, name, group, ingredients));
    }

    private RecipeFactory appendRecipe(Recipe recipe) {
        if(!RECIPES.containsKey(recipe.getName())) {
            RECIPES.put(recipe.getName(), recipe);
        }
        else {
            Mesh.getDebugLogger().error("duplicate recipe name: {}", recipe.getName());
        }
        return this;
    }
}
