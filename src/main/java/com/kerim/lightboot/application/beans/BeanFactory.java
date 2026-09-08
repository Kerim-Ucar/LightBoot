package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.application.headers.Header;

public interface BeanFactory {
    <T> HeaderBeanPair createHeaderBeanPair(Class<?> beanClass, T bean);
    <T> HeaderBeanPair createHeaderBeanPair(Header header, T bean);
}
