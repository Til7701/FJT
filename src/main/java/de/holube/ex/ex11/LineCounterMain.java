package de.holube.ex.ex11;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class LineCounterMain {

    public static void main(String[] args) {
        String rootDir = "C:\\Users\\Tilman\\IdeaProjects\\FJT";
        String search = "x";

        // Simple
        long count = LineCounterSimple.countOccurrence(rootDir, search);
        System.out.println(count);

        // Complex
        final Set<Charset> charsets = Set.of(StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1);
        count = LineCounter.countOccurrence(rootDir, search, charsets, false, true);
        System.out.println(count);
    }

}
