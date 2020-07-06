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
package com.github.glasspane.mesh.util;

import com.github.glasspane.mesh.impl.serialization.IdentifierAdapter;
import com.github.glasspane.mesh.impl.serialization.IngredientJsonSerializer;
import com.github.glasspane.mesh.impl.serialization.ItemStackJsonSerializer;
import com.github.glasspane.mesh.impl.serialization.RegistryValueAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class JsonUtil {

    //TODO register type adapters here
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeHierarchyAdapter(Text.class, new Text.Serializer())
            .registerTypeAdapter(Identifier.class, new IdentifierAdapter())
            .registerTypeAdapter(Item.class, new RegistryValueAdapter<>(Registry.ITEM))
            .registerTypeAdapter(ItemStack.class, new ItemStackJsonSerializer())
            .registerTypeAdapter(Ingredient.class, new IngredientJsonSerializer())
            .create();

    /**
     * Convenience method for reading an "icon" {@link ItemStack}<br/>
     * This will <strong>ignore</strong> any amount, if present<br/>
     * It will also not error if a {@code data} tag is present, and will just ignore it.
     * @see AdvancementDisplay#iconFromJson(JsonObject)
     */
    @SuppressWarnings("JavadocReference") //make the javadoc not error for referencing a private method
    public static ItemStack iconFromJson(JsonObject json) throws JsonParseException {
        Item item = JsonHelper.getItem(json, "item");
        ItemStack stack = new ItemStack(item);
        if(json.has("nbt")) {
            try {
                stack.setTag(StringNbtReader.parse(JsonHelper.getString(json, "nbt")));
            }
            catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT tag", e);
            }
        }
        return stack;
    }
}
