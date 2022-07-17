package de.holube.ex.ex11;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class LineCounterMain {

    public static void main(String[] args) {
        String rootDir = "C:\\Users\\Tilman\\IdeaProjects";
        String search = "debugging";

        // Simple
        long now = System.currentTimeMillis();
        long count = LineCounterSimple.countOccurrence(rootDir, search);
        System.out.println("Simple: " + (System.currentTimeMillis() - now) + "ms; Result: " + count);

        // Complex
        final Set<Charset> charsets = Set.of(StandardCharsets.UTF_8);
        now = System.currentTimeMillis();
        count = LineCounter.countOccurrence(rootDir, search, charsets, false, false);
        System.out.println("Complex parallel folders: " + (System.currentTimeMillis() - now) + "ms; Result: " + count);

        // Complex
        now = System.currentTimeMillis();
        count = LineCounter.countOccurrence(rootDir, search, charsets, true, false);
        System.out.println("Complex parallel lines: " + (System.currentTimeMillis() - now) + "ms; Result: " + count);
    }

}
