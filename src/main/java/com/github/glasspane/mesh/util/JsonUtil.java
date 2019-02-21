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
package com.github.glasspane.mesh.util;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.impl.serialization.IdentifierJsonSerializer;
import com.github.glasspane.mesh.impl.serialization.IngredientJsonSerializer;
import com.github.glasspane.mesh.impl.serialization.ItemStackJsonSerializer;
import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class JsonUtil {

    public static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        if(Mesh.isDebugMode()) {
            builder.setPrettyPrinting();
        }
        builder.disableHtmlEscaping();
        builder.registerTypeAdapter(Identifier.class, new IdentifierJsonSerializer());
        builder.registerTypeAdapter(ItemStack.class, new ItemStackJsonSerializer());
        builder.registerTypeAdapter(Ingredient.class, new IngredientJsonSerializer());
        //TODO register type adapters here
        GSON = builder.create();
    }
}
