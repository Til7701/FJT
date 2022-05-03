package de.holube.ex.ex01;

import java.util.ArrayList;
import java.util.List;

/**
 * Task to search for a given value in a long array.
 *
 * @author Tilman Holube
 */
public class LongArraySearchTask implements Task<long[], Long> {

    private final long[] array;
    private final int start;
    private final int end;
    private final long target;

    /**
     * Constructor to create a task for a given array.
     * This is the same as {@code new LongArraySearchTask(array, 0, array.length - 1, target)}
     *
     * @param array  the array to search in
     * @param target the target value
     */
    public LongArraySearchTask(long[] array, long target) {
        this.array = array;
        this.start = 0;
        this.end = array.length;
        this.target = target;
    }

    /**
     * Constructor to create a task for a given array.
     *
     * @param array  the array to search in
     * @param start  the start index
     * @param end    the end index (exclusive)
     * @param target the target value
     */
    public LongArraySearchTask(long[] array, int start, int end, long target) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.target = target;
    }

    @Override
    public boolean isDivisible() {
        // this task is only once divisible
        if (array.length <= 4) return false;
        return start == 0 && end == array.length;
    }

    @Override
    public Long execute() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                sum++;
            }
        }
        return sum;
    }

    @Override
    public List<Task<long[], Long>> split() {
        int threads = Runtime.getRuntime().availableProcessors();
        List<Task<long[], Long>> subTasks = new ArrayList<>(threads);
        for (int i = 0; i < threads; i++) {
            int newStart = start + i * (end - start) / threads;
            int newEnd = start + (i + 1) * (end - start) / threads;
            subTasks.add(new LongArraySearchTask(array, newStart, newEnd, target));
        }
        return subTasks;
    }

    @Override
    public Long combine(List<Long> results) {
        long sum = 0;
        for (Long l : results) {
            sum += l;
        }
        return sum;
    }
}
