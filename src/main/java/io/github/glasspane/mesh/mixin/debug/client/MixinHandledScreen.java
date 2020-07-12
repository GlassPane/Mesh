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
package io.github.glasspane.mesh.mixin.debug.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
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
@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {

    @Shadow
    @Final
    protected T handler;
    @Shadow
    protected int x;
    @Shadow
    protected int y;

    private MixinHandledScreen() {
        super(NarratorManager.EMPTY);
        throw new UnsupportedOperationException("Mixin constructor cannot be called");
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (Slot slot : handler.slots) {
            if (slot != null && slot.getStack().getCount() == 0) {
                String slotId = String.valueOf(slot instanceof CreativeInventoryScreen.CreativeSlot ? ((CreativeInventoryScreen.CreativeSlot) slot).slot.id : slot.id);
                textRenderer.draw(matrices, (slotId), x + slot.x, y + slot.y, 0xFFFFFF);
            }
        }
    }
}
