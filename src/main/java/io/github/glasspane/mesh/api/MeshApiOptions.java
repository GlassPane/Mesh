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
package io.github.glasspane.mesh.api;

import io.github.glasspane.mesh.api.logging.MeshLoggerFactory;
import io.github.glasspane.mesh.impl.config.MeshSystemProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

/**
 * holder class for all API debug flags
 */
public final class MeshApiOptions {

    static {
        MeshSystemProperties.load();
    }

    /**
     * Fabric's flag for determining whether we are in a development environment
     *
     * @since 1.0.2
     */
    public static final boolean FABRIC_DEVELOPMENT_ENVIRONMENT = Boolean.getBoolean("fabric.development");

    /**
     * whether or not we are on a physical client
     *
     * @since 2.0
     */
    public static final boolean CLIENTSIDE_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

    /**
     * global debug flag. if this is enabled, all other debug flags will be.
     *
     * @since 1.0.2
     */
    public static final boolean DEBUG_MODE = Boolean.getBoolean("mesh.debug");

    /**
     * switch for setting loggers created by the {@link MeshLoggerFactory} to the level defined in {@link MeshApiOptions#DEBUG_LOG_LEVEL}
     *
     * @since 1.0.2
     */
    public static final boolean DEBUG_LOGGING_ENABLED = DEBUG_MODE || Boolean.getBoolean("mesh.debug.logging");

    /**
     * used to set the log level for all our Loggers.
     *
     * @see MeshLoggerFactory
     * @since 1.0.2
     */
    public static final Level DEBUG_LOG_LEVEL = Level.toLevel(System.getProperty("mesh.debug.logging.level"), FABRIC_DEVELOPMENT_ENVIRONMENT ? Level.ALL : Level.DEBUG);

    /**
     * whether or not to output a data dump of virtually injected resources
     *
     * @since 1.0.2
     */
    public static final boolean CREATE_VIRTUAL_DATA_DUMP = DEBUG_MODE || Boolean.getBoolean("mesh.debug.virtual.datadump");

    /**
     * whether or not to display the number of slots in GUIs
     *
     * @since 1.1.0
     */
    public static final boolean RENDER_SLOT_NUMBERS = DEBUG_MODE || Boolean.getBoolean("mesh.debug.render.slotnumber");

    /**
     * global switch to disable all vanity features
     *
     * @since 1.0.2
     */
    public static final boolean VANITY_FEATURES_ENABLED = !Boolean.getBoolean("mesh.debug.vanity.disabled");

    /**
     * if enabled, will enable additional debugging for vanity features
     *
     * @since 1.0.2
     */
    public static final boolean VANITY_DEBUG = VANITY_FEATURES_ENABLED && (DEBUG_MODE || Boolean.getBoolean("mesh.debug.vanity"));

    private MeshApiOptions() {
        //NO-OP
    }
}
