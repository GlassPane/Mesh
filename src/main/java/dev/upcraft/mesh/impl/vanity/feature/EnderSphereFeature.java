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
package dev.upcraft.mesh.impl.vanity.feature;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.upcraft.mesh.api.util.vanity.VanityConfig;
import dev.upcraft.mesh.api.util.vanity.VanityFeature;

import java.util.UUID;

public class EnderSphereFeature extends VanityFeature<EnderSphereFeature.Config> {

    public EnderSphereFeature() {
        super(Config::new);
    }

    @Override
    public void readFeatureConfiguration(JsonObject json) {

    }

    public static class Config extends VanityConfig<JsonObject> {

        protected Config(UUID uuid) {
            super(uuid);
        }

        @Override
        public boolean isEnabled() {
            // FIXME enable one fully implemented
            /*if (MeshClient.CREATOR_UUID.equals(this.getId())) {
                // TODO temp fix until config exists on remote server
                return true;
            }*/
            return super.isEnabled();
        }

        @Override
        protected void deserializeConfig(JsonObject json) throws JsonParseException {

        }
    }
}
