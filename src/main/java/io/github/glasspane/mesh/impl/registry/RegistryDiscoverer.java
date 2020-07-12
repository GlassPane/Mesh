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

//FIXME cleanup old code
public class RegistryDiscoverer {

    private static final RegistryKey<Registry<Registry<?>>> ROOT_REGISTRY = RegistryKey.ofRegistry(new Identifier("registries"));

    public static void register() {
        Mesh.getLogger().debug("discovering registry entries...");
        ListMultimap<RegistryKey<? extends Registry<?>>, MeshModInfo.RegisterInfo> toRegister = ArrayListMultimap.create();
        ModInfoParser.getModInfo().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).flatMap(meshModInfo -> Arrays.stream(meshModInfo.getRegisterData()))
                .forEach(registerInfo -> toRegister.put(registerInfo.getRegistry(), registerInfo));

        Mesh.getLogger().debug("adding new registries...");
        registerEntries(Registry.REGISTRIES, toRegister.get(ROOT_REGISTRY));

        Mesh.getLogger().debug("adding new registry entries...");
        //TODO special case blocks and items first
        for (RegistryKey<? extends Registry<?>> registryKey : toRegister.keySet()) {
            Registry<?> registry = Registry.REGISTRIES.get(registryKey.getValue());
            List<MeshModInfo.RegisterInfo> infos = toRegister.get(registryKey);
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
                            Registry.register((Registry<? super T>) registry, name, (T) f.get(null));
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




//            try {
//                return Class.forName(s);
//            } catch (ClassNotFoundException e) {
//                Mesh.getLogger().error("unable to load registry class", e);
//                return null;
//            }
//        }).filter(Objects::nonNull).forEachOrdered(clazz -> {
//            AutoRegistry.Register ann = clazz.getAnnotation(AutoRegistry.Register.class);
//            if (ann != null) {
//
//
//            } else {
//                Mesh.getLogger().error("ignoring class {}: no AutoRegistry annotation present!", clazz::getCanonicalName);
//            }
//        });
//
//
//        Map<Identifier, Map<Class<?>, Pair<String, Class<?>>>> toRegister = new TreeMap<>();
//        Map<Class<?>, Pair<String, Class<?>>> newRegistries = new TreeMap<>(Comparator.comparing(Class::getCanonicalName, String::compareTo));
//        AtomicInteger counter = new AtomicInteger(0);
//        ModInfoParser.getModInfo().values().stream().map(MeshModInfo::getRegisterData).flatMap(Set::stream).sorted().map(s -> {
//            try {
//                return Class.forName(s);
//            } catch (ClassNotFoundException e) {
//                Mesh.getLogger().error("unable to load registry class", e);
//                return null;
//            }
//        }).filter(Objects::nonNull).forEachOrdered(clazz -> {
//            AutoRegistry.Register ann = clazz.getAnnotation(AutoRegistry.Register.class);
//            if (ann != null) {
//                if (Arrays.stream(ann.modsLoaded()).allMatch(FabricLoader.getInstance()::isModLoaded)) {
//                    String modid = ann.modid();
//                    Class<?> type = ann.value();
//                    Identifier registryName = new Identifier(ann.registry());
//                    if (RegistryHelper.ID_REGISTRIES.equals(registryName)) {
//                        newRegistries.put(clazz, new Pair<>(modid, type));
//                    } else {
//                        toRegister.computeIfAbsent(registryName, k -> new TreeMap<>(Comparator.comparing(Class::getCanonicalName, String::compareTo))).put(clazz, new Pair<>(modid, type));
//                    }
//                } else {
//                    Mesh.getLogger().debug("ignoring class {} for registration", clazz::getCanonicalName);
//                    Mesh.getLogger().trace("Missing one or more loaded mods: {}", () -> Arrays.toString(ann.modsLoaded()));
//                }
//            } else {
//                Mesh.getLogger().error("ignoring class {}: no AutoRegistry annotation present!", clazz::getCanonicalName);
//            }
//        });
//        AtomicInteger registryCount = new AtomicInteger(Registry.REGISTRIES.getIds().size());
//        //register registries first
//        if (!newRegistries.isEmpty()) {
//            //registerRegistries(newRegistries);
//            registerEntries(RegistryHelper.ID_REGISTRIES, newRegistries, counter);
//            registryCount.incrementAndGet();
//        }
//        Mesh.getLogger().debug("registering objects...");
//        //special treatment for the item and block registry
//        for (Identifier registryName : new Identifier[]{new Identifier("items"), new Identifier("blocks")}) {
//            Optional.ofNullable(toRegister.remove(registryName)).ifPresent(map -> RegistryDiscoverer.registerEntries(registryName, map, counter));
//        }
//        toRegister.forEach((registryName, entries) -> registerEntries(registryName, entries, counter));
//        Mesh.getLogger().debug("registered {} objects for {} registries", counter::get, registryCount::get);
//    }

//    @SuppressWarnings({"unchecked", "rawtypes"})
//    private static void registerEntries(Identifier registryName, Map<Class<?>, Pair<String, Class<?>>> entries, AtomicInteger counter) {
//        MutableRegistry registry = RegistryHelper.ID_REGISTRIES.equals(registryName) ? (MutableRegistry) Registry.REGISTRIES : (MutableRegistry) Registry.REGISTRIES.get(registryName);
//        if (registry != null) {
//            entries.forEach((clazz, pair) -> {
//                String modid = pair.getLeft();
//                Class<?> type = pair.getRight();
//                for (Field f : clazz.getDeclaredFields()) {
//                    int modField = f.getModifiers();
//                    if (Modifier.isStatic(modField) && Modifier.isPublic(modField) && Modifier.isFinal(modField) && f.getAnnotation(AutoRegistry.Ignore.class) == null) {
//                        try {
//                            Optional.ofNullable(f.get(null)).filter((o) -> type.isAssignableFrom(o.getClass())).ifPresent(value -> {
//                                Identifier name = new Identifier(modid, f.getName().toLowerCase(Locale.ROOT));
//                                Mesh.getLogger().trace("registering {}: {}", registryName.getPath(), name);
//                                Registry.register(registry, name, type.cast(value));
//                                counter.incrementAndGet();
//                                if (registry == Registry.BLOCK) {
//                                    Item item = value instanceof ItemBlockProvider ? ((ItemBlockProvider) value).createItem() : new BlockItem((Block) value, new Item.Settings());
//                                    if (item != null) {
//                                        Mesh.getLogger().trace("registering item: {}", name);
//                                        Registry.register(Registry.ITEM, name, item);
//                                        counter.incrementAndGet();
//                                    }
//                                    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
//                                        ClientRegistryProcessor.processBlock((Block) value);
//                                    }
//                                }
//                            });
//                        } catch (IllegalAccessException e) {
//                            Mesh.getLogger().debug("unable to register entry {}: {}", () -> new Identifier(modid, f.getName().toLowerCase(Locale.ROOT)), e::getMessage);
//                        }
//                    }
//                }
//            });
//        } else {
//            Mesh.getLogger().warn("ignoring non-existent registry {} for classes {}", () -> registryName, () -> Arrays.toString(entries.keySet().stream().map(Class::getSimpleName).toArray(String[]::new)));
//        }
//    }
}
