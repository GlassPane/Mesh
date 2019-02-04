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
package com.github.glasspane.mesh.crafting;

import com.github.glasspane.mesh.Mesh;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.io.File;

public interface RecipeFactory {
    //TODO brewing
    //TODO new vanilla "machines"
    //TODO Item/Block Tags

    /**
     * create a new instance of the recipe factory that is tied to the provided mod container
     */
    static RecipeFactory getInstance(String modid) {
        if(Mesh.isDevEnvironment()) {
            String resourcesDir = System.getProperty("mesh.resourcesDir");
            if(resourcesDir == null) {
                throw new IllegalStateException("mesh.resourcesDir property not set!");
            }
            RecipeFactoryImpl factory = new RecipeFactoryImpl(modid, new File(resourcesDir, "data/" + modid));
            RecipeFactoryImpl.addFactory(factory);
            return factory;
        }
        return new DummyRecipeFactory();
    }

    /**
     * create a shaped recipe
     *
     * @param output      the resulting ItemStack
     * @param recipeGroup the recipe group (for the recipe book)
     * @param recipe      first the pattern (as up to three {@link String}s), the ingredients, in the form of a char to {@link Ingredient} mapping, ex: 's', Items.Stick
     */
    default RecipeFactory addShaped(ItemStack output, String recipeGroup, Object... recipe) {
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
    RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, String recipeGroup, Object... recipe);

    default RecipeFactory addSmelting(ItemStack output, String group, Object input, float experience, int cookingTime) {
        return addSmelting(output, null, group, input, experience, cookingTime);
    }

    RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime);

    default RecipeFactory addBlasting(ItemStack output, String group, Object input, float experience, int cookingTime) {
        return addBlasting(output, null, group, input, experience, cookingTime);
    }

    RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime);

    default RecipeFactory addSmoking(ItemStack output, String group, Object input, float experience, int cookingTime) {
        return addSmoking(output, null, group, input, experience, cookingTime);
    }

    RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime);

    default RecipeFactory addShapeless(ItemStack output, String group, Object... ingredients) {
        return addShapeless(output, null, group, ingredients);
    }

    RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, String group, Object... ingredients);
}
