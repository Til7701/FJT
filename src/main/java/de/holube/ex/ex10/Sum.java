package de.holube.ex.ex10;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class Sum {

    public static int sumSeq(int[] values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum;
    }

    public static int sumFJ(int[] values) {
        SumTask task = new SumTask(0, values.length, values);
        return task.compute();
    }

    private static class SumTask extends RecursiveTask<Integer> {

        private static final int MAX_SIZE = 1000;

        private final int start;
        private final int end;
        private final int[] values;

        public SumTask(int start, int end, int[] values) {
            this.start = start;
            this.end = end;
            this.values = values;
        }

        @Override
        protected Integer compute() {
            if (end - start < MAX_SIZE) {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += values[i];
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                SumTask left = new SumTask(start, mid, values);
                SumTask right = new SumTask(mid, end, values);
                left.fork();
                return right.compute() + left.join();
            }
        }

    }

    private static final Random random = new Random();

    public static void main(String[] args) {
        final int ITERATIONS = 5;
        final int ARRAY_SIZE = 1_000_000;

        for (int i = 0; i < ITERATIONS; i++) {
            testRun(ARRAY_SIZE);
        }
    }

    private static void testRun(int arraySize) {
        System.out.println("Filling array with random values");
        int[] values = new int[arraySize];
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt();
        }

        //System.out.println("Starting sequential sum");
        long start = System.nanoTime();
        int sum = sumSeq(values);
        long end = System.nanoTime();
        System.out.println("Sequential sum took " + (end - start) + " ns; Result: " + sum);

        System.out.println("Filling array with random values");
        values = new int[arraySize];
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt();
        }

        //System.out.println("Starting parallel sum");
        start = System.nanoTime();
        sum = sumFJ(values);
        end = System.nanoTime();
        System.out.println("Parallel sum took " + (end - start) + " ns; Result: " + sum);
        System.out.println("-----------------------------------------------------");
    }

}
