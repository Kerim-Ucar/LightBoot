package com.kerim;

import com.kerim.lightboot.Test;
import com.kerim.lightboot.TestBeanObj;
import com.kerim.lightboot.application.beans.ApplicationBeanManager;
import com.kerim.lightboot.application.context.ApplicationContext;
import com.kerim.lightboot.application.context.Context;
import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.utility.BeanExtractor;
import com.kerim.lightboot.utility.ClassExtractor;
import com.kerim.lightboot.utility.ClassParser;

import java.io.IOException;

public class Main {
    static void main(String[] args) throws IOException {
        ApplicationBeanManager applicationBeanManager = new ApplicationBeanManager();
        applicationBeanManager.test2();
        Context context = applicationBeanManager.getContext();
        Header header = new Header("Test", Test.class);
        Test t = context.get(header);
        System.out.println(t.getS());


        Test item = new Test("Hello");
        System.out.println(item.getS());

        Runtime runtime = Runtime.getRuntime();

        // Run the garbage collector first for more accurate active usage data
        runtime.gc();

        // Calculate memory metrics in bytes
        long totalMemory = runtime.totalMemory(); // Memory currently allocated to the JVM from the OS
        long freeMemory = runtime.freeMemory();   // Free memory within that allocated space
        long usedMemory = totalMemory - freeMemory; // Actual memory being used by your objects
        long maxMemory = runtime.maxMemory();     // Maximum memory configured (-Xmx flag)

        // Convert to Megabytes for readability
        long mb = 1024 * 1024;
        System.out.println("Used Memory: " + (usedMemory / mb) + " MB");
        System.out.println("Free Memory: " + (freeMemory / mb) + " MB");
        System.out.println("Total Memory Allocated: " + (totalMemory / mb) + " MB");
        System.out.println("Max Memory Available: " + (maxMemory / mb) + " MB");

    }

}
