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
package dev.upcraft.mesh.util.collections;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CollectionHelper {

    @SafeVarargs
    public static <T> T getRandomElement(T... collection) {
        return getRandomElement(null, collection);
    }

    @SafeVarargs
    public static <T> T getRandomElement(@Nullable Random random, T... collection) {
        Validate.notEmpty(collection, "collection must not be empty!");
        return collection[(random != null ? random : ThreadLocalRandom.current()).nextInt(collection.length)];
    }

    public static <T> T getRandomElement(Collection<T> collection) {
        return getRandomElement(collection, null);
    }

    public static <T> T getRandomElement(Collection<T> collection, @Nullable Random random) {
        Validate.notEmpty(collection, "collection must not be empty!");
        List<T> list = collection instanceof List ? (List<T>) collection : new ArrayList<>(collection);
        return list.get((random != null ? random : ThreadLocalRandom.current()).nextInt(list.size()));
    }
}
