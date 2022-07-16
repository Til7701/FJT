package de.holube.ex.ex11;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Prim {

    public static int primSeq() {
        return (int) IntStream.range(2, 1_000_000)
                .filter(i ->
                        IntStream.range(2, (int) Math.sqrt(i) + 1)
                                .noneMatch(j -> i % j == 0)
                )
                .count();
    }

    public static int primPar() {
        return (int) IntStream.range(2, 1_000_000)
                .parallel()
                .filter(i ->
                        IntStream.range(2, (int) Math.sqrt(i) + 1)
                                .noneMatch(j -> i % j == 0)
                )
                .count();
    }

    public static int primParWO2() {
        return (int) IntStream.range(2, 1_000_000)
                .parallel()
                .filter(i -> i % 2 != 0)
                .filter(i ->
                        IntStream.range(2, (int) Math.sqrt(i) + 1)
                                .noneMatch(j -> i % j == 0)
                )
                .count() + 1;
    }

    public static int primParWO2Infinite() {
        return (int) Stream.iterate(3, i -> i + 2)
                .limit(500_000)
                .parallel()
                .filter(i ->
                        IntStream.range(2, (int) Math.sqrt(i) + 1)
                                .noneMatch(j -> i % j == 0)
                )
                .count() + 1;
    }

    private static final int ITERATIONS = 5;

    public static void main(String[] args) {
        for (int i = 0; i < ITERATIONS; i++) {
            testRun();
            System.out.println("-----------------------------------------------------");
        }
    }

    private static void testRun() {
        long start = System.nanoTime();
        System.out.println(primSeq());
        long end = System.nanoTime();
        System.out.println("Sequential: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");

        start = System.nanoTime();
        System.out.println(primPar());
        end = System.nanoTime();
        System.out.println("Parallel: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");

        start = System.nanoTime();
        System.out.println(primParWO2());
        end = System.nanoTime();
        System.out.println("ParallelWO2: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");

        start = System.nanoTime();
        System.out.println(primParWO2Infinite());
        end = System.nanoTime();
        System.out.println("ParallelWO2: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");
    }

}
