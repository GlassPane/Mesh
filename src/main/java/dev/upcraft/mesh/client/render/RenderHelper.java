package dev.upcraft.mesh.client.render;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3f;

public class RenderHelper {

    /**
     * @since 0.12.0
     */
    public static <T extends LivingEntity> void translateToChest(MatrixStack matrices, BipedEntityModel<T> model, T entity) {
        if (entity.isInSneakingPose() && !model.riding && !entity.isSwimming()) {
            matrices.translate(0.0F, 0.2F, 0.0F);
            matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(model.body.pitch));
        }
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(model.body.yaw));
        matrices.translate(0.0F, 0.4F, -0.16F);
    }
}
