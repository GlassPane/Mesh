package io.github.glasspane.mesh.impl.registry;

import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.registry.BlockItemProvider;
import io.github.glasspane.mesh.util.RegistryHelper;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class RegistryProcessor {

    public static void init() {
        RegistryHelper.visitRegistry(Registry.BLOCK, (identifier, block) -> {
            if (block instanceof BlockItemProvider) {
                Item item = ((BlockItemProvider) block).createItem();
                if (item != null) {
                    Mesh.getLogger().trace("registering item: {}", identifier);
                    Registry.register(Registry.ITEM, identifier, item);
                }
            }
        });
    }
}
