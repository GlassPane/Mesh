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
package com.github.glasspane.mesh.mixin.common.crafting;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("PublicStaticMixinMember")
@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAccessor {

    @Invoker("registerPotionType")
    static void registerPotionType(Item item) {}

    @Invoker("registerItemRecipe")
    static void registerItemRecipe(Item potionItem, Item modifier, Item resultPotionItem) {}

    @Invoker("registerPotionRecipe")
    static void registerPotionRecipe(Potion input, Item modifier, Potion output) {}
}
