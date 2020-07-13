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
package io.github.glasspane.mesh;

import io.github.glasspane.mesh.api.MeshApiOptions;
import io.github.glasspane.mesh.api.annotation.CalledByReflection;
import io.github.glasspane.mesh.api.logging.MeshLoggerFactory;
import io.github.glasspane.mesh.api.util.config.ConfigHandler;
import io.github.glasspane.mesh.api.util.vanity.VanityManager;
import io.github.glasspane.mesh.impl.config.MeshConfig;
import io.github.glasspane.mesh.impl.multiblock.MultiblockReloader;
import io.github.glasspane.mesh.impl.registry.ModInfoParser;
import io.github.glasspane.mesh.impl.registry.RegistryDiscoverer;
import io.github.glasspane.mesh.impl.registry.RegistryProcessor;
import io.github.glasspane.mesh.util.command.MeshCommand;
import io.github.glasspane.mesh.util.command.alias.AliasCommands;
import io.github.glasspane.mesh.util.config.ConfigReloader;
import io.github.glasspane.mesh.util.itemgroup.MeshItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CalledByReflection
public class Mesh implements ModInitializer, PreLaunchEntrypoint {

    public static final String MODID = "mesh";
    public static final String MOD_NAME = "Mesh";
    public static final Object[] NO_LOGGER_PARAMS = new Object[0];
    private static final Logger LOGGER = MeshLoggerFactory.createPrefixLogger(MODID, MOD_NAME);
    private static Path outputDir;

    public static Logger getLogger() {
        return LOGGER;
    }

    public static MeshConfig getConfig() {
        return ConfigHandler.getConfig(MeshConfig.class);
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
        if (outputDir == null) {
            outputDir = FabricLoader.getInstance().getGameDirectory().toPath().resolve("io/github/glasspane/mesh");
            if (!Files.exists(outputDir)) {
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
        MeshItemGroup.init();
        RegistryProcessor.init();
        ConfigReloader.init();
        MultiblockReloader.init();
        RegistryDiscoverer.register();
        VanityManager.getInstance().parseRemoteConfig(VanityManager.VANITY_URL).thenRun(() -> VanityManager.getLogger().debug("successfully updated vanity info!", Mesh.NO_LOGGER_PARAMS));
        MeshCommand.init();
        AliasCommands.init();
    }

    @Override
    public void onPreLaunch() {
        LOGGER.info("Send Reinforcements!", Mesh.NO_LOGGER_PARAMS);
        ConfigHandler.registerConfig(MODID, MeshConfig.class);
        ModInfoParser.setup();
    }
}
