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
package com.github.glasspane.mesh.crafting.recipe;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

/**
 * universally used for furnace, smoker and blast furnace recipes
 */
public class SmeltingRecipe extends Recipe {

    @SerializedName("ingredient") private final Ingredient input;
    @SerializedName("experience") private final float xp;
    @SerializedName("cookingtime") private final int smeltTime;

    public SmeltingRecipe(Identifier recipeType, ItemStack output, @Nullable Identifier name, @Nullable String group, Ingredient input, float experience, int smeltTime) {
        super(recipeType, output, name, group);
        this.input = input;
        this.xp = experience;
        this.smeltTime = smeltTime;
    }

    public float getExperience() {
        return xp;
    }

    public int getCookingTime() {
        return smeltTime;
    }

    public Ingredient getIngredient() {
        return input;
    }
}
