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
package io.github.glasspane.mesh.api.util;

import io.github.glasspane.mesh.impl.annotation.DataGenInfo;
import io.github.glasspane.mesh.impl.registry.ModInfoParser;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.List;

public interface MeshModInfo {

    static MeshModInfo get(String modid) {
        return ModInfoParser.getModInfo().get(modid);
    }

    RegisterInfo[] getRegisterData();

    DataGenInfo[] getDataGenerators();

    interface RegisterInfo {
        String getOwnerModid();
        String getOwnerClass();
        <T> RegistryKey<Registry<T>> getRegistry();
        List<String> getFieldsToRegister();
        List<String> getRequiredMods();
    }

}
