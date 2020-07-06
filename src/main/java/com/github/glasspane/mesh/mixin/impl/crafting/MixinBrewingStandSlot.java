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
package com.github.glasspane.mesh.mixin.impl.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/container/BrewingStandContainer$SlotPotion")
public class MixinBrewingStandSlot {

    //make the MC dev plugin shut up, because if we don't do this the compiler will log a warning
    @SuppressWarnings({"UnnecessaryQualifiedMemberReference"})
    @Inject(method = "Lnet/minecraft/container/BrewingStandContainer$SlotPotion;matches(Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private static void matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValueZ() && BrewingRecipeRegistry.POTION_TYPES.stream().anyMatch(ingredient -> ingredient.test(stack))) {
            cir.setReturnValue(true);
        }
    }
}
