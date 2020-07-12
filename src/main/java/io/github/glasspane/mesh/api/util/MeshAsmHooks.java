/*
 * Mesh
 * Copyright (C) 2019-2020 GlassPane
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
package io.github.glasspane.mesh.api.util;

import io.github.glasspane.mesh.impl.asm.AsmHooksImpl;
import com.google.common.annotations.Beta;

/**
 * utility class for accessing internal ASM injections
 */
@Beta
public interface MeshAsmHooks {

    static MeshAsmHooks getInstance() {
        return AsmHooksImpl.INSTANCE;
    }
}
