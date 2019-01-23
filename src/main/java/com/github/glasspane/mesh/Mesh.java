/*
 * Mesh
 * Copyright (C) 2019-2019 GlassPane
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

import com.github.glasspane.mesh.util.CalledByReflection;
import com.github.glasspane.mesh.util.logging.PrefixMessageFactory;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.*;

@CalledByReflection
public class Mesh implements ModInitializer {

    public static final String MODID = "mesh";
    public static final String MOD_NAME = "Mesh";
    public static final String VERSION = "${version}";

    //TODO debug switch
    public static boolean isDebugMode() {
        return true;
    }

    private static final Logger log = LogManager.getLogger(MODID, new PrefixMessageFactory(MOD_NAME));
    private static final Logger debugLog = LogManager.getLogger(MODID + "-debug", new PrefixMessageFactory(MOD_NAME + " Debug"));

    public static Logger getLogger() {
        return log;
    }

    public static Logger getDebugLogger() {
        return debugLog;
    }

    @Override
    public void onInitialize() {
        log.info("Send Reinforcements!");
    }
}
