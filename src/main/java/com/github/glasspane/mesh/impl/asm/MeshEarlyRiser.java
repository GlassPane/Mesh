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
package com.github.glasspane.mesh.impl.asm;

import com.chocohead.mm.api.ClassTinkerers;
import com.github.glasspane.mesh.api.MeshApiOptions;
import com.github.glasspane.mesh.api.annotation.CalledByReflection;
import com.github.glasspane.mesh.api.logging.MeshLoggerFactory;
import com.github.glasspane.mesh.util.asm.AsmHelper;
import com.github.glasspane.mesh.util.reflection.MappingFormats;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;
import java.util.Locale;

@CalledByReflection
public class MeshEarlyRiser implements Runnable {

    private static final String MODID = "mesh";
    private static final Logger logger = MeshLoggerFactory.createPrefixLogger(MODID + "_asm", "Mesh ASM Transformer", () -> MeshApiOptions.FABRIC_DEVELOPMENT_ENVIRONMENT);
    private static final String COPY_CLIPBOARD_NAME_LC = MODID + "_copy_to_clipboard";
    public static final String COPY_CLIPBOARD_NAME = COPY_CLIPBOARD_NAME_LC.toUpperCase(Locale.ROOT);

    @Override
    public void run() {
        //EnvType environment = FabricLoader.getInstance().getEnvironmentType();
        logger.debug("disabling brewing recipe checks", new Object[0]);
        MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
        ClassTinkerers.addTransformation(mappings.mapClassName(MappingFormats.INTERMEDIARY, "net.minecraft.class_1845"), classNode -> { //BrewingRecipeRegistry
            MethodNode registerItemRecipe = AsmHelper.findMethod(classNode, "method_8071", String.format("(L%1$s;L%1$s;L%1$s;)V", "net/minecraft/class_1792"));
            removeInstructions(classNode, mappings, registerItemRecipe, "field_8959"); //ITEM_RECIPES
            MethodNode registerPotionType = AsmHelper.findMethod(classNode, "method_8080", "(Lnet/minecraft/class_1792;)V");
            removeInstructions(classNode, mappings, registerPotionType, "field_8957"); //POTION_TYPES
        });
        logger.debug("adding copy-to-clipboard click event", new Object[0]);
        ClassTinkerers.enumBuilder(mappings.mapClassName(MappingFormats.INTERMEDIARY, "net.minecraft.class_2558$class_2559"), String.class, boolean.class).addEnum(COPY_CLIPBOARD_NAME, COPY_CLIPBOARD_NAME_LC, true).build();
        //logger.trace("doing side-specific ASM: {}", environment::name);
        //if(environment == EnvType.CLIENT) {
        //}
    }

    /**
     * remove all instructions before the specified field access
     */
    private static void removeInstructions(ClassNode classNode, MappingResolver mappings, MethodNode method, String fieldAccessName) {
        String fieldName = mappings.mapFieldName(MappingFormats.INTERMEDIARY, mappings.unmapClassName(MappingFormats.INTERMEDIARY, classNode.name.replace('/', '.')), fieldAccessName, "Ljava/util/List;");
        ListIterator<AbstractInsnNode> iterator = method.instructions.iterator(method.instructions.size());
        int targetIndex = -1;
        int currentIndex;
        while(iterator.hasPrevious()) {
            currentIndex = iterator.previousIndex();
            AbstractInsnNode current = iterator.previous();
            if(targetIndex == -1 && current instanceof FieldInsnNode && current.getOpcode() == Opcodes.GETSTATIC) {
                FieldInsnNode fieldNode = (FieldInsnNode) current;
                if(fieldNode.owner.equals(classNode.name) && fieldNode.name.equals(fieldName) && fieldNode.desc.equals("Ljava/util/List;")) {
                    search:
                    for(int offset = 1; offset < currentIndex; offset++) {
                        AbstractInsnNode insnNode = method.instructions.get(currentIndex - offset);
                        if(insnNode instanceof LabelNode) {
                            targetIndex = currentIndex - offset;
                            logger.trace("included {} additional nodes", offset);
                            break search;
                        }
                    }
                }
            }
            else {
                if(currentIndex < targetIndex) iterator.remove();
            }
        }
        if(targetIndex == -1) throw new IllegalStateException("unable to find injection target!");
        logger.trace("successfully cleaned {}#{}()", classNode.name, method.name);
    }
}
