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
package com.github.glasspane.mesh.mixin.common;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.crafting.RecipeCreator;
import com.github.glasspane.mesh.api.crafting.RecipeFactory;
import com.github.glasspane.mesh.impl.crafting.RecipeFactoryImpl;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class is used in a development environment <strong>ONLY</strong>!
 */
@Mixin(RecipeManager.class)
public class MixinRecipeManager {

    @Inject(method = "apply", at = @At(value = "HEAD"))
    private void createMeshRecipes(ResourceManager resourceManager, CallbackInfo ci) {
        Mesh.getDebugLogger().info("Development Environment detected. Creating recipe files...");
        RecipeFactory factory = new RecipeFactoryImpl();
        FabricLoader.INSTANCE.getInitializers(RecipeCreator.class).forEach(creator -> creator.createRecipes(factory));
    }
}
