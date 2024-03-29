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
package dev.upcraft.mesh.api.util;

import dev.upcraft.mesh.Mesh;
import net.minecraft.util.Identifier;

public interface MeshResourceListenerKeys {

    Identifier CONFIG_RELOADER = new Identifier(Mesh.MODID, "config_reloader");
    Identifier STRUCTURE_NAME_CACHE = new Identifier(Mesh.MODID, "structure_finder");
}
