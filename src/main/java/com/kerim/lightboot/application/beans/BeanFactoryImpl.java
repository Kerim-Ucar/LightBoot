package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.application.headers.HeaderFactory;

import java.lang.reflect.Method;

public class BeanFactoryImpl implements BeanFactory {
    private HeaderFactory headerFactory;

    public BeanFactoryImpl(HeaderFactory headerFactory) {
        this.headerFactory = headerFactory;
    }

    @Override
    public <T> HeaderBeanPair createHeaderBeanPair(Class<?> beanClass, T bean) {
        return new HeaderBeanPair(
                headerFactory.createHeader(beanClass),
                bean
        );
    }

    @Override
    public <T> HeaderBeanPair createHeaderBeanPair(Header header, T bean) {
        return new HeaderBeanPair(
                header,
                bean
        );
    }
}
