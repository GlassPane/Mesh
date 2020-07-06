# Mesh
Minecraft Modding library

[![Build Status](https://travis-ci.com/GlassPane/Mesh.svg?branch=master)](https://travis-ci.com/GlassPane/Mesh "Travis Build Status") [![Maven Repository](https://img.shields.io/maven-metadata/v/https/maven.abusedmaster.xyz/com/github/GlassPane/Mesh/maven-metadata.xml.svg)](https://maven.abusedmaster.xyz/com/github/GlassPane/Mesh "NerdHubMC Maven") [![JitPack](https://jitpack.io/v/GlassPane/Mesh.svg)](https://jitpack.io/#GlassPane/Mesh "Jitpack Build Status")

---

## Notes

- Debug mode is enabled by setting the `mesh.debug` system property to `true`.

- Development mode is enabled by setting the `fabric.development` system property to `true`.

---

## Modules

- Crafting: allows for exporting recipes into json format<br/>
    ~*Note: Generated recipes will be in `$RUN_DIR/mesh/recipes`*

- Registry: allows for automatically registering objects via annotations.

- Multiblock: allows for easy management of Multiblock BlockEntities
    ~*Note: Requires `Fabric API` to be loaded*
