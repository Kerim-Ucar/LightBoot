package com.kerim;

import com.kerim.lightboot.application.LightBootApplication;
import com.kerim.lightboot.application.context.Context;
import com.kerim.lightboot.application.headers.Header;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        LightBootApplication.run();

        ServerApi serverApi = LightBootApplication.lookUpBean(ServerApi.class);
        Context context = LightBootApplication.getInstance().getContext();

        A a = new A();

        Header header = LightBootApplication.getInstance().getHeaderFactory().createHeader("test bean for thing", A.class);

        context.register(header, a);

        context.printContext();

        A a2 = context.get(header);



        getTime(start);
        getMem();
    }


    public static void getMem() {
        Runtime runtime = Runtime.getRuntime();

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

    public static void getTime(long start) {
        long now = System.currentTimeMillis();
        System.out.println(now - start);
    }

}
