package dev.upcraft.mesh.impl.compat.requiem;

import ladysnake.requiem.api.v1.possession.Possessable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MeshRequiemCompat {

    @Nullable
    public static UUID getPlayerUUID(LivingEntity entity) {
        if(entity instanceof Possessable possessable && possessable.isBeingPossessed()) {
            PlayerEntity thePlayer = possessable.getPossessor();
            if(thePlayer != null) {
                return thePlayer.getGameProfile().getId();
            }
        }
        return null;
    }
}
