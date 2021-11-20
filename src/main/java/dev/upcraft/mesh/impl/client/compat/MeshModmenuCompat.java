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
package dev.upcraft.mesh.impl.client.compat;

import com.google.common.collect.Maps;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.upcraft.mesh.api.annotation.CalledByReflection;
import dev.upcraft.mesh.api.util.config.ConfigHandler;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Collections;
import java.util.Map;

@CalledByReflection
@Environment(EnvType.CLIENT)
public class MeshModmenuCompat implements ModMenuApi {

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return Collections.unmodifiableMap(Maps.transformValues(ConfigHandler.getRegisteredConfigs(), configClass -> parent -> AutoConfig.getConfigScreen(configClass, parent).get()));
    }
}
