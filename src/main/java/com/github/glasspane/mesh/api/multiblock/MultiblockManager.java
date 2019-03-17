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
package com.github.glasspane.mesh.api.multiblock;

import com.github.glasspane.mesh.impl.multiblock.MultiblockManagerImpl;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.MutableRegistry;

import javax.annotation.Nullable;

public interface MultiblockManager {

    static MultiblockManager getInstance() {
        return MultiblockManagerImpl.INSTANCE;
    }

    /**
     * convenient getter for the{@link MultiblockTemplate} registry
     */
    MutableRegistry<MultiblockTemplate<?>> getRegistry();

    /**
     * check if the given structure is valid for the provided {@link MultiblockTemplate} and create it
     * @return an {@link Multiblock} if successful, or {@code null}
     */
    @Nullable
    <T extends BlockEntity> Multiblock<T> tryCreateMultiblock(MultiblockTemplate<T> template, ServerWorld world, BlockPos pos, Direction direction, ServerPlayerEntity player, Hand hand);
}
