/*
 * Mesh
 * Copyright (C) 2019-2021 UpcraftLP
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
package dev.upcraft.mesh.util.serialization;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.upcraft.mesh.api.util.LazyReference;
import dev.upcraft.mesh.util.serialization.gson.IdentifierAdapter;
import dev.upcraft.mesh.util.serialization.gson.IngredientJsonSerializer;
import dev.upcraft.mesh.util.serialization.gson.ItemStackJsonSerializer;
import dev.upcraft.mesh.util.serialization.gson.RegistryValueAdapter;
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
    public static final LazyReference<Gson> GSON = new LazyReference<>(() -> new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().registerTypeHierarchyAdapter(Text.class, new Text.Serializer()).registerTypeAdapter(Identifier.class, new IdentifierAdapter()).registerTypeAdapter(Item.class, new RegistryValueAdapter<>(Registry.ITEM)).registerTypeAdapter(ItemStack.class, new ItemStackJsonSerializer()).registerTypeAdapter(Ingredient.class, new IngredientJsonSerializer()).create());

    /**
     * Convenience method for reading an "icon" {@link ItemStack}<br/>
     * This will <strong>ignore</strong> any amount, if present<br/>
     * It will also not error if a {@code data} tag is present, and will just ignore it.
     *
     * @see AdvancementDisplay#iconFromJson(JsonObject)
     */
    @SuppressWarnings("JavadocReference") //make the javadoc not error for referencing a private method
    public static ItemStack iconFromJson(JsonObject json) throws JsonParseException {
        Item item = JsonHelper.getItem(json, "item");
        ItemStack stack = new ItemStack(item);
        if (json.has("nbt")) {
            try {
                stack.setTag(StringNbtReader.parse(JsonHelper.getString(json, "nbt")));
            } catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT tag", e);
            }
        }
        return stack;
    }
}
