package dev.upcraft.mesh.impl.compat;

import net.fabricmc.loader.api.FabricLoader;

public class MeshModCompat {

    public static final boolean REQUIEM_API_INSTALLED = FabricLoader.getInstance().isModLoaded("requiemapi");
}
