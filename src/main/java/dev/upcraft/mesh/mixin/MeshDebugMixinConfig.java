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
package dev.upcraft.mesh.mixin;


import dev.upcraft.mesh.api.MeshApiOptions;
import dev.upcraft.mesh.api.annotation.CalledByReflection;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

@CalledByReflection
public class MeshDebugMixinConfig implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE = "dev.upcraft.mesh.mixin.debug";
    private static final boolean DEBUG_MODE = MeshApiOptions.DEBUG_MODE;

    @Override
    public void onLoad(String mixinPackage) {
        if (!mixinPackage.startsWith(MIXIN_PACKAGE)) {
            throw new IllegalArgumentException("Invalid Package: " + mixinPackage + ", expected: " + MIXIN_PACKAGE);
        }
    }

    @Nullable
    @Override
    public String getRefMapperConfig() {
        return null; //fall back to default value
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith(MIXIN_PACKAGE)) {
            switch (mixinClassName) {
                case "dev.upcraft.mesh.mixin.debug.client.MixinMinecraftClient":
                case "dev.upcraft.mesh.mixin.debug.server.MixinMinecraftDedicatedServer":
                    return MeshApiOptions.CREATE_VIRTUAL_DATA_DUMP;
                case "dev.upcraft.mesh.mixin.debug.client.MixinHandledScreen":
                    return MeshApiOptions.RENDER_SLOT_NUMBERS;
                case "dev.upcraft.mesh.mixin.debug.client.MixinRemoveAuthError":
                case "dev.upcraft.mesh.mixin.debug.client.session.MixinSessionHandler":
                case "dev.upcraft.mesh.mixin.debug.client.session.RunArgsNetworkAccessor":
                    return MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT;
            }
            return DEBUG_MODE;
        } else {
            throw new IllegalArgumentException("Invalid Package for Class " + mixinClassName + ", expected: " + MIXIN_PACKAGE);
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        //NO-OP
    }

    @Nullable
    @Override
    public List<String> getMixins() {
        return null; //NO-OP
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
