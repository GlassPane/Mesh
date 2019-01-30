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

import com.github.glasspane.mesh.crafting.RecipeHelper;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * some of the deserialization logic is copied from <a href=https://gist.github.com/TehNut/f5449de14f3e8107f3e660a73c30bc7b>TehNut's 1.14 Recipe Generator</a>
 */
public class ShapedRecipe extends Recipe {

    private transient static final Identifier TYPE = new Identifier("crafting_shaped");

    @SerializedName("pattern") private final String[] shape;
    @SerializedName("key") private final Map<Character, Ingredient> ingredientMap;

    public ShapedRecipe(ItemStack output, @Nullable Identifier name, String group, Object... recipe) {
        super(TYPE, output, name, group);
        String[] pattern = new String[0];
        Map<Character, Ingredient> ingredients = Maps.newHashMap();
        boolean lookingForShape = true;
        for(int i = 0; i < recipe.length; i++) {
            Object object = recipe[i];
            if(object instanceof String && lookingForShape) {
                pattern = ArrayUtils.add(pattern, (String) object);
            }
            else if(object instanceof Character) {
                Character character = (Character) object;
                lookingForShape = false;
                if(recipe.length < i + 1) {
                    throw new IllegalArgumentException("Character defined with no following valid ingredient");
                }
                ingredients.put(character, RecipeHelper.toIngredient(recipe[++i])); //Add another to skip the ingredient
            }
        }
        this.ingredientMap = ingredients;
        this.shape = pattern;
    }

    public String[] getPattern() {
        return shape;
    }

    public Map<Character, Ingredient> getKeyMap() {
        return ingredientMap;
    }
}