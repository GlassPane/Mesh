package io.github.glasspane.mesh.impl.vanity.feature;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.glasspane.mesh.api.util.vanity.VanityConfig;
import io.github.glasspane.mesh.api.util.vanity.VanityFeature;
import io.github.glasspane.mesh.impl.client.MeshClient;

import java.util.UUID;

public class EnderCapeFeature extends VanityFeature<EnderCapeFeature.Config> {

    public EnderCapeFeature() {
        super(Config::new);
    }

    @Override
    public void readFeatureConfiguration(JsonObject json) {

    }

    public static class Config extends VanityConfig<JsonObject> {

        boolean enabled;

        protected Config(UUID uuid) {
            super(uuid);
        }

        public boolean isEnabled() {
            if (MeshClient.CREATOR_UUID.equals(this.getId())) {
                // TODO temp fix until config exists on remote server
                return true;
            }
            return enabled;
        }

        @Override
        protected void deserializeConfig(JsonObject json) throws JsonParseException {
            enabled = json.get("enabled").getAsBoolean();
        }
    }
}
