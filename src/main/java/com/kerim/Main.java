package com.kerim;

import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.utility.BeanExtractor;
import com.kerim.lightboot.utility.ClassExtractor;
import com.kerim.lightboot.utility.ClassParser;

import java.io.IOException;

public class Main {
    static void main(String[] args) throws IOException {
        ApplicationContext context = new ApplicationContext();

        context.test();
    }
}
