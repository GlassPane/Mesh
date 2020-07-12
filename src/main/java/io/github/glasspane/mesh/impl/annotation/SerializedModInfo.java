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
package io.github.glasspane.mesh.impl.annotation;

import com.google.gson.annotations.SerializedName;
import io.github.glasspane.mesh.api.util.MeshModInfo;
import io.github.glasspane.mesh.impl.registry.RegisterInfoImpl;

public class SerializedModInfo implements MeshModInfo {

    @SerializedName("registry")
    private final RegisterInfoImpl[] registerInfos = new RegisterInfoImpl[0];

    @SerializedName("data_generator")
    private final DataGenInfo[] dataGenInfos = new DataGenInfo[0];

    @Override
    public RegisterInfo[] getRegisterData() {
        return registerInfos;
    }

    @Override
    public DataGenInfo[] getDataGenerators() {
        return dataGenInfos;
    }
}
