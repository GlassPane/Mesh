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
package com.github.glasspane.mesh.template.multiblock;

import com.github.glasspane.mesh.api.multiblock.Multiblock;
import com.github.glasspane.mesh.api.multiblock.MultiblockTemplate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public class MultiblockBase<T extends BlockEntity> implements Multiblock<T> {

    private final MultiblockTemplate<T> template;
    private final ServerWorld world;
    private final BlockPos position;
    private final BlockPos controllerPosition;
    private final Direction orientation;

    public MultiblockBase(MultiblockTemplate<T> template, ServerWorld world, BlockPos position, Direction orientation) {
        this.template = template;
        this.world = world;
        this.position = position;
        this.orientation = orientation;
        this.controllerPosition = position.add(template.getControllerOffset());
    }

    @Override
    public T getController() {
        BlockEntity be = this.world.getBlockEntity(this.getControllerPosition());
        //noinspection unchecked
        return be != null ? (T) be : null;
    }

    @Override
    public MultiblockTemplate<T> getTemplate() {
        return template;
    }

    @Override
    public ServerWorld getWorld() {
        return world;
    }

    @Override
    public BlockPos getPosition() {
        return position;
    }

    @Override
    public BlockPos getControllerPosition() {
        return controllerPosition;
    }

    @Override
    public Direction getOrientation() {
        return orientation;
    }

    @Override
    public void onMultiblockCreated(@Nullable Entity creator, @Nullable Hand hand) {
        //NO-OP
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        //NO-OP
    }

    @Override
    public void invalidate() {
        //NO-OP
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        //NO-OP
        return compoundTag;
    }
}
