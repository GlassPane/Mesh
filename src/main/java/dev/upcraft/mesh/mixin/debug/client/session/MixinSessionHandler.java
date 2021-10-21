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
package dev.upcraft.mesh.mixin.debug.client.session;

import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.MeshApiOptions;
import dev.upcraft.mesh.util.coremods.session.ClientRunArgsAccessor;
import dev.upcraft.mesh.util.session.minecraft.MeshSessionHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinSessionHandler {

    @Inject(method = "createSocialInteractionsService", at = @At("HEAD"))
    private void construct(YggdrasilAuthenticationService yggdrasilAuthenticationService, RunArgs runArgs, CallbackInfoReturnable<SocialInteractionsService> cir) {
        if(MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT) {
            MeshSessionHandler.tryLoadSession(runArgs.network.session).ifPresent(s -> {
                Mesh.getLogger().warn("Updating session state!");
                ((ClientRunArgsAccessor) runArgs.network).mesh_dev_updateSession(s);
            });
        }
    }
}
