package de.holube.ex.ex01;

import java.util.ArrayList;
import java.util.List;

public class LongArraySearchTask implements Task<long[], Long> {

    private static final int MAX_ARRAY_LENGTH = 1000;

    private final long[] array;
    private final int start;
    private final int end;
    private final long target;

    public LongArraySearchTask(long[] array, long target) {
        this.array = array;
        this.start = 0;
        this.end = array.length - 1;
        this.target = target;
    }

    public LongArraySearchTask(long[] array, int start, int end, long target) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.target = target;
    }

    @Override
    public boolean isDivisible() {
        return end - start > MAX_ARRAY_LENGTH;
    }

    @Override
    public Long execute() {
        long sum = 0;
        for (int i = start; i <= end; i++) {
            if (array[i] == target) {
                sum++;
            }
        }
        return sum;
    }

    @Override
    public List<Task<long[], Long>> split() {
        List<Task<long[], Long>> subTasks = new ArrayList<>();
        for (int i = start; i <= end; i += MAX_ARRAY_LENGTH) {
            int end = Math.min(i + MAX_ARRAY_LENGTH, array.length - 1);
            subTasks.add(new LongArraySearchTask(array, i, end, target));
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
