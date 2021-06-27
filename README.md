# Mesh

*Minecraft Modding Library*

[![Build Status](https://img.shields.io/github/workflow/status/GlassPane/Mesh/Build%20Status?label=Build%20Status&logo=GitHub&style=flat-square)](https://github.com/GlassPane/Mesh/actions?query=workflow%3A%22Build+Status%22 "GitHub Actions") [![Latest Release](https://img.shields.io/github/v/release/GlassPane/Mesh?include_prereleases&label=Latest%20Release&logo=GitHub&style=flat-square)](https://github.com/GlassPane/Mesh/releases/latest "GitHub Releases") [![OnyxStudios Maven](https://img.shields.io/maven-metadata/v?label=Download%20from%20OnyxStudios%20Maven&metadataUrl=https%3A%2F%2Fmaven.onyxstudios.dev%2Fdev%2Fupcraft%2FMesh%2Fmaven-metadata.xml&style=flat-square)](https://maven.onyxstudios.dev/dev/upcraft/Mesh "maven.onyxstudios.dev") [![JitPack](https://jitpack.io/v/GlassPane/Mesh.svg?label=Download%20from%20JitPack&style=flat-square)](https://jitpack.io/#GlassPane/Mesh "Jitpack Build Status")

---

<br/>

## Features

-  :globe_with_meridians: automatic loading of environment variables from `.env` files at runtime
- :zap: automatically register anything that has a registry
- :hammer: ​inject recipes at runtime
- :bug: wide variety of [debug features](#Notes)

## Installation

To install Mesh, you need to first add the OnyxStudios maven repository to your buildscript:
```gradle
repositories {
	maven {
		name = "OnyxStudios"
		url = "https://maven.onyxstudios.dev"
	}
}
```

After that, you simply add the api and main artifact as runtime dependencies (define `mesh_version` in your `gradle.properties` file):
*If you want to use the automatic registration feature, you also need the annotation processor jar.*

```gradle
dependencies {

	modRuntime "io.github.GlassPane:Mesh:${project.mesh_version}"
	modApi "io.github.GlassPane:Mesh-API:${project.mesh_version}"
	annotationProcessor "io.github.GlassPane:Mesh-Annotations:${project.mesh_version}" // optional; currently only needed for the automatic registration feature
	
}
```

---

## Notes

- **Debug mode** is enabled by setting the `mesh.debug` system property to `true`. For more fine-tuned control see [MeshApiOptions](src/main/java/io/github/glasspane/mesh/api/MeshApiOptions.java).

- **Development mode** is enabled by setting the `fabric.development` system property to `true` (default for mod workspaces).

---



