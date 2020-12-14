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
