package com.github.glasspane.mesh.util.serialization;

import com.github.glasspane.mesh.Mesh;
import com.google.gson.*;
import net.minecraft.util.Identifier;

public class JsonUtil {

    public static final Gson GSON;
    static {
        GsonBuilder builder = new GsonBuilder();
        if(Mesh.isDebugMode()) builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        builder.serializeNulls();
        builder.registerTypeAdapter(Identifier.class, new IdentifierJsonSerializer());
        //TODO register type adapters here
        GSON = builder.create();
    }
}
