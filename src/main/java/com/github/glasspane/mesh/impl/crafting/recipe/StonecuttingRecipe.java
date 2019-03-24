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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

@JsonAdapter(StonecuttingRecipe.Serializer.class)
public class StonecuttingRecipe extends Recipe {

    private static final Identifier TYPE = new Identifier("stonecutting");

    private final Ingredient input;

    public StonecuttingRecipe(ItemStack output, @Nullable Identifier name, @Nullable String group, Ingredient input) {
        super(TYPE, output, name, group);
        this.input = input;
    }

    public Ingredient getInput() {
        return input;
    }

    public static class Serializer implements JsonSerializer<StonecuttingRecipe> {

        @Override
        public JsonElement serialize(StonecuttingRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.add("type", context.serialize(src.getRecipeType()));
            json.addProperty("__created", "generated using the Mesh Library");
            json.addProperty("__author", "UpcraftLP");
            if(src.getRecipeGroup() != null) {
                json.addProperty("group", src.getRecipeGroup());
            }
            json.add("ingredient", context.serialize(src.getInput()));
            json.add("result", context.serialize(Registry.ITEM.getId(src.getOutput().getItem())));
            json.addProperty("count", src.getOutput().getAmount());
            return json;
        }
    }
}
