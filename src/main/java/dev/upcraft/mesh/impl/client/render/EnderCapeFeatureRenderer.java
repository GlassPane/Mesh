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
package dev.upcraft.mesh.impl.client.render;

import dev.upcraft.mesh.api.util.vanity.VanityManager;
import dev.upcraft.mesh.impl.vanity.RegisteredVanityFeatures;
import dev.upcraft.mesh.impl.vanity.feature.EnderCapeFeature;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class EnderCapeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public EnderCapeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (VanityManager.getInstance().isAvailable(RegisteredVanityFeatures.ENDER_CAPE, player.getUuid()) && player.canRenderCapeTexture() && !player.isInvisible() && player.isPartVisible(PlayerModelPart.CAPE)) {
            EnderCapeFeature.Config config = VanityManager.getInstance().getFeatureConfig(RegisteredVanityFeatures.ENDER_CAPE, player.getUuid());
            if (config.isEnabled() && !isWearingElytra(player)) {
                matrices.push();
                matrices.translate(0.0D, 0.0D, 0.125D);
                double x = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp(tickDelta, player.prevX, player.getX());
                double y = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp(tickDelta, player.prevY, player.getY());
                double z = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
                float n = player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw);
                double o = MathHelper.sin(n * 0.017453292F);
                double p = -MathHelper.cos(n * 0.017453292F);
                float rotXPositive = (float) y * 10.0F;
                rotXPositive = MathHelper.clamp(rotXPositive, -6.0F, 32.0F);
                float rotXNegative = (float) (x * o + z * p) * 100.0F;
                rotXNegative = MathHelper.clamp(rotXNegative, 0.0F, 150.0F);
                float s = (float) (x * p - z * o) * 100.0F;
                s = MathHelper.clamp(s, -20.0F, 20.0F);
                if (rotXNegative < 0.0F) {
                    rotXNegative = 0.0F;
                }

                float stride = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
                rotXPositive += MathHelper.sin(MathHelper.lerp(tickDelta, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * stride;
                if (player.isInSneakingPose()) {
                    rotXPositive += 25.0F;
                }

                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + rotXNegative / 2.0F + rotXPositive));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEndPortal());
                this.getContextModel().cloak.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

                matrices.pop();
            }

        }
    }

    private static boolean isWearingElytra(AbstractClientPlayerEntity player) {
        ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
        return chestStack.getItem() == Items.ELYTRA;
    }
}
