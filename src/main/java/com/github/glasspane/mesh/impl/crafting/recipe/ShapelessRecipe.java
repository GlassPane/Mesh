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
package com.github.glasspane.mesh.impl.crafting.recipe;

import com.github.glasspane.mesh.util.RecipeHelper;
import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ShapelessRecipe extends Recipe {

    private transient static final Identifier TYPE = new Identifier("crafting_shapeless");

    @SerializedName("ingredients")
    private final Ingredient[] inputs;

    public ShapelessRecipe(ItemStack output, @Nullable Identifier name, @Nullable String group, Object... inputs) {
        super(TYPE, output, name, group);
        this.inputs = Arrays.stream(inputs).map(RecipeHelper::toIngredient).toArray(Ingredient[]::new);
    }

    public Ingredient[] getIngredients() {
        return inputs;
    }
}
