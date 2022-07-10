package de.holube.ex.ex10;

import java.util.Arrays;
import java.util.Random;

public class SortTest {

    private static final int ITERATIONS = 5;
    private static final int ARRAY_SIZE = 5_000_000;

    public static void main(String[] args) {
        for (int i = 0; i < ITERATIONS; i++) {
            testRun();
            System.out.println("-----------------------------------------------------");
        }
    }

    private static final Random random = new Random();

    private static void testRun() {
        Integer[] values = new Integer[ARRAY_SIZE];
        Arrays.setAll(values, i -> random.nextInt());

        long start = System.nanoTime();
        MergeSortSequential.sort(values);
        long end = System.nanoTime();
        System.out.println("Sequential: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");

        values = new Integer[ARRAY_SIZE];
        Arrays.setAll(values, i -> random.nextInt());

        start = System.nanoTime();
        MergeSortParFJ.sort(values);
        end = System.nanoTime();
        System.out.println("ParallelFJ: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");

        values = new Integer[ARRAY_SIZE];
        Arrays.setAll(values, i -> random.nextInt());

        start = System.nanoTime();
        Arrays.sort(values);
        end = System.nanoTime();
        System.out.println("Arrays.sort: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");

        values = new Integer[ARRAY_SIZE];
        Arrays.setAll(values, i -> random.nextInt());

        start = System.nanoTime();
        Arrays.parallelSort(values);
        end = System.nanoTime();
        System.out.println("Arrays.parallelSort: " + (end - start) + " ns; " + (end - start) / 1_000_000_000.0 + " s");
    }

}
