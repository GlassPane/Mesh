package com.github.glasspane.mesh;

import com.github.glasspane.mesh.util.CalledByReflection;
import com.github.glasspane.mesh.util.logging.PrefixMessageFactory;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.*;

@CalledByReflection
public class Mesh implements ModInitializer {

    public static final String MODID = "mesh";
    public static final String MOD_NAME = "Mesh";
    public static final String VERSION = "${version}";

    private static final Logger log = LogManager.getLogger(MODID, new PrefixMessageFactory(MOD_NAME));
    private static final Logger debugLog = LogManager.getLogger(MODID + "-debug", new PrefixMessageFactory(MOD_NAME + " Debug"));

    public static Logger getLogger() {
        return log;
    }

    public static Logger getDebugLogger() {
        return debugLog;
    }

    @Override
    public void onInitialize() {
        log.info("Send Reinforcements!");
    }
}
