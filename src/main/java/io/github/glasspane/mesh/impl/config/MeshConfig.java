package io.github.glasspane.mesh.impl.config;

import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Settings;

@Settings
public class MeshConfig {

    @Setting.Group
    public final Commands commands = new Commands();

    public static class Commands {
        public boolean enableDayNightCommands = true;
    }

}
