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
package io.github.glasspane.mesh.impl.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import io.github.glasspane.mesh.Mesh;
import io.github.glasspane.mesh.api.util.MeshModInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class RegistryDiscoverer {

    private static final RegistryKey<Registry<Registry<?>>> ROOT_REGISTRY = RegistryKey.ofRegistry(new Identifier("root"));

    public static void register() {
        Mesh.getLogger().debug("discovering registry entries...");
        ListMultimap<RegistryKey<? extends Registry<?>>, MeshModInfo.RegisterInfo> toRegister = ArrayListMultimap.create();
        ModInfoParser.getModInfo().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).flatMap(meshModInfo -> Arrays.stream(meshModInfo.getRegisterData()))
                .forEach(registerInfo -> toRegister.put(registerInfo.getRegistry(), registerInfo));
        Mesh.getLogger().debug("adding new registries...");
        List<MeshModInfo.RegisterInfo> specialSnowflakes = toRegister.removeAll(RegistryKey.ofRegistry(new Identifier("registries")));
        if(!specialSnowflakes.isEmpty()) {
            Mesh.getLogger().warn("{} registries are still using the 'minecraft:registries' key, they should be using 'minecraft:root'! Offending mods:\n\t", specialSnowflakes.stream().map(MeshModInfo.RegisterInfo::getOwnerModid).collect(Collectors.joining("\n\t")));
        }
        toRegister.putAll(ROOT_REGISTRY, specialSnowflakes);
        registerEntries(Registry.REGISTRIES, toRegister.removeAll(ROOT_REGISTRY));

        Mesh.getLogger().debug("adding new registry entries...");
        //TODO special case blocks and items first
        for (RegistryKey<? extends Registry<?>> registryKey : toRegister.keySet()) {
            List<MeshModInfo.RegisterInfo> infos = toRegister.get(registryKey);
            Registry<?> registry = Registry.REGISTRIES.get(registryKey.getValue());
            if(registry == null) {
                Mesh.getLogger().error("Unable to register entries: Registry {} does not exist or was not registered properly! (distinct classes requesting registry: {})", registryKey, infos.size());
            }
            registerEntries(registry, infos);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void registerEntries(Registry<?> registry, List<MeshModInfo.RegisterInfo> infos) {
        for (MeshModInfo.RegisterInfo info : infos) {
            String modid = info.getOwnerModid();
            if (info.getRequiredMods().stream().allMatch(FabricLoader.getInstance()::isModLoaded)) {
                try {
                    Class<?> owner = Class.forName(info.getOwnerClass());
                    info.getFieldsToRegister().forEach(fieldName -> {
                        try {
                            Field f = owner.getDeclaredField(fieldName);
                            Identifier name = new Identifier(modid, f.getName().toLowerCase(Locale.ROOT));
                            T value = (T) f.get(null);
                            Registry.register((Registry<? super T>) registry, name, value);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException("mod " + modid + " errored during registration", e);
                        }
                    });
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                Mesh.getLogger().debug("skipping loading of class {} ({}) due to missing dependencies", getSimpleClassName(info.getOwnerClass()), modid);
            }
        }
    }

    private static String getSimpleClassName(String fqcn) {
        String[] split = fqcn.split("\\.");
        return split[split.length - 1];
    }
}
