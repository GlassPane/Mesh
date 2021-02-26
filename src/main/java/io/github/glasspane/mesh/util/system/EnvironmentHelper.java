/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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
package io.github.glasspane.mesh.util.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public final class EnvironmentHelper {

    private static final EnvType currentEnv = FabricLoader.getInstance().getEnvironmentType();

    private EnvironmentHelper() {
        // NO-OP
    }

    /**
     * checks whether the current environment is a dedicated server configuration
     *
     * @throws IllegalStateException if the current environment is not a dedicated server
     */
    public static void checkServer() {
        checkEnvironment(EnvType.SERVER);
    }

    /**
     * checks whether the current environment is clientside
     *
     * @throws IllegalStateException if the current environment is not a client
     */
    public static void checkClient() {
        checkEnvironment(EnvType.CLIENT);
    }

    /**
     * checks whether the current environment matches the expected type
     *
     * @throws IllegalStateException if the expected environment is not {@code null} and does not match the current environment
     */
    public static void checkEnvironment(@Nullable EnvType expected) {
        if (expected != null && currentEnv != expected) {
            throw new IllegalStateException("Expected environment to be " + expected.name() + ", was " + currentEnv.name());
        }
    }
}
