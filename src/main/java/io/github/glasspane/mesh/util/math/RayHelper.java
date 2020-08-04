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
package io.github.glasspane.mesh.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class RayHelper {

    /**
     * @param entity        the raytrace source
     * @param range         the maximum range of the raytrace
     * @param shapeType     <b>COLLIDER</b> for collision raytracing, <b>OUTLINE</b> for tracing the block outline shape (render bounding box)
     * @param fluidHandling how to handle fluids
     */
    @Nonnull
    public static HitResult rayTraceEntity(Entity entity, double range, RayTraceContext.ShapeType shapeType, RayTraceContext.FluidHandling fluidHandling) {
        return rayTraceEntity(entity, range, shapeType, fluidHandling, 1.0F);
    }

    /**
     * @param entity        the raytrace source
     * @param range         the maximum range of the raytrace
     * @param shapeType     <b>COLLIDER</b> for collision raytracing, <b>OUTLINE</b> for tracing the block outline shape (render bounding box)
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
     * @param shapeType     <b>COLLIDER</b> for collision raytracing, <b>OUTLINE</b> for tracing the block outline shape (render bounding box)
     * @param fluidHandling how to handle fluids
     */
    @Nonnull
    public static HitResult rayTrace(World world, Entity source, Vec3d start, Vec3d end, RayTraceContext.ShapeType shapeType, RayTraceContext.FluidHandling fluidHandling) {
        return world.rayTrace(new RayTraceContext(start, end, shapeType, fluidHandling, source));
    }
}
