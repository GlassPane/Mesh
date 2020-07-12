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
package io.github.glasspane.mesh.mixin.impl.resources;

import io.github.glasspane.mesh.Mesh;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

//FIXME fix this
@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    //@Shadow @Final private ResourcePackManager dataPackManager;

    //@Inject(method = "loadWorldResourcePack", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;registerProvider(Lnet/minecraft/resource/ResourcePackProvider;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void createResourcepackContainers(File file, LevelProperties properties, CallbackInfo ci) {
        Mesh.getLogger().trace("registering external data pack");
        //this.dataPackManager.registerProvider(new ExternalResourcePackCreator());
        //this.dataPackManager.registerProvider(new VirtualResourcePackCreator());
    }
}
