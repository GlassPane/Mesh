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
package dev.upcraft.mesh.util.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3d;

public class MathUtil {

    public static final Double TAU = 2.0D * Math.PI;

    public static double getDistanceXZ(Vec3d vec1, Vec3d vec2) {
        return getDistanceXZ(vec1.x, vec1.z, vec2.x, vec2.z);
    }

    public static double getDistanceXZ(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * this method uses a static constant because that's what MC does when rendering models.
     * this may not be as accurate as {@link Math#toDegrees(double)}, but it will be more correct for rendering purposes
     */
    @Environment(EnvType.CLIENT)
    public static float toDegrees(float radians) {
        return radians * 57.295776F;
    }
}
