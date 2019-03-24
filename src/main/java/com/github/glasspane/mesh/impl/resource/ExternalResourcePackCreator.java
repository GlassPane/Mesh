package com.github.glasspane.mesh.impl.resource;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.util.JsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class ExternalResourcePackCreator implements ResourcePackCreator {

    private static final String PACK_NAME = Mesh.MODID + "_external";

    @Override
    public <T extends ResourcePackContainer> void registerContainer(Map<String, T> map, ResourcePackContainer.Factory<T> factory) {
        File packDir = new File(Mesh.getOutputDir(), "resources");
        if(packDir.isDirectory()) {
            File mcmeta = new File(packDir, "pack.mcmeta");
            if(!mcmeta.exists()) {
                JsonObject meta = new JsonObject();
                JsonObject pack = new JsonObject();
                pack.addProperty("description", "Mesh extra resources");
                pack.addProperty("pack_format", 4);
                meta.add("pack", pack);
                try(Writer writer = new OutputStreamWriter(new FileOutputStream(mcmeta), StandardCharsets.UTF_8)) {
                    JsonUtil.GSON.toJson(meta, writer);
                }
                catch (IOException e) {
                    Mesh.getLogger().error("unable to create external resource meta file", e);
                }
            }
            Optional.ofNullable(ResourcePackContainer.of(PACK_NAME, false, () -> new DirectoryResourcePack(packDir), factory, ResourcePackContainer.SortingDirection.TOP)).ifPresent(t -> map.put(PACK_NAME, t));
        }
    }
}
