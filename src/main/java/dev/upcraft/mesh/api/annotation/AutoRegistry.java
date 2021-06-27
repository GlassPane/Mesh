/*
 * Mesh
 * Copyright (C) 2019-2021 UpcraftLP
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
package dev.upcraft.mesh.api.annotation;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

import java.lang.annotation.*;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class AutoRegistry {
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Register {

        /**
         * @return the modid for registering the entries
         */
        String modid();

        /**
         * @return the registry name of the desired registry<br/>
         * if the registry does not exist, no fields will be registered
         */
        String registry();

        /**
         * @return the (super-) type of fields to look for
         */
        Class<?> value();

        /**
         * used to make loading conditional<br/>
         * will only register if <strong>ALL</strong> of the specified mod IDs are loaded
         */
        String[] modsLoaded() default {};

        /**
         * a valid {@link SourceType} or {@code null}
         */
        String source() default "registry";

    }

    /**
     * used to ignore a field inside a class annotated with {@link Register}
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ignore {

    }

    public enum SourceType {
        REGISTRY("registry", () -> Registry.REGISTRIES),
        BUILT_IN("builtin", () -> BuiltinRegistries.REGISTRIES);

        private final String name;
        private final Supplier<Registry<? extends Registry<?>>> rootSupplier;

        SourceType(String name, Supplier<Registry<? extends Registry<?>>> rootSupplier) {
            this.name = name;
            this.rootSupplier = rootSupplier;
        }

        public Registry<? extends Registry<?>> getRoot() {
            return this.rootSupplier.get();
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

        public static SourceType fromString(String value) {
            if(value != null) {
                for (SourceType type : values()) {
                    if(type.getName().equals(value)) {
                        return type;
                    }
                }
                throw new NoSuchElementException(value);
            }
            return REGISTRY;
        }
    }
}
