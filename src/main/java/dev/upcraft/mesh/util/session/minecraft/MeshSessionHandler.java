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
package dev.upcraft.mesh.util.session.minecraft;

import dev.upcraft.mesh.api.MeshApiOptions;
import net.minecraft.client.util.Session;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class MeshSessionHandler {

    public static Optional<Session> tryLoadSession(Session previous) {
        if(MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT) {
            String username = System.getProperty("mesh.minecraft.session.username");
            if(username != null) {
                String uuid = PlayerEntity.getOfflinePlayerUuid(username).toString();
                String idString = System.getProperty("mesh.minecraft.session.uuid");
                if(idString != null) {
                    uuid = idString;
                }
                return Optional.of(new Session(username, uuid, previous.getAccessToken(), previous.getXuid(), previous.getClientId(), previous.getAccountType()));
            }
        }
        return Optional.empty();
    }
}
