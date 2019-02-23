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
import com.github.glasspane.mesh.impl.crafting.RecipeFactoryImpl;
import com.github.glasspane.mesh.impl.registry.RegistryDiscoverer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CalledByReflection
public class Mesh implements ModInitializer {

    public static final String MODID = "mesh";
    public static final String MOD_NAME = "Mesh";
    private static final Logger LOGGER = LogManager.getLogger(MODID, new PrefixMessageFactory(MOD_NAME));
    private static final Logger DEBUG_LOGGER = LogManager.getLogger(MODID + "-debug", new PrefixMessageFactory(MOD_NAME + "/Debug"));
    private static final boolean DEVELOPMENT_ENVIRONMENT = FabricLoader.getInstance().isDevelopmentEnvironment();
    private static final boolean DEBUG_MODE = Boolean.getBoolean("mesh.debug");

    public static Logger getLogger() {
        return LOGGER;
    }

    public static Logger getDebugLogger() {
        return DEBUG_LOGGER;
    }

    //TODO debug switch
    public static boolean isDebugMode() {
        return DEBUG_MODE || isDevEnvironment();
    }

    public static boolean isDevEnvironment() {
        return DEVELOPMENT_ENVIRONMENT;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Send Reinforcements!");
        RegistryDiscoverer.init();
        RecipeFactoryImpl.init();
    }
}
