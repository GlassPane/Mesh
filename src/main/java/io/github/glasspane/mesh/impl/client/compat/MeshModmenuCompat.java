package io.github.glasspane.mesh.impl.client.compat;

import com.google.common.collect.ImmutableMap;
import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.annotation.CalledByReflection;
import io.github.glasspane.mesh.api.util.config.ConfigHandler;
import io.github.glasspane.mesh.impl.config.MeshConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.fiber2cloth.api.Fiber2Cloth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;

import java.util.Map;

@CalledByReflection
@Environment(EnvType.CLIENT)
public class MeshModmenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> Fiber2Cloth
                .create(parent, Mesh.MODID, ConfigHandler.getConfigBranch(MeshConfig.class), new TranslatableText("config.mesh.title"))
                .setSaveRunnable(() -> ConfigHandler.saveConfig(MeshConfig.class))
                .build()
                .getScreen();
    }

    //TODO provide config screen factories for every mod handled
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return ImmutableMap.of();
    }
}
