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
package com.github.glasspane.mesh.api.logging;

import com.github.glasspane.mesh.api.MeshApiOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.function.BooleanSupplier;

public class MeshLoggerFactory {

    /**
     * @param shouldDebugLogger whether to debug this logger
     */
    public static Logger createPrefixLogger(String loggerName, String prefix, BooleanSupplier shouldDebugLogger) {
        Logger log = LogManager.getLogger(loggerName, new PrefixMessageFactory(prefix));
        checkDebugLogging(loggerName, shouldDebugLogger);
        return log;
    }

    public static Logger createPrefixLogger(String loggerName, String prefix) {
        return createPrefixLogger(loggerName, prefix, () -> false);
    }

    /**
     * @param shouldDebugLogger whether to debug this logger
     */
    public static Logger createLogger(String loggerName, BooleanSupplier shouldDebugLogger) {
        Logger log = LogManager.getLogger(loggerName);
        checkDebugLogging(loggerName, shouldDebugLogger);
        return log;
    }

    public static Logger createLogger(String loggerName) {
        return createLogger(loggerName, () -> false);
    }

    public static void checkDebugLogging(String loggerName) {
        checkDebugLogging(loggerName, () -> false);
    }

    /**
     * enables debug logging for each logger, if enabled
     * @see MeshApiOptions#DEBUG_LOGGING_ENABLED
     */
    public static void checkDebugLogging(String loggerName, BooleanSupplier shouldDebugLogger) {
        if(MeshApiOptions.DEBUG_LOGGING_ENABLED || shouldDebugLogger.getAsBoolean()) {
            // configure the logger for debug mode
            // see https://logging.apache.org/log4j/2.0/faq.html#reconfig_level_from_code
            Configurator.setLevel(loggerName, MeshApiOptions.DEBUG_LOG_LEVEL);
        }
    }
}
