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
package io.github.glasspane.mesh.util.player;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

//TODO better text
public class PlayerManagerHelper {

    public static void forceDisconnect(ServerPlayerEntity player, String modid, String reason) {
        String modname = FabricLoader.getInstance().getModContainer(modid).map(ModContainer::getMetadata).map(ModMetadata::getName).orElse(modid);
        player.networkHandler.disconnect(new LiteralText(String.format("%s forced a disconnect: %s", modname, reason)));
    }

    public static void forceDisconnect(ServerPlayerEntity player, String modid, int errorCode) {
        forceDisconnect(player, modid, String.format("error code %s", errorCode));
    }
}
