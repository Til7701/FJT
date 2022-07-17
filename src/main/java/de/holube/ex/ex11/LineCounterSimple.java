package de.holube.ex.ex11;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public class LineCounterSimple {

    private LineCounterSimple() {
        // no instances allowed
    }

    public static long countOccurrence(String path, String search) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null)
                return 0;
            return Arrays.stream(files)
                    .parallel()
                    .mapToLong(f -> countOccurrence(f.getAbsolutePath(), search))
                    .sum();
        } else {
            return countInFile(path, search);
        }
    }

    private static long countInFile(String path, String search) {
        try (Stream<String> stream = Files.lines(Path.of(path))) {
            return stream
                    //.parallel() // don't do this
                    .filter(line -> line.contains(search))
                    .count();
        } catch (IOException | UncheckedIOException e) {
            return 0;
        }
    }

}
