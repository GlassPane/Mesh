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
package dev.upcraft.mesh.impl.registry;

import com.google.gson.annotations.SerializedName;
import dev.upcraft.mesh.api.annotation.AutoRegistry;
import dev.upcraft.mesh.api.util.MeshModInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class RegisterInfoImpl implements MeshModInfo.RegisterInfo {

    @SerializedName("owner")
    private final String ownerClass = null;

    @SerializedName("registry")
    private final String registryName = null;

    @SerializedName("modid")
    private final String modid = null;

    @SerializedName("fields")
    private final List<String> fieldsToRegister = Collections.emptyList();

    @SerializedName("required_mods")
    private final List<String> requiredModIDs = Collections.emptyList();

    @SerializedName("source")
    private final String registrySource = null;

    @Override
    public String getOwnerModid() {
        return modid;
    }

    @Override
    public String getOwnerClass() {
        return ownerClass;
    }

    @Override
    public <T> RegistryKey<Registry<T>> getRegistry() {
        return RegistryKey.ofRegistry(new Identifier(registryName));
    }

    @Override
    public List<String> getFieldsToRegister() {
        return fieldsToRegister;
    }

    @Override
    public List<String> getRequiredMods() {
        return requiredModIDs;
    }

    @Override
    public AutoRegistry.SourceType getRegistrySource() {
        return AutoRegistry.SourceType.fromString(registrySource);
    }
}
