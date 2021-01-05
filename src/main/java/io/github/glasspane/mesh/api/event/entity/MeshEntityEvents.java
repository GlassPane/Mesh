/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
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
package io.github.glasspane.mesh.api.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class MeshEntityEvents {

    public static final Event<LivingEntityDeath> LIVING_ENTITY_DEATH = EventFactory.createArrayBacked(LivingEntityDeath.class, callbacks -> (entity, reason, amount) -> {
        for (LivingEntityDeath callback : callbacks) {
            callback.consume(entity, reason, amount);
        }
    });

    @FunctionalInterface
    public interface LivingEntityDeath {

        void consume(LivingEntity entity, DamageSource reason, float amount);
    }

}
