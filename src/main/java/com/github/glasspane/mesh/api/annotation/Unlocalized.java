/*
 * Mesh
 * Copyright (C) 2019 GlassPane
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
package com.github.glasspane.mesh.api.annotation;

import net.minecraft.client.resource.language.I18n;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a String should be used as a translation key
 * <p>
 *     Example:
 *     <pre><code>
 *         List&lt;@Unlocalized String&gt lines = obj.getLines();
 *         for (@Unlocalized String line : lines) {
 *             font.drawString(I18n.translate(line), 0, 0, 0xFFFFFF);
 *         }
 *     </code></pre>
 * </p>
 * @see I18n
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface Unlocalized {

}
