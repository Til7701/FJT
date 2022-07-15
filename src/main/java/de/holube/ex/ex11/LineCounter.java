package de.holube.ex.ex11;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Stream;

public class LineCounter {

    public static long countOccurrence(String path, String search, Set<Charset> charsets, boolean parallelLines, boolean printErrors) {
        return new LineCounter(search, charsets, parallelLines, printErrors).countOccurrence(path);
    }

    public static long countOccurrence(String path, String search, Set<Charset> charsets, boolean parallelLines) {
        return countOccurrence(path, search, charsets, parallelLines, false);
    }

    public static long countOccurrence(String path, String search, boolean parallelLines) {
        return countOccurrence(path, search, Set.of(StandardCharsets.UTF_8), parallelLines, false);
    }

    private final String search;
    private final Set<Charset> charsets;
    private final boolean parallelLines;
    private final boolean printErrors;

    private LineCounter(String search, Set<Charset> charsets, boolean parallelLines, boolean printErrors) {
        this.search = search;
        this.charsets = charsets;
        this.parallelLines = parallelLines;
        this.printErrors = printErrors;
    }

    private long countOccurrence(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            Stream<File> stream = Arrays.stream(Objects.requireNonNull(file.listFiles()));
            if (!parallelLines) {
                //noinspection ResultOfMethodCallIgnored
                stream.parallel();
            }
            return stream
                    .mapToLong(f -> countOccurrence(f.getAbsolutePath()))
                    .sum();
        } else {
            return countInFile(path);
        }
    }

    private long countInFile(String path) {
        OptionalLong result = charsets.parallelStream()
                .mapToLong(charset -> {
                    try {
                        return countInFileWithCharset(path, charset);
                    } catch (UncheckedIOException | IOException e) {
                        return -1;
                    }
                })
                .max();
        if (result.isEmpty() || result.getAsLong() == -1) {
            if (printErrors) {
                System.err.println("Unable to read file: " + path);
            }
            return 0;
        } else {
            return result.getAsLong();
        }
    }

    private long countInFileWithCharset(String path, Charset charset) throws IOException {
        try (Stream<String> stream = Files.lines(Path.of(path), charset)) {
            if (parallelLines) {
                //noinspection ResultOfMethodCallIgnored
                stream.parallel();
            }
            return stream
                    .filter(line -> line.contains(search))
                    .count();
        }
    }

}
