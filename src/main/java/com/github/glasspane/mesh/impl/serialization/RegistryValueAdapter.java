package com.github.glasspane.mesh.impl.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.Objects;

/**
 * A generic {@link TypeAdapter} for values of a registry
 * @param <T> the type of the registry
 */
public class RegistryValueAdapter<T> extends TypeAdapter<T> {
    private final Registry<T> registry;

    public RegistryValueAdapter(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(Objects.requireNonNull(registry.getId(value)).toString());
    }

    @Override
    public T read(JsonReader in) throws IOException {
        return registry.get(Identifier.create(in.nextString()));
    }
}
