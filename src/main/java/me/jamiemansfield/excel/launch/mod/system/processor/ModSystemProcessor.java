/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.excel.launch.mod.system.processor;

import static javax.tools.StandardLocation.CLASS_OUTPUT;

import me.jamiemansfield.excel.SharedConstants;
import me.jamiemansfield.excel.launch.mod.system.ModSystem;
import me.jamiemansfield.excel.util.ap.AnnotationProcessor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * An annotation processor designed to produce <code>mod-system.properties</code>
 * files for ExcelLoader mod systems.
 *
 * Should a mod developer choose to produce the mod system descriptor themselves,
 * the annotation processor will acknowledge that, and not recreate the file.
 */
@SupportedAnnotationTypes("me.jamiemansfield.excel.launch.mod.system.ModSystem")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModSystemProcessor extends AnnotationProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        // Lets not waste time if there aren't any mod systems to process
        if (this.contains(annotations, ModSystem.class)) return false;

        // Check if a mod system descriptor already exists
        // TODO: implement

        // Process the mod systems
        for (final Element annotatedElement : roundEnv.getElementsAnnotatedWith(ModSystem.class)) {
            // First, check that the element is a class
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                this.error("Only classes can be annotated with @ModSystem", annotatedElement);
                return false; // There should only be one mod system per jar!
            }

            // Lets grab that annotation
            final TypeElement pluginElement = (TypeElement) annotatedElement;
            final ModSystem modSystem = pluginElement.getAnnotation(ModSystem.class);

            // Check the mod system id
            if (modSystem.value().isEmpty()) {
                this.error("The mod identifier cannot be empty!", annotatedElement);
                return false; // There should only be one mod system per jar!
            }

            // We're good to go
            final Properties properties = new Properties();
            properties.setProperty("id", modSystem.value());
            properties.setProperty("main-class", pluginElement.getQualifiedName().toString());
            try (final BufferedWriter writer =
                    new BufferedWriter(this.processingEnv.getFiler().createResource(CLASS_OUTPUT, "", "mod-system.properties").openWriter())) {
                properties.store(writer, "Generated by ExcelLoader v" + SharedConstants.VERSION);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
            return false; // There should only be one mod system per jar!
        }

        return false;
    }

}