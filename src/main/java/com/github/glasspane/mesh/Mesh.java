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
package com.github.glasspane.mesh;

import com.github.glasspane.mesh.api.MeshApiOptions;
import com.github.glasspane.mesh.api.annotation.CalledByReflection;
import com.github.glasspane.mesh.api.logging.MeshLoggerFactory;
import com.github.glasspane.mesh.api.util.vanity.VanityManager;
import com.github.glasspane.mesh.impl.multiblock.MultiblockReloader;
import com.github.glasspane.mesh.impl.registry.RegistryDiscoverer;
import com.github.glasspane.mesh.util.command.MeshCommand;
import com.github.glasspane.mesh.util.command.alias.AliasCommands;
import com.github.glasspane.mesh.util.config.ConfigReloader;
import com.github.glasspane.mesh.util.itemgroup.MeshItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CalledByReflection
public class Mesh implements ModInitializer {

    public static final String MODID = "mesh";
    public static final String MOD_NAME = "Mesh";
    public static final Object[] NO_LOGGER_PARAMS = new Object[0];
    private static final Logger LOGGER = MeshLoggerFactory.createPrefixLogger(MODID, MOD_NAME);
    private static Path outputDir;

    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * @see MeshApiOptions#FABRIC_DEVELOPMENT_ENVIRONMENT
     * @deprecated moved to MeshApiOptions
     */
    @Deprecated
    public static boolean isDevEnvironment() {
        return MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT;
    }

    /**
     * @see MeshApiOptions#DEBUG_MODE
     * @deprecated moved to MeshApiOptions
     */
    @Deprecated
    public static boolean isDebugMode() {
        return MeshApiOptions.DEBUG_MODE;
    }

    public static Path getOutputDir() {
        if(outputDir == null) {
            outputDir = FabricLoader.getInstance().getGameDirectory().toPath().resolve("mesh");
            if(!Files.exists(outputDir)) {
                try {
                    Files.createDirectories(outputDir);
                } catch (IOException e) {
                    LOGGER.error("unable to create output directory at " + outputDir.toAbsolutePath(), e);
                }
            }
        }
        return outputDir;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Send Reinforcements!", Mesh.NO_LOGGER_PARAMS);
        RegistryDiscoverer.init();
        VanityManager.getInstance().parseRemoteConfig(VanityManager.VANITY_URL).thenRun(() -> VanityManager.getLogger().debug("successfully updated vanity info!", Mesh.NO_LOGGER_PARAMS));
        ConfigReloader.init();
        MultiblockReloader.init();
        MeshCommand.init();
        AliasCommands.init();
        MeshItemGroup.init();
    }
}
