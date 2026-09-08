package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.TestBeanObj;
import com.kerim.lightboot.application.AnnotatedClassesHolder;
import com.kerim.lightboot.application.ApplicationComponent;
import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.application.context.Context;
import com.kerim.lightboot.application.headers.SimpleHeaderFactory;
import com.kerim.lightboot.utility.BeanExtractor;
import com.kerim.lightboot.utility.ClassExtractor;
import com.kerim.lightboot.utility.ClassParser;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ApplicationBeanManager implements BeanManager, ApplicationComponent {
    private BeanFactory beanFactory = new BeanFactoryImpl(new SimpleHeaderFactory());
    private BeanExtractor beanExtractor = new BeanExtractor();
    private AnnotatedClassesHolder annotatedClassesHolder = new AnnotatedClassesHolder(new ClassParser(), new ClassExtractor());
    private ApplicationContext applicationContext = new ApplicationContext();


    //get class from annotatedClassHolder, then use beanextractor to get those beans, then turn those beans into pairs, and then unbox them and add to app context
    @Override
    public void addBeansToContext() {
        annotatedClassesHolder.startup();
        ArrayList<HeaderBeanPair[]> headerBeanPairs = getBeansFromClasses(annotatedClassesHolder.getConfigurationClasses());
        ArrayList<HeaderBeanPair[]> headerBeanPairs2 = getBeansFromClasses(annotatedClassesHolder.getServiceClasses());

        addBeansToContext(headerBeanPairs);
        addBeansToContext(headerBeanPairs2);
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
            headerBeanPairs[i] = beanFactory.createHeaderBeanPair(methods[i].getReturnType(), beans[i]);
        }

        return headerBeanPairs;
    }

    public Context getContext() {
        return applicationContext;
    }

    public AnnotatedClassesHolder getAnnotatedClassesHolder() {
        return annotatedClassesHolder;
    }

    public void test() {
        HeaderBeanPair[] beans = getBeans(TestBeanObj.class);
        System.out.println(beans.length == 0);
        for (HeaderBeanPair header : beans) {
            System.out.println(header.toString());
        }
    }

    public void test2() {
        startup();
    }

    @Override
    public void startup() {
        addBeansToContext();
        System.out.println("ApplicationBeanManager startup");
    }
}
