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
package dev.upcraft.mesh.mixin.debug.server;

import dev.upcraft.mesh.impl.debug.RegistryDumper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * THIS CLASS IS ONLY USED IN DEBUG MODE
 */
@Environment(EnvType.SERVER)
@Mixin(value = MinecraftDedicatedServer.class, priority = 99999)
public class MixinMinecraftDedicatedServer {

    @Inject(method = "setupServer", at = @At("HEAD"))
    private void meshInitCallback(CallbackInfoReturnable<Boolean> cir) {
        RegistryDumper.dumpRegistries();
    }
}
