package com.kerim.lightboot.application;

import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.utility.BeanExtractor;

import java.util.ArrayList;
import java.util.Objects;

public class BeanFactory {
    private ArrayList<Header> headers;
    private ArrayList<Object>  objects;
    private BeanExtractor beanExtractor;

    public BeanFactory() {
        headers = new ArrayList<>();
        objects = new ArrayList<>();
    }


}
