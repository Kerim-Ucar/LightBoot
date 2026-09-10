package com.kerim;

import com.kerim.lightboot.annotations.application.AutoInject;
import com.kerim.lightboot.annotations.application.Service;

@Service
public class B {

    @AutoInject
    private A a;

    public B(){

    }

    public B(A a) {
        this.a = a;
    }
}
