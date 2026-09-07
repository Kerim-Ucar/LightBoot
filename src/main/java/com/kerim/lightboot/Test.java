package com.kerim.lightboot;

import com.kerim.lightboot.annotations.Bean;

public class Test {
    String s;

    public Test(String s)  {
        this.s = s;
    }

    public Test() {

    }

    public String getS() {
        return s;
    }

}
