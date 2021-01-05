/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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
package io.github.glasspane.mesh.mixin.debug.client;

import io.github.glasspane.mesh.impl.debug.RegistryDumper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * THIS CLASS IS ONLY USED IN DEBUG MODE
 */
@Environment(EnvType.CLIENT)
@Mixin(value = MinecraftClient.class, priority = 99999)
public abstract class MixinMinecraftClient {

    //TODO better injection point?
    @Inject(method = "<init>", at = @At("RETURN"))
    private void meshInitCallback(CallbackInfo ci) {
        RegistryDumper.dumpRegistries();
    }
}
