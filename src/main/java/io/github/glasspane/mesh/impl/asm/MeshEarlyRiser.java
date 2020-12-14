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
package io.github.glasspane.mesh.impl.asm;

import io.github.glasspane.mesh.api.MeshApiOptions;
import io.github.glasspane.mesh.api.logging.MeshLoggerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.apache.logging.log4j.Logger;

/**
 * NOT USED FOR NOW
 */
public class MeshEarlyRiser implements Runnable {

    private static final String MODID = "mesh";
    private static final Logger logger = MeshLoggerFactory.createPrefixLogger(MODID + "_asm", "Mesh ASM Transformer", () -> MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT);

    @Override
    public void run() {
        //EnvType environment = FabricLoader.getInstance().getEnvironmentType();
        //logger.debug("disabling brewing recipe checks", new Object[0]);
        //logger.trace("doing side-specific ASM: {}", environment::name);
        //if(environment == EnvType.CLIENT) {
        //}
    }
}
