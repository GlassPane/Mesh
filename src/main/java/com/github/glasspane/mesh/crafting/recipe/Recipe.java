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

import com.github.glasspane.mesh.util.objects.LazyReference;
import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public abstract class Recipe {

    @SuppressWarnings("unused")
    @SerializedName("__created")
    private final String meshComment = "generated using the Mesh Library";

    @SuppressWarnings("unused")
    @SerializedName("__author")
    private final String authorComment = "UpcraftLP";

    @SerializedName("type") private final Identifier recipeType;
    @Nullable
    @SerializedName("group")
    private final String recipeGroup;
    transient private final LazyReference<Identifier> name;
    @SerializedName("result") private ItemStack output;

    protected Recipe(Identifier recipeType, ItemStack output, @Nullable Identifier name, @Nullable String group) {
        this.recipeType = recipeType;
        this.recipeGroup = group;
        this.name = new LazyReference<>(() -> name != null ? name : Registry.ITEM.getId(this.output.getItem()));
        this.output = output;
    }

    public Identifier getRecipeType() {
        return recipeType;
    }

    @Nullable
    public String getRecipeGroup() {
        return recipeGroup;
    }

    public Identifier getName() {
        return this.name.get();
    }

    public ItemStack getOutput() {
        return output;
    }
}
