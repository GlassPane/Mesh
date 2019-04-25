/*
 * Mesh
 * Copyright (C) 2019 GlassPane
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
package com.github.glasspane.mesh;

import com.github.glasspane.mesh.api.annotation.CalledByReflection;
import com.github.glasspane.mesh.api.logging.PrefixMessageFactory;
import com.github.glasspane.mesh.impl.multiblock.MultiblockReloader;
import com.github.glasspane.mesh.impl.registry.RegistryDiscoverer;
import com.github.glasspane.mesh.util.command.MeshCommand;
import com.github.glasspane.mesh.util.config.ConfigReloader;
import com.github.glasspane.mesh.util.itemgroup.MeshItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;

@CalledByReflection
public class Mesh implements ModInitializer {

    public static final String MODID = "mesh";
    public static final String MOD_NAME = "Mesh";
    private static final Logger LOGGER = LogManager.getLogger(MODID, new PrefixMessageFactory(MOD_NAME));
    private static final boolean DEVELOPMENT_ENVIRONMENT = Boolean.getBoolean("fabric.development");
    private static final boolean DEBUG_MODE = Boolean.getBoolean("mesh.debug");
    private static File outputDir;

    static {
        // configure the logger for debug mode
        // see https://logging.apache.org/log4j/2.0/faq.html#reconfig_level_from_code
        Configurator.setLevel(MODID, DEBUG_MODE ? Level.ALL : Level.INFO);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static boolean isDevEnvironment() {
        return DEVELOPMENT_ENVIRONMENT;
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

    public static File getOutputDir() {
        if(outputDir == null) {
            outputDir = new File(FabricLoader.getInstance().getGameDirectory(), "mesh");
            if(!outputDir.exists()) {
                outputDir.mkdirs();
            }
        }
        return outputDir;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Send Reinforcements!");
        RegistryDiscoverer.init();
        if(Compat.FABRIC) {
            ConfigReloader.init();
            MultiblockReloader.init();
            MeshCommand.init();
            MeshItemGroup.init();
        }
    }

    public static class Compat {
        public static final boolean FABRIC = FabricLoader.getInstance().isModLoaded("fabric");

        public static void ensureFabricLoaded() {
            if(!Compat.FABRIC) {
                throw new IllegalStateException("Please install Fabric API!");
            }
        }
    }
}
