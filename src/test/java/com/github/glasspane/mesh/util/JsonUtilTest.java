package com.github.glasspane.mesh.util;

import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {
    @Test
    void identifiersSerialize() {
        assertEquals("\"mesh:test\"", JsonUtil.GSON.toJson(new Identifier("mesh", "test")));
    }

    @Test
    void identifiersDeserialize() {
        assertEquals(new Identifier("hi"), JsonUtil.GSON.fromJson("\"minecraft:hi\"", Identifier.class));
    }
}