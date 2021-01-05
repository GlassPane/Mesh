/*
 * Mesh
 * Copyright (C) 2019-2021 GlassPane
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package io.github.glasspane.mesh.impl.vanity.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.glasspane.mesh.api.util.vanity.VanityConfig;
import io.github.glasspane.mesh.api.util.vanity.VanityFeature;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TitleFeature extends VanityFeature<TitleFeature.Config> {

    private static final Map<Identifier, Title> titles = new HashMap<>();

    public TitleFeature() {
        super(Config::new);
    }

    @Override
    public void readFeatureConfiguration(JsonObject json) {
        titles.clear();
        for (JsonElement element : JsonHelper.getArray(json, "titles")) {
            JsonObject obj = element.getAsJsonObject();
            Identifier id;
            try {
                id = new Identifier(JsonHelper.getString(obj, "id"));
            } catch (InvalidIdentifierException e) {
                throw new JsonParseException("invalid identifier", e);
            }
            Text text = Text.Serializer.fromJson(obj.get("title"));
            titles.put(id, new Title(id, text));
        }
    }

    public static class Config extends VanityConfig<JsonArray> {

        private final Set<Identifier> availableTitles = new HashSet<>();

        @Nullable
        private Title currentTitle;

        public Config(UUID uuid) {
            super(uuid);
        }

        @Override
        protected void deserializeConfig(JsonArray json) throws JsonParseException {
            this.availableTitles.clear();
            for (JsonElement element : json) {
                try {
                    availableTitles.add(new Identifier(element.getAsString()));
                } catch (InvalidIdentifierException e) {
                    throw new JsonParseException("invalid identifier", e);
                }
            }
            availableTitles.stream().findFirst().ifPresent(this::setCurrentTitle);
        }

        @Nullable
        public Title getCurrentTitle() {
            return currentTitle;
        }

        public void setCurrentTitle(@Nullable Identifier title) {
            if (title == null) {
                this.currentTitle = null;
            } else if (this.availableTitles.contains(title) && TitleFeature.titles.containsKey(title)) {
                this.currentTitle = TitleFeature.titles.get(title);
            }
        }

        public Set<Identifier> getAvailableTitles() {
            return Collections.unmodifiableSet(this.availableTitles);
        }
    }

    public static class Title {

        private final Identifier id;
        private final Text title;

        public Title(Identifier id, String title, Formatting... formatting) {
            this(id, new LiteralText(title).formatted(formatting));
        }

        public Title(Identifier id, Text title) {
            Validate.notNull(id, "ID must not be null!");
            Validate.notNull(title, "title must not be null!");
            this.id = id;
            this.title = title;
        }

        public Title(Identifier id, String title) {
            this(id, new LiteralText(title));
        }

        public Text getTitle() {
            return title;
        }

        public Identifier getId() {
            return id;
        }
    }
}
