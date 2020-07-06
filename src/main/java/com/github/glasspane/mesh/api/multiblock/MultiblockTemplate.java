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
package com.github.glasspane.mesh.api.multiblock;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.template.multiblock.MultiblockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockTemplate<T extends BlockEntity> {

    private final BlockPos controllerOffset;
    private final MultiblockFactory<T> factory;
    private final Identifier path;
    private BlockPos size;
    private Map<BlockPos, BlockState> stateMap = new HashMap<>();

    public MultiblockTemplate(Identifier path, MultiBlockChecker checker) {
        this(path, controllerOffset, MultiblockBase::new);
    }

    public Map<BlockPos, BlockState> getStateMap() {
        return Collections.unmodifiableMap(stateMap);
    }

    public MultiblockTemplate(Identifier path, BlockPos controllerOffset, MultiblockFactory<T> factory) {
        this.path = path;
        this.controllerOffset = controllerOffset;
        this.factory = factory;
    }

    public Multiblock<T> newInstance(ServerWorld world, BlockPos pos, Direction orientation) {
        return this.factory.newInstance(this, world, pos, orientation);
    }

    public boolean isValidMultiblock(ServerWorld world, BlockPos pos, Direction orientation) {
        BlockRotation rot = toRotation(orientation);
        BlockPos startPos = pos.subtract(Structure.method_15168(this.getControllerOffset(), BlockMirror.NONE, rot, BlockPos.ORIGIN));
        List<BlockPos> exactMatches = this.getExactMatchPositions();
        return this.stateMap.entrySet().stream().allMatch(entry -> {
            BlockPos testPos = startPos.add(Structure.method_15168(entry.getKey(), BlockMirror.NONE, rot, BlockPos.ORIGIN));
            BlockState testState = world.getBlockState(testPos);
            if(exactMatches.contains(entry.getKey())) {
                return entry.getValue().rotate(rot) == testState;
            }
            else {
                Block block = entry.getValue().getBlock();
                return block == Blocks.AIR ? testState.isAir() : block == testState.getBlock();
            }
        });
    }

    public static BlockRotation toRotation(Direction direction) {
        switch(direction) {
            default:
                Mesh.getLogger().warn("invalid direction, only horizontals allowed!", new IllegalStateException("invalid horizontal direction: " + direction.name()));
            case NORTH:
                return BlockRotation.NONE;
            case EAST:
                return BlockRotation.CLOCKWISE_90;
            case SOUTH:
                return BlockRotation.CLOCKWISE_180;
            case WEST:
                return BlockRotation.COUNTERCLOCKWISE_90;
        }
    }

    public BlockPos getControllerOffset() {
        return this.controllerOffset;
    }

    /**
     * @return a list of {@link BlockPos} relative to the structure's position which have to be <strong>exact</strong> matches for the {@link BlockState}, rather than just matching the {@link Block}
     */
    public List<BlockPos> getExactMatchPositions() {
        return Collections.singletonList(this.getControllerOffset());
    }

    public Box getSize() {
        return new Box(BlockPos.ORIGIN, this.size);
    }

    public void setSize(BlockPos size) {
        this.size = size;
    }

    public Identifier getResourcePath() {
        return path;
    }

    public void setStateMap(Map<BlockPos, BlockState> stateMap) {
        this.stateMap = stateMap;
    }
}
