package com.kerim.lightboot.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ClassHandler {

    public ClassHandler() {}

    private String[] getJavaPaths() throws IOException {
        Path start = Path.of("src/main/java");

        try (Stream<Path> files = Files.walk(start)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(string -> string.endsWith(".java"))
                    .toArray(String[]::new);
        }
    }

    private String[] parseJavaPaths(String[] javaPaths) {
        String[] result = new String[javaPaths.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = javaPaths[i].substring("src/main/java/".length());
            result[i] = result[i].replace(".java", "");
            result[i] = result[i].replace("\\", ".");
        }

        return result;
    }

    public void test() throws IOException {
        String[] javaPaths = getJavaPaths();
        String[] packagePaths = parseJavaPaths(javaPaths);


        for(String javaPath : packagePaths) {
            System.out.println(javaPath);
        }
    }


}
