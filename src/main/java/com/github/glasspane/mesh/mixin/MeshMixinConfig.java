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
package com.github.glasspane.mesh.mixin;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.annotation.CalledByReflection;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

@CalledByReflection
public class MeshMixinConfig implements IMixinConfigPlugin {
    private static final String PACKAGE_NAME = "com.github.glasspane.mesh.mixin";
    private static final ImmutableMap<String, BooleanSupplier> MIXIN_STATES = ImmutableMap.of(
            PACKAGE_NAME + "common.MixinRecipeManager", Mesh::isDevEnvironment,
            PACKAGE_NAME + "client.MixinMinecraftClient", Mesh::isDebugMode,
            PACKAGE_NAME + "server.MixinMinecraftDedicatedServer", Mesh::isDebugMode
    );

    @Override
    public void onLoad(String mixinPackage) {
        if(!PACKAGE_NAME.equals(mixinPackage)) {
            throw new IllegalArgumentException("Invalid Package: " + mixinPackage + ", expected: " + PACKAGE_NAME);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null; //fall back to default value
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return MIXIN_STATES.getOrDefault(mixinClassName, () -> false).getAsBoolean();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        //NO-OP
    }

    @Override
    public List<String> getMixins() {
        return Collections.emptyList(); //NO-OP
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        //NO-OP
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        //NO-OP
    }
}
