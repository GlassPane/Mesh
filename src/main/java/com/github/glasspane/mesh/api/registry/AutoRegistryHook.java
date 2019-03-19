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
package com.github.glasspane.mesh.api.registry;

import com.github.glasspane.mesh.api.annotation.AutoRegistry;

/**
 * hook for catching AutoRegistry annotations<br/>
 * <b>all classes implementing this must be annotated with {@link AutoRegistry}!</b>
 * @deprecated will soon be obsolete, only the {@link AutoRegistry} annotation will be required
 */
@Deprecated
public interface AutoRegistryHook { }
