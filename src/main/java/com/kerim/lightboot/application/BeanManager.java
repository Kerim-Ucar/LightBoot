package com.kerim.lightboot.application;

import com.kerim.lightboot.application.context.Context;
import com.kerim.lightboot.application.headers.HeaderFactory;
import com.kerim.lightboot.utility.BeanExtractor;
import com.kerim.lightboot.utility.ClassExtractor;
import com.kerim.lightboot.utility.ClassParser;

public class BeanManager implements ApplicationComponent {
    private Application application;
    private Context context;
    private final BeanExtractor beanExtractor;
    private AnnotatedClassesHolder annotatedClassesHolder;
    private HeaderFactory headerFactory;

    public BeanManager(
            Application application,
            Context context,
            HeaderFactory headerFactory,
            BeanExtractor beanExtractor,
            AnnotatedClassesHolder annotatedClassesHolder
    ) {
        this.application = application;
        this.context = context;
        this.headerFactory = headerFactory;
        this.beanExtractor = beanExtractor;
        this.annotatedClassesHolder = annotatedClassesHolder;
    }

    public Application getApplication() {
        return application;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void startup() {

    }

}
