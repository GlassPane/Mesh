package io.github.glasspane.mesh.util.player;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

//TODO better text
public class PlayerManagerHelper {

    public static void forceDisconnect(ServerPlayerEntity player, String modid, String reason) {
        String modname = FabricLoader.getInstance().getModContainer(modid).map(ModContainer::getMetadata).map(ModMetadata::getName).orElse(modid);
        player.networkHandler.disconnect(new LiteralText(String.format("%s forced a disconnect: %s", modname, reason)));
    }

    public static void forceDisconnect(ServerPlayerEntity player, String modid, int errorCode) {
        forceDisconnect(player, modid, String.format("error code %s", errorCode));
    }
}
