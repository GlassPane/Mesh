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

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class InventoryHelper {

    public static void giveOrDrop(ServerPlayerEntity player, ItemStack stack) {
        if(player.inventory.insertStack(stack) && stack.isEmpty()) {
            stack.setCount(1);
            ItemEntity entity = player.dropItem(stack, false);
            if(entity != null) {
                entity.method_6987();
            }
            player.world.playSoundFromEntity(null, player, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRand().nextFloat() - player.getRand().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.playerContainer.sendContentUpdates();
        }
        else {
            ItemEntity entity = player.dropItem(stack, false);
            if(entity != null) {
                entity.resetPickupDelay();
                entity.setOwner(player.getUuid());
            }
        }
    }
}
