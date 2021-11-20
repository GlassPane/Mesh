package dev.upcraft.mesh.test.main.baseline;

import dev.upcraft.mesh.test.MeshGameTest;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;

public class AlwaysTrueTest implements MeshGameTest {

    @GameTest(structureName = FabricGameTest.EMPTY_STRUCTURE, batchId = "mesh:baseline")
    @Override
    public void runTest(TestContext ctx) throws GameTestException {
        ctx.complete();
    }
}
