package com.kerim.lightboot.utility;

import com.kerim.lightboot.annotations.application.AutoInject;
import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.application.headers.Header;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;

public class AutoInjectExtractor {

    public Field[] findAutoInjectFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field[] buf = new Field[fields.length];

        int bufIndex = 0;

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInject.class)) {
                buf[bufIndex++] = field;
            }
        }

        Field[] fieldsWithAutoInject = new Field[bufIndex];

        System.arraycopy(buf, 0, fieldsWithAutoInject, 0, bufIndex);

        return fieldsWithAutoInject;
    }

    public Object createServiceInstance(Class<?> clazz, ApplicationContext applicationContext) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 0) {
            throw new RuntimeException("No constructor found for service: " + clazz.getName());
        }

        Constructor<?> constructor = selectConstructor(constructors, clazz, applicationContext);
        constructor.setAccessible(true);

        try {
            if (constructor.getParameterCount() == 0) {
                return constructor.newInstance();
            }

            Object[] dependencies = resolveConstructorArguments(constructor, applicationContext);
            return constructor.newInstance(dependencies);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create service: " + clazz.getName(), e);
        }
    }

    public void injectAutoInjectFields(Object host, Class<?> clazz, ApplicationContext applicationContext) {
        for (Field field : findAutoInjectFields(clazz)) {
            Header header = resolveHeader(field.getType(), field.getAnnotation(AutoInject.class).value(), applicationContext);
            Object bean = applicationContext.get(header);
            injectVariable(host, field, bean);
        }
    }

    public <T> void injectVariable(Object host, Field field, T bean) {
        Class<?> fieldType = field.getType();
        if (!fieldType.isAssignableFrom(bean.getClass())) {
            throw new IllegalArgumentException(
                    "Bean type " + bean.getClass().getName() + " is not assignable to field type " + fieldType.getName()
            );
        }

        field.setAccessible(true);
        try {
            field.set(host, bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> selectConstructor(
            Constructor<?>[] constructors,
            Class<?> clazz,
            ApplicationContext applicationContext
    ) {
        Constructor<?> autoInjectConstructor = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(AutoInject.class))
                .filter(constructor -> canResolveConstructor(constructor, applicationContext))
                .findFirst()
                .orElse(null);

        if (autoInjectConstructor != null) {
            return autoInjectConstructor;
        }

        if (constructors.length == 1) {
            Constructor<?> constructor = constructors[0];
            if (constructor.getParameterCount() == 0 || canResolveConstructor(constructor, applicationContext)) {
                return constructor;
            }
        }

        Constructor<?> noArgConstructor = Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst()
                .orElse(null);

        if (noArgConstructor != null && findAutoInjectFields(clazz).length > 0) {
            return noArgConstructor;
        }

        return Arrays.stream(constructors)
                .filter(constructor -> canResolveConstructor(constructor, applicationContext))
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("Could not resolve constructor dependencies for service: " + clazz.getName()));
    }

    private boolean canResolveConstructor(Constructor<?> constructor, ApplicationContext applicationContext) {
        for (Parameter parameter : constructor.getParameters()) {
            if (!canResolveParameter(parameter, applicationContext)) {
                return false;
            }
        }
        return true;
    }

    private boolean canResolveParameter(Parameter parameter, ApplicationContext applicationContext) {
        try {
            resolveHeader(parameter.getType(), getInjectName(parameter), applicationContext);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private Object[] resolveConstructorArguments(Constructor<?> constructor, ApplicationContext applicationContext) {
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Header header = resolveHeader(parameter.getType(), getInjectName(parameter), applicationContext);
            dependencies[i] = applicationContext.get(header);
        }

        return dependencies;
    }

    private Header resolveHeader(Class<?> type, String injectName, ApplicationContext applicationContext) {
        if (injectName != null && !injectName.isEmpty()) {
            return applicationContext.lookUpHeader(injectName);
        }
        return applicationContext.lookUpHeader(type.getSimpleName());
    }

    private String getInjectName(Parameter parameter) {
        AutoInject autoInject = parameter.getAnnotation(AutoInject.class);
        if (autoInject != null && !autoInject.value().isEmpty()) {
            return autoInject.value();
        }
        return parameter.getType().getSimpleName();
    }
}
