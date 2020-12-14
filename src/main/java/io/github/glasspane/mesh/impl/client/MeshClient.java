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
package io.github.glasspane.mesh.impl.client;

import com.mojang.util.UUIDTypeAdapter;
import io.github.glasspane.mesh.api.MeshApiOptions;
import io.github.glasspane.mesh.api.annotation.CalledByReflection;
import io.github.glasspane.mesh.api.client.event.PlayerFeatureRendererCallback;
import io.github.glasspane.mesh.api.util.vanity.VanityManager;
import io.github.glasspane.mesh.impl.client.registry.ClientRegistryProcessor;
import io.github.glasspane.mesh.impl.client.render.EnderCapeFeatureRenderer;
import io.github.glasspane.mesh.impl.client.render.TitleFeatureRenderer;
import io.github.glasspane.mesh.util.collections.CollectionHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.Validate;

import java.util.UUID;

@Environment(EnvType.CLIENT)
@CalledByReflection
public class MeshClient implements ClientModInitializer {

    public static final UUID CREATOR_UUID = UUIDTypeAdapter.fromString("d5034857-9e8a-44cb-a6da-931caff5b838"); //TODO remove

    private static UUID currentPlayerUUID;
    private static boolean creator;

    public static boolean isCreator() {
        return creator;
    }

    public static UUID getCurrentPlayerUUID() {
        return currentPlayerUUID;
    }

    @Override
    public void onInitializeClient() {
        currentPlayerUUID = MinecraftClient.getInstance().getSession().getProfile().getId();
        Validate.notNull(currentPlayerUUID, "current player has no UUID! this is a serious error!");
        if (MeshApiOptions.VANITY_FEATURES_ENABLED) {
            if (creator = CREATOR_UUID.equals(currentPlayerUUID)) {
                String name = CollectionHelper.getRandomElement("Creator", "Dave", "Sir", "Kami-sama", "there");
                VanityManager.getLogger().warn("Hello {}!", name);
            }

            PlayerFeatureRendererCallback.EVENT.register((context, renderDispatcher, slimModel, featureAdder) -> {
                featureAdder.accept(new TitleFeatureRenderer(context));
                featureAdder.accept(new EnderCapeFeatureRenderer(context));
            });
        }
        ClientRegistryProcessor.init();
    }
}
