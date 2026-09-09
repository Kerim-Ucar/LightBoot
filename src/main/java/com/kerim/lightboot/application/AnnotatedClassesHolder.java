package com.kerim.lightboot.application;

import com.kerim.lightboot.annotations.application.Configuration;
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
    private Class<?>[] serviceClasses;

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

    public Class<?>[] getServiceClasses() {
        return serviceClasses;
    }

    @Override
    public String startup() {
        try {
            allClassPaths = this.classParser.getParsedJavaClassPaths();
            configurationClasses = this.classExtractor.getClasses(allClassPaths, Configuration.class);
            serviceClasses = this.classExtractor.getClasses(allClassPaths, Service.class);

        } catch (IOException | NoPathFound exception) {
            throw new NoPathFound(exception.getMessage());
        }
        return LOGGER_STRING_RETURN;
    }
}
