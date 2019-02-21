/*
 * Mesh
 * Copyright (C) 2019 GlassPane
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
package com.github.glasspane.mesh.impl.serialization;

import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class IdentifierJsonSerializer implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Identifier(json.getAsString());
    }

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(String.valueOf(src.toString()));
    }
}
