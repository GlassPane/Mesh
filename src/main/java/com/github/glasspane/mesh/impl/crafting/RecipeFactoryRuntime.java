package com.github.glasspane.mesh.impl.crafting;

import com.github.glasspane.mesh.api.crafting.RecipeFactory;
import com.github.glasspane.mesh.util.reflection.MethodInvoker;
import com.github.glasspane.mesh.util.reflection.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.function.Function;

public class RecipeFactoryRuntime implements RecipeFactory {

    private static final MethodInvoker<BrewingRecipeRegistry> _registerPotionType = ReflectionHelper.getMethodInvoker(BrewingRecipeRegistry.class, "method_8080", "registerPotionType", Item.class);
    private static final MethodInvoker<BrewingRecipeRegistry> _registerItemRecipe = ReflectionHelper.getMethodInvoker(BrewingRecipeRegistry.class, "method_8071", "registerItemRecipe", Item.class, Item.class, Item.class);
    private static final MethodInvoker<BrewingRecipeRegistry> _registerPotionRecipe = ReflectionHelper.getMethodInvoker(BrewingRecipeRegistry.class, "method_8074", "registerPotionRecipe", Potion.class, Item.class, Potion.class);

    @Override
    public RecipeFactory addPotionType(Item item) {
        _registerPotionType.invokeStatic(item);
        return this;
    }

    @Override
    public RecipeFactory addPotionItemRecipe(Item potionItem, Item modifier, Item resultPotionItem) {
        _registerItemRecipe.invokeStatic(potionItem, modifier, resultPotionItem);
        return this;
    }

    @Override
    public RecipeFactory addPotionRecipe(Potion input, Item modifier, Potion output) {
        _registerPotionRecipe.invokeStatic(input, modifier, output);
        return this;
    }

    @Override
    public RecipeFactory addShaped(ItemStack output, @Nullable Identifier name, @Nullable String recipeGroup, Object... recipe) {
        return this;
    }

    @Override
    public RecipeFactory addSmelting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime) {
        return this;
    }

    @Override
    public RecipeFactory addBlasting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime) {
        return this;
    }

    @Override
    public RecipeFactory addSmoking(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input, float experience, int cookingTime) {
        return this;
    }

    @Override
    public RecipeFactory addShapeless(ItemStack output, @Nullable Identifier name, @Nullable String group, Object... ingredients) {
        return this;
    }

    @Override
    public <T extends Recipe<?>> RecipeFactory addCustomRecipe(T recipe) {
        return this;
    }

    @Override
    public RecipeFactory addItemTag(Identifier name, boolean replace, Item... items) {
        return this;
    }

    @Override
    public RecipeFactory addBlockTag(Identifier name, boolean replace, Block... blocks) {
        return this;
    }

    @Override
    public <T> RecipeFactory addCustomTag(Identifier name, boolean replace, String tagName, Function<T, Identifier> idMapper, T... objects) {
        return this;
    }

    @Override
    public RecipeFactory addStoneCutting(ItemStack output, @Nullable Identifier name, @Nullable String group, Object input) {
        return this;
    }
}
