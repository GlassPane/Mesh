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

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

/**
 * dummy class that does not generate any recipe files
 */
class DummyRecipeFactory implements RecipeFactory {

    DummyRecipeFactory() {
    }

    @Override
    public RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, String recipeGroup, Object... recipe) {
        return this;
    }

    @Override
    public RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this;
    }

    @Override
    public RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this;
    }

    @Override
    public RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, String group, Object input, float experience, int cookingTime) {
        return this;
    }

    @Override
    public RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, String group, Object... ingredients) {
        return this;
    }
}
