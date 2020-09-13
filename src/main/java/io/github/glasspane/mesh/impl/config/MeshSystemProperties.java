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
package io.github.glasspane.mesh.impl.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MeshSystemProperties {

    private static boolean loaded = false;

    /**
     * load system properties from '.env' file in MC root directory
     */
    public static void load() {
        if(loaded) {
            return;
        }
        Path env = Paths.get(".env");
        if (Files.exists(env) && !Files.isDirectory(env)) {
            System.out.println("[Mesh Preload] Found .env file, loading properties!");
            try (Reader reader = Files.newBufferedReader(env, StandardCharsets.UTF_8)) {
                Properties props = new Properties();
                props.load(reader);
                props.forEach((key, value) -> {
                    if (key instanceof String && value instanceof String) {
                        System.setProperty(String.valueOf(key), String.valueOf(value));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loaded = true;
    }
}
