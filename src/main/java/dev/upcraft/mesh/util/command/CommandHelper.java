package dev.upcraft.mesh.util.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

public class CommandHelper {

    private CommandHelper() {
        // NO-OP
    }

    /**
     * creates a copy of the target node, as workaround for https://github.com/Mojang/brigadier/issues/46
     * @param command the command being built
     * @param target the command to be executed instead
     */
    public static <S> LiteralArgumentBuilder<S> createRedirect(LiteralArgumentBuilder<S> command, CommandNode<S> target) {
        // loosely based on https://github.com/VelocityPowered/Velocity/blob/8abc9c80a69158ebae0121fda78b55c865c0abad/proxy/src/main/java/com/velocitypowered/proxy/util/BrigadierUtils.java#L38
        var builder = command.requires(command.getRequirement().and(target.getRequirement())).forward(target.getRedirect(), target.getRedirectModifier(), target.isFork()).executes(target.getCommand());
        target.getChildren().forEach(builder::then);
        return builder;
    }
}
