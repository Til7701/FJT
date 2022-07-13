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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class LineCounter {

    private static final Set<String> debugSet = new ConcurrentSkipListSet<>();

    public static void main(String[] args) {
        String rootDir = "C:\\Users\\Tilman\\IdeaProjects\\FJT";
        String search = "{";
        final Set<Charset> charsets = Set.of(StandardCharsets.UTF_8);
        final Set<String> ignoredFileTypes = Set.of(".png", ".pb");
        long count = countOccurrence(rootDir, search, charsets, ignoredFileTypes, false, true);
        System.out.println(count);
    }

    private static long countOccurrence(String path, String search, Set<Charset> charsets, Set<String> ignoredFileTypes, boolean parallelLines, boolean printErrors) {
        File file = new File(path);
        if (file.isDirectory()) {
            return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .parallel()
                    .mapToLong(f -> countOccurrence(f.getAbsolutePath(), search, charsets, ignoredFileTypes, parallelLines, printErrors))
                    .sum();
        } else {
            if (ignoredFileTypes.stream().noneMatch(path::endsWith))
                return countInFile(path, search, charsets, parallelLines, printErrors);
            else
                return 0;
        }
    }

    private static long countInFile(String path, String search, Set<Charset> charsets, boolean parallelLines, boolean printErrors) {
        OptionalLong result = charsets.parallelStream()
                .mapToLong(charset -> {
                    try {
                        return countInFileWithCharset(path, search, charset, parallelLines);
                    } catch (UncheckedIOException | IOException e) {
                        return -1;
                    }
                })
                .max();
        if (result.isEmpty() || result.getAsLong() == -1) {
            if (printErrors)
                System.err.println(path);
            return 0;
        } else
            return result.getAsLong();
    }

    private static long countInFileWithCharset(String path, String search, Charset charset, boolean parallelLines) throws IOException {
        try (Stream<String> stream = Files.lines(Path.of(path), charset)) {
            if (parallelLines)
                //noinspection ResultOfMethodCallIgnored
                stream.parallel(); // don't use this for Images
            return stream
                    .filter(line -> line.contains(search))
                    .count();
        }
    }

}
