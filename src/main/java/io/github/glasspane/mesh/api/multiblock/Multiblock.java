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
package io.github.glasspane.mesh.api.multiblock;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public interface Multiblock<T extends BlockEntity> {

    ServerWorld getWorld();

    MultiblockTemplate<T> getTemplate();

    BlockPos getPosition();

    T getController();

    BlockPos getControllerPosition();

    Direction getOrientation();

    void onMultiblockCreated(@Nullable Entity creator, @Nullable Hand hand);

    void fromTag(CompoundTag compoundTag);

    /**
     * called when a {@link Multiblock} is about to be removed from the world
     */
    void invalidate();

    CompoundTag toTag(CompoundTag compoundTag);
}
