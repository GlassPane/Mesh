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
package com.github.glasspane.mesh.impl.client.render;

import com.github.glasspane.mesh.api.util.vanity.VanityManager;
import com.github.glasspane.mesh.impl.vanity.RegisteredVanityFeatures;
import com.github.glasspane.mesh.impl.vanity.feature.TitleFeature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

public class TitleFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final int MAX_RENDERING_DISTANCE = 32;

    public TitleFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> rendererContext) {
        super(rendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        EntityRenderDispatcher renderDispatcher = MinecraftClient.getInstance().getEntityRenderManager();
        double distance = renderDispatcher.getSquaredDistanceToCamera(player);
        if(distance <= MAX_RENDERING_DISTANCE) {
            TitleFeature.TitleConfig config = VanityManager.getInstance().getFeatureConfig(RegisteredVanityFeatures.TITLE, player.getUuid());
            TitleFeature.Title currentTitle = config.getCurrentTitle();
            if(currentTitle != null) {
                Text title = config.getCurrentTitle().getTitle();
                TextRenderer textRenderer = renderDispatcher.getTextRenderer();
                boolean textVisible = !player.isSneaky();
                float height = player.getHeight() + 0.7F;
                matrices.push();
                matrices.translate(0.0D, height, 0.0D);
                matrices.multiply(renderDispatcher.getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f modelView = matrices.peek().getModel();
                int bgColor = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
                float xOffset = -textRenderer.getWidth(title) / 2.0F;
                float yOffset = "deadmau5".equals(player.getEntityName()) ? -10.0F : 0.0F;
                textRenderer.draw(title, xOffset, yOffset, 0x20FFFFFF, false, modelView, vertexConsumers, textVisible, bgColor, light);
                if(textVisible) {
                    textRenderer.draw(title, xOffset, yOffset, -1, false, modelView, vertexConsumers, false, 0x000000, light);
                }
                matrices.pop();
            }
        }
    }
}
