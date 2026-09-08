package com.kerim;

import com.kerim.lightboot.application.LightBootApplication;

import java.io.IOException;

public class Main {
    static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        LightBootApplication.run();

        getTime(start);
    }

    public static void getMem() {
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

    public static void getTime(long start) {
        long now = System.currentTimeMillis();
        System.out.println(now - start);
    }

}
