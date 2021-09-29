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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.ibm.icu.impl.locale.XCldrStub;
import dev.upcraft.mesh.api.MeshApiOptions;
import dev.upcraft.mesh.api.annotation.CalledByReflection;
import dev.upcraft.mesh.api.logging.MeshLoggerFactory;
import dev.upcraft.mesh.impl.config.MeshSystemProperties;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

@CalledByReflection
public class MeshMixinConfig implements IMixinConfigPlugin {

    private static final String MODID = "mesh";
    private static final String MIXIN_PACKAGE = "dev.upcraft.mesh.mixin.impl";
    private static final boolean DEBUG_MODE = MeshApiOptions.DEBUG_MODE;
    private static final boolean DEVELOPMENT = MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT;
    private static final boolean ARCHITECTURY_LOADED = FabricLoader.getInstance().isModLoaded("architectury");
    private static final Logger LOGGER = MeshLoggerFactory.createPrefixLogger(MODID +"_mixin_config","Mesh Mixin Config", () -> DEBUG_MODE);

    static {
        MeshSystemProperties.load();
    }

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
                // TODO find workaround for achitectury
                case "dev.upcraft.mesh.mixin.impl.command.MixinCommandElement":
                case "dev.upcraft.mesh.mixin.impl.command.MixinCommandManager":
                case "dev.upcraft.mesh.mixin.impl.command.MixinExecuteCommand":
                    if(ARCHITECTURY_LOADED) {
                        LOGGER.trace("Disabling mixin {} for architectury compatibility!", mixinClassName);
                        return false;
                    }
            }
            return true;
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
