package dev.upcraft.mesh.test.util.assertions;

import net.minecraft.test.GameTestException;

import java.util.Objects;

public class Assertions {

    public static void assertTrue(boolean condition, String message) throws GameTestException {
        if(!condition) throw new GameTestException(message);
    }

    public static void assertFalse(boolean condition, String message) throws GameTestException {
        if (condition)
            throw new GameTestException(message);
    }

    public static void assertTrue(boolean condition) throws GameTestException {
        assertTrue(condition, "Assertion failed");
    }

    public static void assertFalse(boolean condition) throws GameTestException {
        assertFalse(condition, "Assertion failed");
    }

    public static void assertEquals(Object o1, Object o2, String message) throws GameTestException {
        assertTrue(Objects.equals(o1, o2), message);
    }

    public static void assertEquals(Object o1, Object o2) throws GameTestException {
        assertTrue(Objects.equals(o1, o2), "Equality assertion failed");
    }
}
