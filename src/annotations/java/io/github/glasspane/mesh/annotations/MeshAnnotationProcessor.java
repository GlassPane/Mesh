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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(MeshAnnotationProcessor.AUTOREGISTRY_REGISTER_CLASS)
public class MeshAnnotationProcessor extends AbstractProcessor {

    public static final String AUTOREGISTRY_REGISTER_CLASS = "io.github.glasspane.mesh.api.annotation.AutoRegistry.Register";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Collecting Mesh annotations");
        TypeElement annotationTypeElement = processingEnv.getElementUtils().getTypeElement(AUTOREGISTRY_REGISTER_CLASS);
        Collection<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(annotationTypeElement);
        TypeMirror annotationTypeMirror = annotationTypeElement.asType();
        JsonObject json = new JsonObject();
        for (TypeElement clazz : ElementFilter.typesIn(annotated)) {
            for (AnnotationMirror annotationMirror : clazz.getAnnotationMirrors()) {
                if (annotationMirror.getAnnotationType().equals(annotationTypeMirror)) {
                    String className = clazz.getQualifiedName().toString();
                    JsonObject obj = new JsonObject();
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
                        ExecutableElement key = entry.getKey();
                        AnnotationValue annValue = entry.getValue();
                        switch (key.getSimpleName().toString()) {
                            case "value":
                                obj.addProperty("type", annValue.getValue().toString()); // value is class
                                break;
                            case "registry":
                                obj.addProperty("registry", annValue.getValue().toString()); // value is string
                                break;
                            case "modid":
                                obj.addProperty("modid", annValue.getValue().toString()); // value is string
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
                    json.add(className, obj);
                    break;
                }
            }
        }
        try {
            FileObject output = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "","mesh_annotations.json");
            try (Writer writer = output.openWriter()) {
                GSON.toJson(json, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

