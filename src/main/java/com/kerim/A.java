package com.kerim;

import com.kerim.lightboot.annotations.application.AutoInject;
import com.kerim.lightboot.annotations.application.Service;

@Service
public class A {

    @AutoInject
    private B b;

    public A(){
    }

    public A(B b) {
        this.b = b;
    }
}
