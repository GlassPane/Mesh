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
package com.github.glasspane.mesh.impl.crafting;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.crafting.RecipeFactory;
import com.github.glasspane.mesh.impl.crafting.recipe.Recipe;
import com.github.glasspane.mesh.impl.crafting.recipe.ShapedRecipe;
import com.github.glasspane.mesh.impl.crafting.recipe.ShapelessRecipe;
import com.github.glasspane.mesh.impl.crafting.recipe.SmeltingRecipe;
import com.github.glasspane.mesh.util.JsonUtil;
import com.github.glasspane.mesh.util.RecipeHelper;
import com.github.glasspane.mesh.util.reflection.MethodInvoker;
import com.github.glasspane.mesh.util.reflection.ReflectionHelper;
import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecipeFactoryImpl implements RecipeFactory {

    private static final Identifier TYPE_FURNACE = new Identifier("smelting");
    private static final Identifier TYPE_BLAST_FURNACE = new Identifier("blasting");
    private static final Identifier TYPE_SMOKER = new Identifier("smoking");
    private static File resourcesDir;

    //FIXME won't work outside dev!
    // -> extract to json files!
    private static final MethodInvoker<BrewingRecipeRegistry> _registerPotionType = ReflectionHelper.getMethodInvoker(BrewingRecipeRegistry.class, "method_8080", "registerPotionType", Item.class);
    private static final MethodInvoker<BrewingRecipeRegistry> _registerItemRecipe = ReflectionHelper.getMethodInvoker(BrewingRecipeRegistry.class, "method_8071", "registerItemRecipe", Item.class, Item.class, Item.class);
    private static final MethodInvoker<BrewingRecipeRegistry> _registerPotionRecipe = ReflectionHelper.getMethodInvoker(BrewingRecipeRegistry.class, "method_8074", "registerPotionRecipe", Potion.class, Item.class, Potion.class);

    public static void init() {
        String path = System.getProperty("mesh.resourcesDir", null);
        if(path == null) {
            throw new IllegalStateException("mesh.resourcesDir property not set!");
        }
        resourcesDir = new File(path, "data");
    }

    @Override
    public RecipeFactory addPotionType(Item item) {
        _registerPotionType.invokeStatic(item);
        return this;
    }

    @Override
    public RecipeFactory addPotionItemRecipe(Item potionItem, Item modifier, Item resultPotionItem) {
        _registerItemRecipe.invokeStatic(potionItem, modifier, resultPotionItem);
        return this;
    }

    @Override
    public RecipeFactory addPotionRecipe(Potion input, Item modifier, Potion output) {
        _registerPotionRecipe.invokeStatic(input, modifier, output);
        return this;
    }

    @Override
    public RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, String recipeGroup, Object... recipe) {
        return this.saveRecipe(new ShapedRecipe(output, name, recipeGroup, recipe));
    }

    @Override
    public RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this.saveRecipe(new SmeltingRecipe(TYPE_FURNACE, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this.saveRecipe(new SmeltingRecipe(TYPE_BLAST_FURNACE, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this.saveRecipe(new SmeltingRecipe(TYPE_SMOKER, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, String group, Object... ingredients) {
        return this.saveRecipe(new ShapelessRecipe(output, name, group, ingredients));
    }

    @Override
    public <T extends net.minecraft.recipe.Recipe<?>> RecipeFactory addCustomRecipe(T recipe) {
        return this.save(recipe.getId(), JsonUtil.GSON.toJsonTree(recipe));
    }

    private RecipeFactory save(Identifier name, JsonElement json) {
        File outputFile = new File(resourcesDir, name.getNamespace() + "/recipes/" + name.getPath() + ".json");
        if(outputFile.exists()) {
            Mesh.getLogger().trace("Recipe file {} already exists. overwriting!", name, outputFile.getAbsolutePath());
            outputFile.delete();
        }
        outputFile.getParentFile().mkdirs();
        try(FileWriter writer = new FileWriter(outputFile)) {
            JsonUtil.GSON.toJson(json, writer);
        }
        catch (IOException e) {
            Mesh.getLogger().debug("unable to write recipe", e);
        }
        return this;
    }

    private RecipeFactory saveRecipe(Recipe recipe) {
        return save(recipe.getName(), JsonUtil.GSON.toJsonTree(recipe));
    }
}
