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
package dev.upcraft.mesh.impl.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dev.upcraft.mesh.Mesh;
import dev.upcraft.mesh.api.annotation.AutoRegistry;
import dev.upcraft.mesh.api.util.MeshModInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistryDiscoverer {

    private static final RegistryKey<Registry<Registry<?>>> ROOT_REGISTRY = RegistryKey.ofRegistry(new Identifier("root"));

    public static void register() {
        Mesh.getLogger().debug("discovering registry entries...");

        Mesh.getLogger().debug("adding new registries...");
        // we ignore source type for new registries
        List<MeshModInfo.RegisterInfo> registries = ModInfoParser.getModInfo().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).flatMap(meshModInfo -> Arrays.stream(meshModInfo.getRegisterData())).filter(registerInfo -> ROOT_REGISTRY.equals(registerInfo.getRegistry())).toList();
        registerEntries(Registry.REGISTRIES, registries);

        Mesh.getLogger().debug("adding new registry entries...");
        for (AutoRegistry.SourceType sourceType : AutoRegistry.SourceType.values()) {
            ListMultimap<RegistryKey<? extends Registry<?>>, MeshModInfo.RegisterInfo> toRegister = ArrayListMultimap.create();
            ModInfoParser.getModInfo().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).flatMap(meshModInfo -> Arrays.stream(meshModInfo.getRegisterData())).filter(registerInfo -> !ROOT_REGISTRY.equals(registerInfo.getRegistry())).filter(registerInfo -> registerInfo.getRegistrySource() == sourceType).forEach(registerInfo -> toRegister.put(registerInfo.getRegistry(), registerInfo));

            //TODO special case blocks and items first
            for (RegistryKey<? extends Registry<?>> registryKey : toRegister.keySet()) {
                List<MeshModInfo.RegisterInfo> infos = toRegister.get(registryKey);
                Registry<?> registry = sourceType.getRoot().get(registryKey.getValue());
                if (registry == null) {
                    Mesh.getLogger().error("Unable to register entries: Registry {} does not exist for source provider {} or was not registered properly! (distinct classes requesting registry: {})", registryKey, sourceType.getName(), infos.size());
                }
                registerEntries(registry, infos);
            }
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
            } else {
                Mesh.getLogger().debug("skipping loading of class {} ({}) due to missing dependencies", getSimpleClassName(info.getOwnerClass()), modid);
            }
        }
    }

    private static String getSimpleClassName(String fqcn) {
        String[] split = fqcn.split("\\.");
        return split[split.length - 1];
    }
}
