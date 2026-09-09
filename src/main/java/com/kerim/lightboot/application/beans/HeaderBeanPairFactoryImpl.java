package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.application.headers.HeaderFactory;

public class HeaderBeanPairFactoryImpl implements HeaderBeanPairFactory {
    private final HeaderFactory headerFactory;

    public HeaderBeanPairFactoryImpl(HeaderFactory headerFactory) {
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
    public <T> HeaderBeanPair createHeaderBeanPair(Class<?> beanClass, String name, T bean) {
        return new HeaderBeanPair(
                headerFactory.createHeader(name, beanClass),
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
