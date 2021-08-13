package dev.upcraft.mesh.impl.client.render;

import dev.upcraft.mesh.api.util.vanity.VanityManager;
import dev.upcraft.mesh.client.render.RenderHelper;
import dev.upcraft.mesh.impl.compat.MeshModCompat;
import dev.upcraft.mesh.impl.compat.requiem.MeshRequiemCompat;
import dev.upcraft.mesh.impl.vanity.RegisteredVanityFeatures;
import dev.upcraft.mesh.impl.vanity.feature.EnderSphereFeature;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EnderSphereFeatureRenderer extends FeatureRenderer<LivingEntity, BipedEntityModel<LivingEntity>> {

    public EnderSphereFeatureRenderer(FeatureRendererContext<LivingEntity, BipedEntityModel<LivingEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        UUID uuid = getPlayerUUID(entity);
        if (uuid == null || !VanityManager.getInstance().isAvailable(RegisteredVanityFeatures.ENDER_CAPE, uuid)) {
            return;
        }
        EnderSphereFeature.Config config = VanityManager.getInstance().getFeatureConfig(RegisteredVanityFeatures.ENDER_SPHERE, uuid);

        // moriya: TLDR it returns true.
        if (config.isEnabled()) { // TODO check other conditions
            matrices.push();
            RenderHelper.translateToChest(matrices, this.getContextModel(), entity);
            // TODO make sphere float around player based on age

            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEndPortal());
            // TODO move render to baked model for performance

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(entity.age + tickDelta + 33F));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.cos((entity.age + tickDelta) / 10.0F) * 20.0F));
            matrices.translate(0.35D, -0.25D, -0.65D);

            final float scale = 0.1F + MathHelper.sin((entity.age + tickDelta) / 20.0F) * 0.05F + 0.05F;
            matrices.scale(scale, scale, scale);

            final int subdivisions = 8;
            final float stepSize = 1.0F / subdivisions;

            // here I was testing all sides individually; used to be Direction.values()
            for (Direction dir : Direction.values()) { // for all sides of cube
                matrices.push();

                matrices.multiply(dir.getRotationQuaternion());

                Matrix4f model = matrices.peek().getModel();
                for (float x = -0.5F; x <= 0.5F; x += stepSize) {
                    for (float z = -0.5F; z <= 0.5F; z += stepSize) {
                        Vec3f p00 = new Vec3f(x, 0.5F, z);
                        p00.normalize();
                        Vec3f p10 = new Vec3f(x + stepSize, 0.5F, z);
                        p10.normalize();
                        Vec3f p11 = new Vec3f(x + stepSize, 0.5F, z + stepSize);
                        p11.normalize();
                        Vec3f p01 = new Vec3f(x, 0.5F, z + stepSize);
                        p01.normalize();

                        // up, down:
                        buffer.vertex(model, p00.getX(), p00.getY(), p00.getZ()).next();
                        buffer.vertex(model, p10.getX(), p10.getY(), p10.getZ()).next();
                        buffer.vertex(model, p11.getX(), p11.getY(), p11.getZ()).next();
                        buffer.vertex(model, p01.getX(), p01.getY(), p01.getZ()).next();
                    }
                }
                matrices.pop();
            }

            matrices.pop();
        }
    }

    @Nullable
    private static UUID getPlayerUUID(LivingEntity entity) {
        if (MeshModCompat.REQUIEM_API_INSTALLED) {
            UUID id = MeshRequiemCompat.getPlayerUUID(entity);
            if (id != null) {
                return id;
            }
        }

        if (entity instanceof AbstractClientPlayerEntity clientPlayer) {
            return clientPlayer.getGameProfile().getId();
        }
        return null;
    }
}
