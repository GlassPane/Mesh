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

import com.google.common.collect.ImmutableMap;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.upcraft.mesh.impl.config.MeshConfig;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.annotation.CalledByReflection;
import dev.upcraft.mesh.api.util.config.ConfigHandler;
import me.shedaniel.fiber2cloth.api.Fiber2Cloth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;

import java.util.Map;

@CalledByReflection
@Environment(EnvType.CLIENT)
public class MeshModmenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> Fiber2Cloth.create(parent, Mesh.MODID, ConfigHandler.getConfigBranch(MeshConfig.class), new TranslatableText("config.mesh.title")).setSaveRunnable(() -> ConfigHandler.saveConfig(MeshConfig.class)).build().getScreen();
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        ImmutableMap.Builder<String, ConfigScreenFactory<?>> builder = ImmutableMap.builder();
        ConfigHandler.getRegisteredConfigs().forEach((modid, configClass) -> {
            if (!modid.equals(Mesh.MODID)) {
                builder.put(modid, parent -> Fiber2Cloth.create(parent, modid, ConfigHandler.getConfigBranch(configClass), new TranslatableText(String.format("config.%s.title", modid))).setSaveRunnable(() -> ConfigHandler.saveConfig(configClass)).build().getScreen());
            }
        });
        return builder.build();
    }
}
