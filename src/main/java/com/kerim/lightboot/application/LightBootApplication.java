package com.kerim.lightboot.application;

import com.kerim.lightboot.application.beans.ApplicationContextHandler;
import com.kerim.lightboot.application.beans.ContextHandler;
import com.kerim.lightboot.application.beans.HeaderBeanPairFactory;
import com.kerim.lightboot.application.beans.HeaderBeanPairFactoryImpl;
import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.application.context.Context;
import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.application.headers.HeaderFactory;
import com.kerim.lightboot.application.headers.SimpleHeaderFactory;
import com.kerim.lightboot.utility.AutoInjectExtractor;
import com.kerim.lightboot.utility.BeanExtractor;
import com.kerim.lightboot.utility.ClassExtractor;
import com.kerim.lightboot.utility.ClassParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LightBootApplication {
    private static LightBootApplication application;

    private static final ApplicationConfiguration DEFAULT_CONFIGURATION = LightBootApplication::createDefault;

    private final Logger logger = Logger.getLogger(LightBootApplication.class.getName());

    private final ApplicationContext applicationContext;
    private final ContextHandler contextHandler;

    private final ClassParser classParser;
    private final BeanExtractor beanExtractor;
    private final ClassExtractor classExtractor;
    private final AutoInjectExtractor autoInjectExtractor;
    private final HeaderFactory headerFactory;
    private final HeaderBeanPairFactory headerBeanPairFactory;
    private final AnnotatedClassesHolder annotatedClassesHolder;
    private final ArrayList<ApplicationComponent> applicationComponents;

    private LightBootApplication(
            ClassParser classParser,
            ClassExtractor classExtractor,
            BeanExtractor beanExtractor,
            AutoInjectExtractor autoInjectExtractor,
            HeaderFactory headerFactory,
            HeaderBeanPairFactory headerBeanPairFactory,
            AnnotatedClassesHolder annotatedClassesHolder,
            ApplicationContext applicationContext,
            ContextHandler contextHandler,
            ArrayList<ApplicationComponent> applicationComponents
    ) {
        this.classParser = classParser;
        this.classExtractor = classExtractor;
        this.beanExtractor = beanExtractor;
        this.autoInjectExtractor = autoInjectExtractor;
        this.headerFactory = headerFactory;
        this.headerBeanPairFactory = headerBeanPairFactory;
        this.annotatedClassesHolder = annotatedClassesHolder;
        this.applicationContext = applicationContext;
        this.contextHandler = contextHandler;
        this.applicationComponents = applicationComponents;
    }

    public static LightBootApplication createDefault() {
        ClassParser classParser = new ClassParser();
        ClassExtractor classExtractor = new ClassExtractor();
        BeanExtractor beanExtractor = new BeanExtractor();
        AutoInjectExtractor autoInjectExtractor = new AutoInjectExtractor();
        HeaderFactory headerFactory = new SimpleHeaderFactory();
        HeaderBeanPairFactory headerBeanPairFactory = new HeaderBeanPairFactoryImpl(headerFactory);
        ApplicationContext applicationContext = new ApplicationContext();
        AnnotatedClassesHolder annotatedClassesHolder = new AnnotatedClassesHolder(classParser, classExtractor);
        ApplicationContextHandler contextHandler = new ApplicationContextHandler(
                headerBeanPairFactory,
                beanExtractor,
                annotatedClassesHolder,
                applicationContext
        );

        return new LightBootApplication(
                classParser,
                classExtractor,
                beanExtractor,
                autoInjectExtractor,
                headerFactory,
                headerBeanPairFactory,
                annotatedClassesHolder,
                applicationContext,
                contextHandler,
                new ArrayList<>()
        );
    }

    public static LightBootApplication getInstance() {
        return getInstance(DEFAULT_CONFIGURATION);
    }

    public static LightBootApplication getInstance(ApplicationConfiguration applicationConfiguration) {
        if (application == null) {
            application = applicationConfiguration.configuration();
        }
        return application;
    }

    public static void run() {
        run(null);
    }

    public static void run(ApplicationConfiguration applicationConfiguration) {
        LightBootApplication instance = applicationConfiguration == null
                ? getInstance()
                : getInstance(applicationConfiguration);
        instance.start();
    }

    private void start() {
        registerComponent(classParser);
        registerComponent(contextHandler);
        injectBeans();
    }

    private void injectBeans() {
        injectBeans(annotatedClassesHolder.getServiceClasses());
    }

    private void injectBeans(Class<?>[] classes) {
        Map<Class<?>, Object> serviceInstances = new HashMap<>();

        for (Class<?> clazz : classes) {
            if (applicationContext.isRegistered(clazz)) {
                continue;
            }

            Object host = autoInjectExtractor.createServiceInstance(clazz, applicationContext);
            serviceInstances.put(clazz, host);
            applicationContext.register(headerFactory.createHeader(clazz), host);
        }

        for (Class<?> clazz : classes) {
            Object host = serviceInstances.get(clazz);
            if (host == null) {
                continue;
            }

            autoInjectExtractor.injectAutoInjectFields(host, clazz, applicationContext);
        }
    }

    public Context getContext() {
        return applicationContext;
    }

    public ContextHandler getContextHandler() {
        return contextHandler;
    }

    private void registerComponent(ApplicationComponent component) {
        component.startup();
        applicationComponents.add(component);
    }


}
