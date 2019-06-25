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
import com.github.glasspane.mesh.impl.crafting.recipe.StonecuttingRecipe;
import com.github.glasspane.mesh.impl.resource.CraftingVirtualResourcePack;
import com.github.glasspane.mesh.mixin.common.crafting.BrewingRecipeRegistryAccessor;
import com.github.glasspane.mesh.util.JsonUtil;
import com.github.glasspane.mesh.util.RecipeHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public class RecipeFactoryImpl implements RecipeFactory {

    private static final Identifier TYPE_FURNACE = new Identifier("smelting");
    private static final Identifier TYPE_BLAST_FURNACE = new Identifier("blasting");
    private static final Identifier TYPE_SMOKER = new Identifier("smoking");
    private final Map<Identifier, JsonObject> recipeMap;

    public RecipeFactoryImpl(Map<Identifier, JsonObject> recipeMap) {
        this.recipeMap = recipeMap;
    }

    @Override
    public RecipeFactory addPotionType(Item item) {
        BrewingRecipeRegistryAccessor.registerPotionType(item);
        return this;
    }

    @Override
    public RecipeFactory addPotionItemRecipe(Item potionItem, Item modifier, Item resultPotionItem) {
        BrewingRecipeRegistryAccessor.registerItemRecipe(potionItem, modifier, resultPotionItem);
        return this;
    }

    @Override
    public RecipeFactory addPotionRecipe(Potion input, Item modifier, Potion output) {
        BrewingRecipeRegistryAccessor.registerPotionRecipe(input, modifier, output);
        return this;
    }

    @Override
    public RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, @Nullable String recipeGroup, Object... recipe) {
        return this.saveRecipe(new ShapedRecipe(output, name, recipeGroup, recipe));
    }

    @Override
    public RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime) {
        return this.saveRecipe(new SmeltingRecipe(TYPE_FURNACE, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime) {
        return this.saveRecipe(new SmeltingRecipe(TYPE_BLAST_FURNACE, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime) {
        return this.saveRecipe(new SmeltingRecipe(TYPE_SMOKER, output, name, group, RecipeHelper.toIngredient(input), experience, cookingTime));
    }

    @Override
    public RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, @Nullable String group, Object... ingredients) {
        return this.saveRecipe(new ShapelessRecipe(output, name, group, ingredients));
    }

    @Override
    public <T extends net.minecraft.recipe.Recipe<?>> RecipeFactory addCustomRecipe(T recipe) {
        JsonElement element = JsonUtil.GSON.toJsonTree(recipe);
        if(element.isJsonObject()) {
            this.recipeMap.putIfAbsent(recipe.getId(), element.getAsJsonObject());
        }
        else {
            Mesh.getLogger().error("unable to serialize recipe: {}", recipe::getId);
        }
        return this;
    }

    @Override
    public RecipeFactory addItemTag(Identifier name, boolean replace, Item... items) {
        return addCustomTag(name, replace, "items", Registry.ITEM::getId, items);
    }

    @Override
    public RecipeFactory addBlockTag(Identifier name, boolean replace, Block... blocks) {
        return addCustomTag(name, replace, "blocks", Registry.BLOCK::getId, blocks);
    }

    @Override
    public <T> RecipeFactory addCustomTag(Identifier name, boolean replace, String tagName, Function<T, Identifier> idMapper, T... objects) {
        Tag.Builder<T> builder = Tag.Builder.create();
        builder.add(objects);
        JsonObject json = builder.build(name).toJson(idMapper);
        json.addProperty("replace", replace);
        return this.save(name, "tags/" + tagName, json);
    }

    @Override
    public RecipeFactory addStoneCutting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input) {
        return saveRecipe(new StonecuttingRecipe(output, name, group, RecipeHelper.toIngredient(input)));
    }

    @Deprecated
    private RecipeFactory save(Identifier name, String path, JsonObject json) {
        CraftingVirtualResourcePack.getInstance().addResource(ResourceType.SERVER_DATA, new Identifier(name.getNamespace(), path + "/" + name.getPath() + ".json"), json);
        if(Mesh.isDebugMode()) {
            File outputFile = new File(Mesh.getOutputDir(), "virtual_resource_pack_dump/data/" + name.getNamespace() + "/" + path + "/" + name.getPath() + ".json");
            outputFile.getParentFile().mkdirs();
            try(FileWriter writer = new FileWriter(outputFile)) {
                JsonUtil.GSON.toJson(json, writer);
            }
            catch (IOException e) {
                Mesh.getLogger().debug("unable to write recipe", e);
            }
        }
        return this;
    }

    private RecipeFactory saveRecipe(Recipe recipe) {
        JsonElement element = JsonUtil.GSON.toJsonTree(recipe);
        if(element.isJsonObject()) {
            this.recipeMap.putIfAbsent(recipe.getName(), element.getAsJsonObject());
        }
        else {
            Mesh.getLogger().error("unable to serialize recipe: {}", recipe::getName);
        }
        return this;
    }
}
