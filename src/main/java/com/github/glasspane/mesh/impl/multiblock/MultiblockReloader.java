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
package com.github.glasspane.mesh.impl.multiblock;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.MeshApiOptions;
import com.github.glasspane.mesh.api.multiblock.MultiblockManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

public class MultiblockReloader implements SimpleSynchronousResourceReloadListener {

    public static void init() {
        Mesh.getLogger().debug("enabling multiblock reloader");
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MultiblockReloader());
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Mesh.MODID, "multiblock_reloader");
    }

    @Override
    public void apply(ResourceManager resourceManager) {
        Mesh.getLogger().debug("reloading multiblock structures");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
        StreamSupport.stream(MultiblockManager.getInstance().getRegistry().spliterator(), true).forEach(template -> {
            Identifier path = new Identifier(template.getResourcePath().getNamespace(), "structures/" + template.getResourcePath().getPath() + ".nbt");
            try(InputStream stream = resourceManager.getResource(path).getInputStream()) {
                CompoundTag structureTag = NbtIo.readCompressed(stream);
                BlockPos size = getPos(structureTag, "size").add(-1, -1, -1);
                ListTag blockTag = structureTag.getList("blocks", NbtType.COMPOUND);
                ListTag paletteTag = structureTag.getList("palette", NbtType.COMPOUND);
                BlockState[] states = new BlockState[paletteTag.size()];
                for(int i = 0; i < paletteTag.size(); i++) {
                    BlockState state = NbtHelper.toBlockState(paletteTag.getCompound(i));
                    //swap structure void and air, to make building easier
                    if(!state.isAir()) {
                        states[i] = state;
                    }
                    else {
                        states[i] = null;
                    }
                }
                Map<BlockPos, BlockState> stateMap = new HashMap<>();
                BlockState air = Blocks.AIR.getDefaultState();
                BlockPos.iterate(BlockPos.ORIGIN, size).forEach(pos -> stateMap.put(pos.toImmutable(), air)); //fill the map with default values
                for(int i = 0; i < blockTag.size(); i++) {
                    CompoundTag tag = blockTag.getCompound(i);
                    BlockPos pos = getPos(tag, "pos");
                    BlockState state = states[tag.getInt("state")];
                    if(state != null) {
                        stateMap.put(pos, state);
                    }
                    else {
                        stateMap.remove(pos);
                    }
                }
                template.setSize(size);
                template.setStateMap(stateMap);
                if(MeshApiOptions.CREATE_VIRTUAL_DATA_DUMP) {
                    String fileName = "structures/" + template.getResourcePath().getNamespace() + "/" + template.getResourcePath().getPath() + ".json";
                    Mesh.getLogger().trace("writing structure {} to json: {}", () -> MultiblockManager.getInstance().getRegistry().getId(template), () -> fileName);
                    Path output = Mesh.getOutputDir().resolve(fileName);
                    Files.createDirectories(output.getParent());
                    try(Writer writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
                        gson.toJson(NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, structureTag), writer);
                    }
                }
            }
            catch (IOException e) {
                Mesh.getLogger().error("exception reloading structure", e);
            }
        });
    }

    private static BlockPos getPos(CompoundTag tag, String name) {
        ListTag posTag = tag.getList(name, NbtType.INT);
        return new BlockPos(posTag.getInt(0), posTag.getInt(1), posTag.getInt(2));
    }
}
