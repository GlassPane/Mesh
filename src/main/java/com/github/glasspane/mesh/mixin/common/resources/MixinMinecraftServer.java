package com.github.glasspane.mesh.mixin.common.resources;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.impl.resource.ExternalResourcePackCreator;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackContainerManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    @Final
    private ResourcePackContainerManager<ResourcePackContainer> resourcePackContainerManager;

    @Inject(method = "method_3800", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackContainerManager;addCreator(Lnet/minecraft/resource/ResourcePackCreator;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void createResourcepackContainers(File file, LevelProperties properties, CallbackInfo ci) {
        Mesh.getLogger().trace("registering external data pack");
        this.resourcePackContainerManager.addCreator(new ExternalResourcePackCreator());
    }
}
