package dev.upcraft.mesh.util.serialization;

import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {

    @Test
    void identifiersSerialize() {
        assertEquals("\"mesh:test\"", JsonUtil.GSON.get().toJson(new Identifier("mesh", "test")));
    }

    @Test
    void identifiersDeserialize() {
        assertEquals(new Identifier("hi"), JsonUtil.GSON.get().fromJson("\"minecraft:hi\"", Identifier.class));
    }
}
