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
package io.github.glasspane.mesh.annotations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({MeshAnnotationProcessor.AUTOREGISTRY_REGISTER_CLASS, MeshAnnotationProcessor.AUTOREGISTRY_IGNORE_CLASS})
public class MeshAnnotationProcessor extends AbstractProcessor {

    public static final String AUTOREGISTRY_REGISTER_CLASS = "io.github.glasspane.mesh.api.annotation.AutoRegistry.Register";
    public static final String AUTOREGISTRY_IGNORE_CLASS = "io.github.glasspane.mesh.api.annotation.AutoRegistry.Ignore";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Collecting Mesh annotations");
        TypeElement registerTypeElement = processingEnv.getElementUtils().getTypeElement(AUTOREGISTRY_REGISTER_CLASS);
        TypeMirror registerTypeMirror = registerTypeElement.asType();
        TypeElement ignoreTypeElement = processingEnv.getElementUtils().getTypeElement(AUTOREGISTRY_IGNORE_CLASS);
        TypeMirror ignoreTypeMirror = ignoreTypeElement.asType();
        Collection<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(registerTypeElement);
        JsonObject root = new JsonObject();
        JsonArray registry = new JsonArray();
        String owner = null;
        for (TypeElement clazz : ElementFilter.typesIn(annotated)) {
            for (AnnotationMirror annotationMirror : clazz.getAnnotationMirrors()) {
                if (annotationMirror.getAnnotationType().equals(registerTypeMirror)) {
                    String className = clazz.getQualifiedName().toString();
                    JsonObject obj = new JsonObject();
                    obj.addProperty("owner", className);
                    TypeMirror temp = null;
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
                        ExecutableElement key = entry.getKey();
                        AnnotationValue annValue = entry.getValue();
                        switch (key.getSimpleName().toString()) {
                            case "value":
                                temp = (TypeMirror) annValue.getValue(); // value is class
                                break;
                            case "registry":
                                obj.addProperty("registry", annValue.getValue().toString()); // value is string
                                break;
                            case "modid":
                                String modid = annValue.getValue().toString(); // value is string
                                obj.addProperty("modid", modid);
                                if(owner == null) { // TODO find a better way to determine the owner mod of our output file?
                                    owner = modid;
                                }
                                break;
                            case "modsLoaded":
                                @SuppressWarnings("unchecked") List<? extends AnnotationValue> requiredModsList = (List<? extends AnnotationValue>) annValue.getValue();
                                JsonArray array = new JsonArray();
                                requiredModsList.stream().map(annotationValue -> annotationValue.getValue().toString()).forEach(array::add);
                                if (array.size() > 0) {
                                    obj.add("required_mods", array);
                                }
                                break;
                        }
                    }
                    TypeMirror registrySuperType = Objects.requireNonNull(temp, "no type specified");

                    JsonArray fields = new JsonArray();
                    clazz.getEnclosedElements().stream()
                            .filter(e -> {
                                if (e.getKind().isField() && e.getModifiers().contains(Modifier.PUBLIC) && e.getModifiers().contains(Modifier.STATIC) && e.getModifiers().contains(Modifier.FINAL)) {
                                    for (AnnotationMirror mirror : e.getAnnotationMirrors()) {
                                        if (mirror.getAnnotationType().equals(ignoreTypeMirror)) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                                return false;
                            })
                            .forEach(e -> {
                                TypeMirror fieldType = e.asType();
                                if(processingEnv.getTypeUtils().isAssignable(fieldType, registrySuperType)) {
                                    fields.add(e.getSimpleName().toString());
                                }
                            });
                    obj.add("fields", fields);
                    registry.add(obj);
                    break;
                }
            }
        }
        root.addProperty("id", owner);
        root.add("registry", registry);
        //root.add("data_generator", new JsonArray()); //TODO process datagen annotations
        try {
            FileObject output = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "mesh_annotations.json");
            try (Writer writer = output.openWriter()) {
                GSON.toJson(root, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

