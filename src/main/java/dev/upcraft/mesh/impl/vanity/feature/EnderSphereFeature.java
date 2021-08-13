package dev.upcraft.mesh.impl.vanity.feature;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.upcraft.mesh.api.util.vanity.VanityConfig;
import dev.upcraft.mesh.api.util.vanity.VanityFeature;

import java.util.UUID;

public class EnderSphereFeature extends VanityFeature<EnderSphereFeature.Config> {

    public EnderSphereFeature() {
        super(Config::new);
    }

    @Override
    public void readFeatureConfiguration(JsonObject json) {

    }

    public static class Config extends VanityConfig<JsonObject> {

        protected Config(UUID uuid) {
            super(uuid);
        }

        @Override
        public boolean isEnabled() {
            // FIXME enable one fully implemented
            /*if (MeshClient.CREATOR_UUID.equals(this.getId())) {
                // TODO temp fix until config exists on remote server
                return true;
            }*/
            return super.isEnabled();
        }

        @Override
        protected void deserializeConfig(JsonObject json) throws JsonParseException {

        }
    }
}
