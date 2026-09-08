package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.application.headers.Header;

public interface HeaderBeanPairFactory {
    <T> HeaderBeanPair createHeaderBeanPair(Class<?> beanClass, T bean);
    <T> HeaderBeanPair createHeaderBeanPair(Header header, T bean);
    <T> HeaderBeanPair createHeaderBeanPair(Class<?> beanClass, String name, T bean);
}
