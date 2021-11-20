package dev.upcraft.mesh.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;

import java.lang.reflect.Method;

public interface MeshGameTest extends FabricGameTest {

    default void beforeTest() {}

    default void afterTest() {}

    void runTest(TestContext ctx) throws GameTestException;

    @Override
    default void invokeTestMethod(TestContext context, Method method) {
        beforeTest();
        FabricGameTest.super.invokeTestMethod(context, method);
        context.addInstantFinalTask(this::afterTest);
    }
}
