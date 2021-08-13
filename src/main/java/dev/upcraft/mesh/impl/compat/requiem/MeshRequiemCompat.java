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
package dev.upcraft.mesh.impl.compat.requiem;

import ladysnake.requiem.api.v1.possession.Possessable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MeshRequiemCompat {

    @Nullable
    public static UUID getPlayerUUID(LivingEntity entity) {
        if(entity instanceof Possessable possessable && possessable.isBeingPossessed()) {
            PlayerEntity thePlayer = possessable.getPossessor();
            if(thePlayer != null) {
                return thePlayer.getGameProfile().getId();
            }
        }
        return null;
    }
}
