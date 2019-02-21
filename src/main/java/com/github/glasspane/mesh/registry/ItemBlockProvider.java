package com.github.glasspane.mesh.registry;

import net.minecraft.item.Item;

@FunctionalInterface
public interface ItemBlockProvider {

    Item createItem();
}
