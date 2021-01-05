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
package io.github.glasspane.mesh.api.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * This annotation can be applied to a package, class or field to indicate that
 * the fields in that element are nonnull by default unless there is:
 * <ul>
 * <li>An explicit nullness annotation
 * <li>a default parameter annotation applied to a more tightly nested
 * element.
 * </ul>
 */
@Documented
@NotNull
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface FieldsAreNonnullByDefault {
}
