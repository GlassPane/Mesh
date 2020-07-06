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
package com.github.glasspane.mesh.api.crafting;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.List;

public class MeshBrewingRecipeRegistry {

    public static void registerPotionType(Ingredient type) {
        BrewingRecipeRegistry.POTION_TYPES.add(type);
    }

    public static void registerPotionType(Item item) {
        registerPotionType(Ingredient.ofItems(item));
    }

    public static void registerItemRecipe(Item inputItem, Ingredient modifier, Item resultItem) {
        BrewingRecipeRegistry.ITEM_RECIPES.add(new BrewingRecipeRegistry.Recipe<>(inputItem, modifier, resultItem));
    }

    public static void registerItemRecipe(Item inputItem, Item modifier, Item resultItem) {
        registerItemRecipe(inputItem, Ingredient.ofItems(modifier), resultItem);
    }

    public static void registerPotionRecipe(Potion input, Item modifier, Potion output) {
        BrewingRecipeRegistry.registerPotionRecipe(input, modifier, output);
    }

    public static List<Ingredient> getPotionTypes() {
        return Collections.unmodifiableList(BrewingRecipeRegistry.POTION_TYPES);
    }
}
