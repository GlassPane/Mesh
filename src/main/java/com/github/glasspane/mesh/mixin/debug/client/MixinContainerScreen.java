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
package com.github.glasspane.mesh.mixin.debug.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerProvider;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * THIS CLASS IS ONLY USED IN DEBUG MODE
 */
@Environment(EnvType.CLIENT)
@Mixin(ContainerScreen.class)
public abstract class MixinContainerScreen<T extends Container> extends Screen implements ContainerProvider<T> {

    @Shadow
    @Final
    protected T container;
    @Shadow
    protected int x;
    @Shadow
    protected int y;

    private MixinContainerScreen() {
        super(NarratorManager.EMPTY);
        throw new UnsupportedOperationException("Mixin constructor cannot be called");
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for(Slot slot : container.slots) {
            if(slot != null && slot.getStack().getCount() == 0) {
                String slotId = String.valueOf(slot instanceof CreativeInventoryScreen.CreativeSlot ? ((CreativeInventoryScreen.CreativeSlot) slot).slot.id : slot.id);
                font.draw((slotId), x + slot.xPosition, y + slot.yPosition, 0xFFFFFF);
            }
        }
    }
}
