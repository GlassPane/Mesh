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
package io.github.glasspane.mesh.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * @deprecated will be removed in a future update, however currently there is no replacement
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public interface PlayerFeatureRendererCallback {

    Event<PlayerFeatureRendererCallback> EVENT = EventFactory.createArrayBacked(PlayerFeatureRendererCallback.class, callbacks -> (context, renderDispatcher, slimModel, featureAdder) -> {
        for (PlayerFeatureRendererCallback callback : callbacks) {
            callback.accept(context, renderDispatcher, slimModel, featureAdder);
        }
    });

    void accept(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityRenderDispatcher renderDispatcher, boolean slimModel, Consumer<FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>> featureAdder);
}
