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

import com.github.glasspane.mesh.Mesh;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MultiblockTemplate<T extends BlockEntity> {

    static {
        Mesh.Compat.ensureFabricLoaded();
    }

    private final BlockPos controllerOffset;
    private final MultiblockFactory<T> factory;
    private final Identifier path;
    private BlockPos size;
    private Map<BlockPos, Predicate<BlockState>> predicates = new HashMap<>();

    public MultiblockTemplate(Identifier path, BlockPos controllerOffset, MultiblockFactory<T> factory) {
        this.path = path;
        this.controllerOffset = controllerOffset;
        this.factory = factory;
    }

    public Multiblock<T> newInstance(ServerWorld world, BlockPos pos, Direction orientation) {
        return isValidMultiblock(world, pos, orientation) ? this.factory.newInstance(this, world, pos, orientation) : null;
    }

    public boolean isValidMultiblock(ServerWorld world, BlockPos centerPos, Direction orientation) {
        Rotation rot = toRotation(orientation);
        BlockPos start = centerPos.subtract(Structure.method_15168(this.getControllerOffset(), Mirror.NONE, rot, BlockPos.ORIGIN));
        return !this.predicates.isEmpty() && this.predicates.entrySet().parallelStream().allMatch(entry -> {
            BlockPos pos = start.add(Structure.method_15168(entry.getKey(), Mirror.NONE, rot, BlockPos.ORIGIN));
            return entry.getValue().test(world.getBlockState(pos));
        });
    }

    public static Rotation toRotation(Direction direction) {
        switch(direction) {
            default:
                Mesh.getLogger().warn("invalid direction, only horizontals allowed!", new IllegalStateException("invalid horizontal direction: " + direction.name()));
            case NORTH:
                return Rotation.ROT_0;
            case EAST:
                return Rotation.ROT_90;
            case SOUTH:
                return Rotation.ROT_180;
            case WEST:
                return Rotation.ROT_270;
        }
    }

    public BlockPos getControllerOffset() {
        return this.controllerOffset;
    }

    /**
     * @return a list of {@link BlockPos} relative to the structure's position which have to be <strong>exact</strong> matches for the blockstate, rather than just matching the block
     */
    public List<BlockPos> getExactMatchPositions() {
        return ImmutableList.of(this.getControllerOffset());
    }

    public BoundingBox getSize() {
        return new BoundingBox(BlockPos.ORIGIN, this.size);
    }

    public void setSize(BlockPos size) {
        this.size = size;
    }

    public Identifier getResourcePath() {
        return path;
    }

    public void setPredicates(Map<BlockPos, Predicate<BlockState>> predicates) {
        this.predicates = predicates;
    }
}
