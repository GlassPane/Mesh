# Mesh
Minecraft Modding library

[![Build Status](https://img.shields.io/github/workflow/status/GlassPane/Mesh/Build%20Status?label=Build%20Status&logo=GitHub&style=flat-square)](https://github.com/GlassPane/Mesh/actions?query=workflow%3A%22Build+Status%22 "GitHub Actions") [![Latest Release](https://img.shields.io/github/v/release/GlassPane/Mesh?include_prereleases&label=Latest%20Release&logo=GitHub&style=flat-square)](https://github.com/GlassPane/Mesh/releases/latest "GitHub Releases") [![JitPack](https://jitpack.io/v/GlassPane/Mesh.svg?label=Download%20from%20JitPack&style=flat-square)](https://jitpack.io/#GlassPane/Mesh "Jitpack Build Status")

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

