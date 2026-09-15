package com.kerim.lightboot.connectivity.http;

import com.kerim.lightboot.annotations.http.GET;
import com.kerim.lightboot.application.AnnotatedClassesHolder;
import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.application.headers.Header;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HttpGetRouteRegistry {
    private final List<HttpGetRoute> routes = new ArrayList<>();

    public HttpGetRouteRegistry(
            AnnotatedClassesHolder annotatedClassesHolder,
            ApplicationContext applicationContext
    ) {
        registerRoutes(annotatedClassesHolder, applicationContext);
    }

    public List<HttpGetRoute> getRoutes() {
        return List.copyOf(routes);
    }

    private void registerRoutes(
            AnnotatedClassesHolder annotatedClassesHolder,
            ApplicationContext applicationContext
    ) {
        Class<?>[] controllerClasses = annotatedClassesHolder.getControllerClasses();
        if (controllerClasses == null || controllerClasses.length == 0) {
            return;
        }

        Set<String> registeredPaths = new HashSet<>();
        for (Class<?> controllerClass : controllerClasses) {
            if (!applicationContext.isRegistered(controllerClass)) {
                throw new RuntimeException(
                        "Controller is not registered in the application context: " + controllerClass.getName()
                );
            }

            Header header = applicationContext.lookUpHeader(controllerClass);
            Object instance = applicationContext.get(header);
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(GET.class)) {
                    continue;
                }

                if (method.getParameterCount() > 0) {
                    throw new RuntimeException(
                            "GET handler " + controllerClass.getName() + "#" + method.getName()
                                    + " must not take parameters"
                    );
                }

                String path = normalizePath(method.getAnnotation(GET.class).value());
                if (!registeredPaths.add(path)) {
                    throw new RuntimeException("Duplicate GET path registered: " + path);
                }

                routes.add(new HttpGetRoute(path, method, instance));
            }
        }
    }

    private static String normalizePath(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            throw new RuntimeException("GET path must not be empty");
        }

        String path = rawPath.trim();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }
}
