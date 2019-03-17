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
package com.github.glasspane.mesh.impl.multiblock;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.multiblock.MultiblockManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class MultiblockReloader implements SimpleSynchronousResourceReloadListener {

    public static void init() {
        Mesh.getLogger().debug("enabling multiblock reloader");
        ResourceManagerHelper.get(ResourceType.DATA).registerReloadListener(new MultiblockReloader());
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
                BlockPos size = getPos(structureTag, "size");
                ListTag blockTag = structureTag.getList("blocks", NbtType.COMPOUND);
                ListTag paletteTag = structureTag.getList("palette", NbtType.COMPOUND);
                List<BlockPos> exactMatches = template.getExactMatchPositions();
                Map<BlockPos, Predicate<BlockState>> predicateMap = new HashMap<>();
                for(int i = 0; i < blockTag.size(); i++) {
                    CompoundTag tag = blockTag.getCompoundTag(i);
                    BlockPos pos = getPos(tag, "pos");
                    CompoundTag stateTag = paletteTag.getCompoundTag(tag.getInt("state"));
                    BlockState state = TagHelper.deserializeBlockState(stateTag);
                    Block block = state.getBlock();
                    predicateMap.put(pos, exactMatches.contains(pos) ? state1 -> state1 == state : state1 -> state1.getBlock() == block);
                }
                template.setSize(size);
                template.setPredicates(predicateMap);
                File output = new File("structures/" + template.getResourcePath().getNamespace() + "/" + template.getResourcePath().getPath() + ".json");
                Mesh.getLogger().debug("writing structure data to {}", output::getAbsolutePath);
                output.getParentFile().mkdirs();
                try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output))) {
                    gson.toJson(Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, structureTag), writer);
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
