package dev.upcraft.mesh.test.main.serialization;

import dev.upcraft.mesh.test.MeshGameTest;
import dev.upcraft.mesh.test.util.assertions.Assertions;
import dev.upcraft.mesh.util.serialization.JsonUtil;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;

public class JsonUtilTest extends Assertions implements MeshGameTest {

    @GameTest(structureName = FabricGameTest.EMPTY_STRUCTURE, batchId = "mesh:serialization")
    @Override
    public void runTest(TestContext ctx) throws GameTestException {
        assertEquals(JsonUtil.GSON.get().toJson(new Identifier("mesh", "test")), "\"mesh:test\"");
    }
}
