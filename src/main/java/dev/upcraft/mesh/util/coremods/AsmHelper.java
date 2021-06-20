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
package dev.upcraft.mesh.util.coremods;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmHelper {

    public static MethodNode findMethod(ClassNode owner, String methodName, String methodDesc) {
        return findMethod(owner, FabricLoader.getInstance().getMappingResolver().unmapClassName(MappingFormats.INTERMEDIARY, owner.name.replace('/', '.')), methodName, methodDesc);
    }

    public static MethodNode findMethod(ClassNode classNode, String methodOwner, String methodName, String methodDesc) {
        MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
        String remappedMethodName = mappings.mapMethodName(MappingFormats.INTERMEDIARY, methodOwner, methodName, methodDesc);
        for (int i = 0; i < classNode.methods.size(); i++) {
            MethodNode node = classNode.methods.get(i);
            if (node.name.equals(remappedMethodName))
                return node;
        }
        throw new IllegalArgumentException(String.format("Method %s does not exist in %s", remappedMethodName, classNode.name));
    }
}
