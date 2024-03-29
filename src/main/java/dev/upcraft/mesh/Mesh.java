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
package dev.upcraft.mesh;

import dev.upcraft.mesh.api.MeshApiOptions;
import dev.upcraft.mesh.api.annotation.CalledByReflection;
import dev.upcraft.mesh.api.logging.MeshLoggerFactory;
import dev.upcraft.mesh.api.util.config.ConfigHandler;
import dev.upcraft.mesh.impl.config.MeshConfig;
import dev.upcraft.mesh.impl.registry.ModInfoParser;
import dev.upcraft.mesh.impl.registry.RegistryDiscoverer;
import dev.upcraft.mesh.impl.registry.RegistryProcessor;
import dev.upcraft.mesh.util.command.MeshCommand;
import dev.upcraft.mesh.util.command.alias.AliasCommands;
import dev.upcraft.mesh.util.command.mesh.StructureFilterCommand;
import dev.upcraft.mesh.util.config.ConfigReloader;
import dev.upcraft.mesh.util.itemgroup.MeshItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Util;
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
            outputDir = FabricLoader.getInstance().getGameDir().resolve("mesh");
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
        RegistryDiscoverer.register();
        // FIXME doesn't work on dedicated servers because fabric loader hasn't obtained the game object yet
        //VanityManager.getInstance().parseRemoteConfig(VanityManager.VANITY_URL).thenRun(() -> VanityManager.getLogger().debug("successfully updated vanity info!", Mesh.NO_LOGGER_PARAMS));
        MeshCommand.init();
        AliasCommands.init();
        Util.make(ResourceManagerHelper.get(ResourceType.SERVER_DATA), manager -> {
            manager.registerReloadListener(new ConfigReloader());
            manager.registerReloadListener(StructureFilterCommand.RELOADER);
        });
    }

    @Override
    public void onPreLaunch() {
        LOGGER.info("Send Reinforcements!", Mesh.NO_LOGGER_PARAMS);
        ConfigHandler.registerConfig(MODID, MeshConfig.class);
        ModInfoParser.setup();
    }
}
