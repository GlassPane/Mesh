package com.github.glasspane.mesh.mixin;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.util.CalledByReflection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

@CalledByReflection
public class MeshMixinConfig implements IMixinConfigPlugin {
    private static final String PACKAGE_NAME = "com.github.glasspane.mesh.mixin.common";
    private static final ImmutableList<String> MIXINS = ImmutableList.of("MixinRecipeManager");
    private static final ImmutableMap<String, BooleanSupplier> MIXIN_STATES = ImmutableMap.of(
            PACKAGE_NAME + ".MixinRecipeManager", Mesh::isDevEnvironment
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
        System.out.println("Mixin: " + mixinClassName + " @ " + targetClassName);
        return false;
        //return MIXIN_STATES.getOrDefault(mixinClassName, () -> false).getAsBoolean();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        //NO-OP
    }

    @Override
    public List<String> getMixins() {
        return MIXINS;
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
