package com.kerim.lightboot.connectivity.http;

import java.lang.reflect.Method;

public class HttpGetRoute {
    private final String path;
    private final Method handlerMethod;
    private final Object controllerInstance;

    public HttpGetRoute(String path, Method handlerMethod, Object controllerInstance) {
        this.path = path;
        this.handlerMethod = handlerMethod;
        this.controllerInstance = controllerInstance;
    }

    public String getPath() {
        return path;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public Object invoke() throws ReflectiveOperationException {
        handlerMethod.setAccessible(true);
        return handlerMethod.invoke(controllerInstance);
    }
}
