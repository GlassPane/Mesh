package io.github.glasspane.mesh.mixin.debug.accessor;

import net.minecraft.tag.RequiredTagList;
import net.minecraft.tag.RequiredTagListRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RequiredTagListRegistry.class)
public interface RequiredTagListRegistryAccessor {

    @Accessor("REQUIRED_TAG_LISTS")
    static Map<Identifier, RequiredTagList<?>> getRequiredTagLists() {
        throw new UnsupportedOperationException("Mixin class not transformed");
    }
}
