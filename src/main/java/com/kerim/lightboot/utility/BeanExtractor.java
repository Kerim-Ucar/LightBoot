package com.kerim.lightboot.utility;

import com.kerim.lightboot.annotations.application.Bean;

import java.lang.reflect.Method;

public class BeanExtractor {

    public BeanExtractor() {
    }

    public Method[] getBeanMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        Method[] buf = new Method[methods.length];
        int bufIndex = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                buf[bufIndex++] = method;
            }
        }

        Method[] methodsWithBeanAnnotation = new Method[bufIndex];
        System.arraycopy(buf, 0, methodsWithBeanAnnotation, 0, bufIndex);

        return methodsWithBeanAnnotation;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBeanObject(Method method) {
        try {
            Object target = method.getDeclaringClass().getDeclaredConstructor().newInstance();
            return (T) method.invoke(target);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
