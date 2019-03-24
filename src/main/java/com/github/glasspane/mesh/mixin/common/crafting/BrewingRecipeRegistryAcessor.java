package com.github.glasspane.mesh.mixin.common.crafting;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("PublicStaticMixinMember")
@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAcessor {

    @Invoker("registerPotionType")
    static void registerPotionType(Item item) {}

    @Invoker("registerItemRecipe")
    static void registerItemRecipe(Item potionItem, Item modifier, Item resultPotionItem) {}

    @Invoker("registerPotionRecipe")
    static void registerPotionRecipe(Potion input, Item modifier, Potion output) {}
}
