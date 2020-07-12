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
package io.github.glasspane.mesh.util;

import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class RecipeHelper {

    public static Ingredient toIngredient(Object object) {
        if(object instanceof Ingredient) {
            return (Ingredient) object;
        }
        else if(object instanceof ItemConvertible) {
            return Ingredient.ofItems((ItemConvertible) object);
        }
        else if(object instanceof ItemStack) {
            return Ingredient.ofStacks((ItemStack) object);
        }
        else if(object instanceof Tag.Identified) {
            try {
                //noinspection unchecked
                return Ingredient.fromTag((Tag<Item>) object);
            }
            catch (ClassCastException e) {
                throw new IllegalArgumentException(String.format("provided tag (%s) is not an item tag!", ((Tag.Identified<?>) object).getId()));
            }
        }
        else if(object instanceof String) {
            return Ingredient.fromTag(ItemTags.getContainer().method_30210(new Identifier((String) object)));
        }
        else if(object instanceof Identifier) {
            return Ingredient.fromTag(ItemTags.getContainer().method_30210((Identifier) object));
        }
        else if(object instanceof JsonElement) {
            return Ingredient.fromJson((JsonElement) object);
        }
        else if(object == null) {
            return Ingredient.EMPTY;
        }
        else {
            throw new IllegalArgumentException("Cannot convert unknown type to Ingredient: " + object);
        }
    }
}
