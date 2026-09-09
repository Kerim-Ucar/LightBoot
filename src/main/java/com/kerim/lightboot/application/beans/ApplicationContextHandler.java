package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.annotations.application.Bean;
import com.kerim.lightboot.application.AnnotatedClassesHolder;
import com.kerim.lightboot.application.ApplicationComponent;
import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.application.context.Context;
import com.kerim.lightboot.utility.BeanExtractor;

import java.lang.reflect.Method;
import java.util.ArrayList;

/***
 *
 */
public class ApplicationContextHandler implements ContextHandler, ApplicationComponent {
    private final String LOGGER_STRING_RETURN = "[ApplicationContextHandler]";

    private final HeaderBeanPairFactory beanFactory;
    private final BeanExtractor beanExtractor;
    private final AnnotatedClassesHolder annotatedClassesHolder;
    private final ApplicationContext applicationContext;

    public ApplicationContextHandler(HeaderBeanPairFactory beanFactory, BeanExtractor beanExtractor, AnnotatedClassesHolder annotatedClassesHolder, ApplicationContext applicationContext) {
        this.beanFactory = beanFactory;
        this.beanExtractor = beanExtractor;
        this.annotatedClassesHolder = annotatedClassesHolder;
        this.applicationContext = applicationContext;
    }

    //get class from annotatedClassHolder, then use beanextractor to get those beans, then turn those beans into pairs, and then unbox them and add to app context
    @Override
    public void addBeansToContext() {
        annotatedClassesHolder.startup();
        ArrayList<HeaderBeanPair[]> headerBeanPairs = getBeansFromClasses(annotatedClassesHolder.getConfigurationClasses());

        addBeansToContext(headerBeanPairs);
    }

    public void addBeansToContext(ArrayList<HeaderBeanPair[]> headerBeanPairs) {
        for (HeaderBeanPair[] headerBeanPair : headerBeanPairs) {
            for (HeaderBeanPair pair : headerBeanPair) {
                applicationContext.register(pair.header(), pair.bean());
            }
        }
    }

    private ArrayList<HeaderBeanPair[]> getBeansFromClasses(Class<?>[] classes) {
        ArrayList<HeaderBeanPair[]> list = new ArrayList<>();
        for (Class<?> clazz : classes) {
            HeaderBeanPair[] beanPairs = getBeans(clazz);
            list.add(beanPairs);
        }

        return list;
    }

    private HeaderBeanPair[] getBeans(Class<?> beanClass) {
        Method[] methods = beanExtractor.getBeanMethods(beanClass);
        Object[] beans = new Object[methods.length];
        for (int i = 0; i < methods.length; i++) {
            beans[i] = beanExtractor.getBeanObject(methods[i]);
        }
        HeaderBeanPair[] headerBeanPairs = new HeaderBeanPair[beans.length];

        for (int i = 0; i < beans.length; i++) {
            if (methods[i].getAnnotation(Bean.class).value().isEmpty()) {
                headerBeanPairs[i] = beanFactory.createHeaderBeanPair(methods[i].getReturnType(), beans[i]);
            } else {
                headerBeanPairs[i] = beanFactory.createHeaderBeanPair(methods[i].getReturnType(), methods[i].getAnnotation(Bean.class).value(), beans[i]);
            }

        }

        return headerBeanPairs;
    }


    public Context getContext() {
        return applicationContext;
    }

    public AnnotatedClassesHolder getAnnotatedClassesHolder() {
        return annotatedClassesHolder;
    }


    @Override
    public String startup() {
        addBeansToContext();
        return LOGGER_STRING_RETURN;
    }
}
