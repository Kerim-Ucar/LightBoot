package com.kerim.lightboot.utility;

import com.kerim.lightboot.exceptions.NoPathFound;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ClassParser {

    public ClassParser() {}

    private String[] getJavaPaths() throws IOException {
        Path start = Path.of("src/main/java");

        try (Stream<Path> files = Files.walk(start)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(string -> string.endsWith(".java"))
                    .toArray(String[]::new);
        } catch(IOException | NoPathFound e) {
            throw new NoPathFound("ClassParser.getJavaPaths() failed to find a java path.");
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

    public String[] getParsedJavaClassPaths() throws IOException {
        try{
            String[] javaPaths = getJavaPaths();
            return parseJavaPaths(javaPaths);
        } catch(NoPathFound e) {
            throw new NoPathFound("ClassParser.getPaths() failed due to IO error or due to ClassParser.parseJavaPaths()");
        }
    }

    public void test() throws IOException {
        String[] javaPaths = getJavaPaths();
        String[] packagePaths = parseJavaPaths(javaPaths);

        for(String javaPath : packagePaths) {
            System.out.println(javaPath);
        }
    }


}
