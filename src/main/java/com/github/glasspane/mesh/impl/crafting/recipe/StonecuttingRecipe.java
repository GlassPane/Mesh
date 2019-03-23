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
