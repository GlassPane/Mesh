package com.github.glasspane.mesh.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import javax.annotation.Nonnull;

public class MeshHelper {

    /**
     * @param entity        the raytrace source
     * @param range         the maximum range of the raytrace
     * @param shapeType     {@link RayTraceContext.ShapeType#COLLIDER} for collision raytracing, {@link RayTraceContext.ShapeType#OUTLINE} for tracing the block outline shape (render bounding box)
     * @param fluidHandling how to handle fluids
     */
    @Nonnull
    public static HitResult rayTraceEntity(Entity entity, double range, RayTraceContext.ShapeType shapeType, RayTraceContext.FluidHandling fluidHandling) {
        return rayTraceEntity(entity, range, shapeType, fluidHandling, 1.0F);
    }

    /**
     * @param entity        the raytrace source
     * @param range         the maximum range of the raytrace
     * @param shapeType     {@link RayTraceContext.ShapeType#COLLIDER} for collision raytracing, {@link RayTraceContext.ShapeType#OUTLINE} for tracing the block outline shape (render bounding box)
     * @param fluidHandling how to handle fluids
     * @param tickDeltaTime the delta tick time (partial render tick)
     */
    @Nonnull
    public static HitResult rayTraceEntity(Entity entity, double range, RayTraceContext.ShapeType shapeType, RayTraceContext.FluidHandling fluidHandling, float tickDeltaTime) {
        Vec3d startPoint = entity.getCameraPosVec(tickDeltaTime);
        Vec3d lookVec = entity.getRotationVec(tickDeltaTime);
        Vec3d endPoint = startPoint.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        return rayTrace(entity.world, entity, startPoint, endPoint, shapeType, fluidHandling);
    }

    /**
     * @param world         the world
     * @param source        the entity to be used for determining block bounding boxes
     * @param start         the start point
     * @param end           the end point, if no result was found
     * @param shapeType     {@link RayTraceContext.ShapeType#COLLIDER} for collision raytracing, {@link RayTraceContext.ShapeType#OUTLINE} for tracing the block outline shape (render bounding box)
     * @param fluidHandling how to handle fluids
     */
    @Nonnull
    public static HitResult rayTrace(World world, Entity source, Vec3d start, Vec3d end, RayTraceContext.ShapeType shapeType, RayTraceContext.FluidHandling fluidHandling) {
        return world.rayTrace(new RayTraceContext(start, end, shapeType, fluidHandling, source));
    }
}
