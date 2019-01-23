package com.github.glasspane.mesh.util.serialization;

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
