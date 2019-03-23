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
package com.github.glasspane.mesh.api.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface RecipeFactory {
    //TODO cartography table
    //TODO fletching table
    //TODO loom
    //TODO smithing table
    //TODO grindstone

    /**
     * register items that can hold potions
     * (splash potions, lingering potions, potion bottles)
     */
    RecipeFactory addPotionType(Item item);

    /**
     * register recipes that change the item of a potion, not the potion itself
     *
     * @param modifier the item that is applied to the potion item
     */
    RecipeFactory addPotionItemRecipe(Item potionItem, Item modifier, Item resultPotionItem);

    RecipeFactory addPotionRecipe(Potion input, Item modifier, Potion output);

    /**
     * create a shaped recipe
     *
     * @param output      the resulting ItemStack
     * @param recipeGroup the recipe group (for the recipe book)
     * @param recipe      first the pattern (as up to three {@link String}s), the ingredients, in the form of a char to {@link Ingredient} mapping, ex: 's', Items.Stick
     */
    default RecipeFactory addShaped(ItemStack output, @Nullable String recipeGroup, Object... recipe) {
        return addShaped(output, null, recipeGroup, recipe);
    }

    /**
     * create a shaped recipe
     *
     * @param output      the resulting ItemStack
     * @param name        the recipe name, defaults to the item's registry name if not present
     * @param recipeGroup the recipe group (for the recipe book)
     * @param recipe      first the pattern (as up to three {@link String}s), the ingredients, in the form of a char to {@link Ingredient} mapping, ex: 's', Items.Stick
     */
    RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, @Nullable String recipeGroup, Object... recipe);

    default RecipeFactory addSmelting(ItemStack output, @Nullable String group, Object input, float experience, int cookingTime) {
        return addSmelting(output, null, group, input, experience, cookingTime);
    }

    RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime);

    default RecipeFactory addBlasting(ItemStack output, String group, Object input, float experience, int cookingTime) {
        return addBlasting(output, null, group, input, experience, cookingTime);
    }

    RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime);

    default RecipeFactory addSmoking(ItemStack output, @Nullable String group, Object input, float experience, int cookingTime) {
        return addSmoking(output, null, group, input, experience, cookingTime);
    }

    RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime);

    default RecipeFactory addShapeless(ItemStack output, @Nullable String group, Object... ingredients) {
        return addShapeless(output, null, group, ingredients);
    }

    RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, @Nullable String group, Object... ingredients);

    <T extends Recipe<?>> RecipeFactory addCustomRecipe(T recipe);

    RecipeFactory addItemTag(Identifier name, boolean replace, Item... items);

    RecipeFactory addBlockTag(Identifier name, boolean replace, Block... blocks);

    <T> RecipeFactory addCustomTag(Identifier name, boolean replace, String tagName, Function<T, Identifier> idMapper, T... objects);

    default RecipeFactory addStoneCutting(ItemStack output, @Nullable String group, Object input) {
        return addStoneCutting(output, null, group, input);
    }

    RecipeFactory addStoneCutting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input);
}
