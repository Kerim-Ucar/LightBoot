package com.kerim.lightboot.utility;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ClassExtractor {

    public ClassExtractor() {}

    public Class<?> getClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?>[] getClasses(String[] classPaths) {
        Class<?>[] classes = new Class[classPaths.length];

        for (int i = 0; i < classPaths.length; i++) {
            classes[i] = getClass(classPaths[i]);
        }

        return classes;
    }

    public Class<?>[] getClasses(String[] classPaths, Class<? extends Annotation> annotation) {
        return Arrays.stream(classPaths)
                .map(this::getClass)
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .toArray(Class<?>[]::new);
    }

    public void test() {
        String className = "com.kerim.lightboot.utility.ClassExtractor";
        Class<?> claz = getClass(className);
        assert claz != null;
        System.out.println(claz);
    }

    public void failedTest() {
        String className = "Hey man did you sort those 65,536 int entries for our database? Just use Arrays.sort, whats the worst that could happen?";
        Class<?> claz = getClass(className);
        assert claz != null;
        System.out.println(claz);
    }
}
