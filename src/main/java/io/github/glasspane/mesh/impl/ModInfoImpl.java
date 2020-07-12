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
package io.github.glasspane.mesh.impl;

import io.github.glasspane.mesh.api.util.MeshModInfo;

import java.util.Set;

public class ModInfoImpl implements MeshModInfo {

    private final Set<String> registryClasses;
    private final Set<String> dataGenerators;

    public ModInfoImpl(Set<String> registryClasses, Set<String> dataGenerators) {
        this.registryClasses = registryClasses;
        this.dataGenerators = dataGenerators;
    }

    @Override
    public Set<String> getRegistryClasses() {
        return registryClasses;
    }

    @Override
    public Set<String> getDataGenerators() {
        return dataGenerators;
    }
}
