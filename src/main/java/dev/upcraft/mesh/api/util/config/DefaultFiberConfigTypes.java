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
package dev.upcraft.mesh.api.util.config;

import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.StringConfigType;
import me.shedaniel.fiber2cloth.api.DefaultTypes;
import net.minecraft.util.Identifier;

public final class DefaultFiberConfigTypes {

    /**
     * a copy of fiber2cloth's {@link DefaultTypes#IDENTIFIER_TYPE}<br/>
     * we need this because fiber2cloth is <strong>client-only</strong> and therefore using theirs crashes servers on startup.
     */
    public static final StringConfigType<Identifier> IDENTIFIER = ConfigTypes.STRING.withPattern("(?>[a-z0-9_.-]+:)?[a-z0-9/._-]+").derive(Identifier.class, Identifier::new, Identifier::toString);
}
