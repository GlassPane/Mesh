/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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
package io.github.glasspane.mesh.impl.multiblock;

import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.multiblock.Multiblock;
import io.github.glasspane.mesh.api.multiblock.MultiblockManager;
import io.github.glasspane.mesh.api.multiblock.MultiblockTemplate;
import io.github.glasspane.mesh.impl.registry.MeshRegistries;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.MutableRegistry;
import org.jetbrains.annotations.Nullable;


public class MultiblockManagerImpl implements MultiblockManager {

    public static final MultiblockManager INSTANCE = new MultiblockManagerImpl();

    @Override
    public MutableRegistry<MultiblockTemplate<?>> getRegistry() {
        return MeshRegistries.MULTIBLOCKS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> Multiblock<T> tryCreateMultiblock(MultiblockTemplate<T> template, ServerWorld world, BlockPos pos, Direction direction, ServerPlayerEntity player, Hand hand) {
        if (template.isValidMultiblock(world, pos, direction)) {
            Multiblock<T> multiblock = template.newInstance(world, pos, direction);
            multiblock.onMultiblockCreated(player, hand);
            Mesh.getLogger().debug("Multiblock created at {}, type {}", () -> pos, () -> getRegistry().getId(template));
            return multiblock;
        }
        return null;
    }
}
