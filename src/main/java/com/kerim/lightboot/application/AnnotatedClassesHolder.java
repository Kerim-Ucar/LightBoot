package com.kerim.lightboot.application;

import com.kerim.lightboot.annotations.application.Component;
import com.kerim.lightboot.annotations.application.Configuration;
import com.kerim.lightboot.annotations.application.Controller;
import com.kerim.lightboot.annotations.application.Service;
import com.kerim.lightboot.exceptions.NoPathFound;
import com.kerim.lightboot.utility.ClassExtractor;
import com.kerim.lightboot.utility.ClassParser;

import java.io.IOException;

public class AnnotatedClassesHolder implements ApplicationComponent {
    private final String LOGGER_STRING_RETURN = "[AnnotatedClassesHolder]";

    public static final int ANNOTATED_TYPES = 2;

    private String[] allClassPaths;
    private Class<?>[] configurationClasses;
    private Class<?>[] componentClasses;
    private Class<?>[] serviceClasses;
    private Class<?>[] controllerClasses;

    private final ClassParser classParser;
    private final ClassExtractor classExtractor;

    public AnnotatedClassesHolder(
            ClassParser classParser,
            ClassExtractor classExtractor
    ) {
        this.classParser = classParser;
        this.classExtractor = classExtractor;
    }

    public String[] getClassPaths() {
        return allClassPaths;
    }

    public Class<?>[] getConfigurationClasses() {
        return configurationClasses;
    }

    public Class<?>[] getComponentClasses() {
        return componentClasses;
    }

    public Class<?>[] getServiceClasses() {
        return serviceClasses;
    }

    public Class<?>[] getControllerClasses() {
        return controllerClasses;
    }

    @Override
    public String startup() {
        try {
            allClassPaths = this.classParser.getParsedJavaClassPaths();
            configurationClasses = this.classExtractor.getClasses(allClassPaths, Configuration.class);
            componentClasses = this.classExtractor.getClasses(allClassPaths, Component.class, true);
            serviceClasses = this.classExtractor.getClasses(allClassPaths, Service.class);
            controllerClasses = this.classExtractor.getClasses(allClassPaths, Controller.class);

        } catch (IOException | NoPathFound exception) {
            throw new NoPathFound(exception.getMessage());
        }
        return LOGGER_STRING_RETURN;
    }
}
