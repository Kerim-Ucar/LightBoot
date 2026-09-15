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
import com.kerim.lightboot.connectivity.http.HttpGetRouteRegistry;
import com.kerim.lightboot.connectivity.http.HttpServerComponent;
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
    private int httpServerPort = HttpServerComponent.DEFAULT_PORT;
    private HttpServerComponent httpServerComponent;

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
        try {
            instance.start();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("LightBoot application interrupted", exception);
        }
    }

    public static void run(int httpServerPort) {
        LightBootApplication instance = getInstance();
        instance.httpServerPort = httpServerPort;
        run(null);
    }

    public static void run(ApplicationConfiguration applicationConfiguration, int httpServerPort) {
        LightBootApplication instance = getInstance(applicationConfiguration);
        instance.httpServerPort = httpServerPort;
        run(applicationConfiguration);
    }

    private void start() throws InterruptedException {
        registerComponent(classParser);
        registerComponent(contextHandler);
        injectBeans();
        startHttpServer();
        applicationContext.printContext();
        keepHttpServerRunning();
    }

    private void startHttpServer() {
        HttpGetRouteRegistry routeRegistry = new HttpGetRouteRegistry(
                annotatedClassesHolder,
                applicationContext
        );
        if (routeRegistry.getRoutes().isEmpty()) {
            return;
        }

        httpServerComponent = new HttpServerComponent(routeRegistry, httpServerPort);
        registerComponent(httpServerComponent);
    }

    private void keepHttpServerRunning() throws InterruptedException {
        if (httpServerComponent == null) {
            return;
        }

        httpServerComponent.awaitStartup();
        System.out.println("HTTP server listening on http://localhost:" + httpServerComponent.getPort());
        httpServerComponent.awaitRunning();
    }

    private void injectBeans() {
        injectBeans(annotatedClassesHolder.getComponentClasses());
        injectBeans(annotatedClassesHolder.getServiceClasses());
        injectBeans(annotatedClassesHolder.getControllerClasses());
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

    public HeaderFactory getHeaderFactory() {
        return headerFactory;
    }


    public static <T> T lookUpBean(Class<?> clazz) {
        Header header = LightBootApplication.getInstance().applicationContext.lookUpHeader(clazz);
        return LightBootApplication.getInstance().applicationContext.get(header);
    }

    public static <T> T lookUpBean(String name) {
        Header header = LightBootApplication.getInstance().applicationContext.lookUpHeader(name);
        return LightBootApplication.getInstance().applicationContext.get(header);
    }

    private void registerComponent(ApplicationComponent component) {
        component.startup();
        applicationComponents.add(component);
    }



}
