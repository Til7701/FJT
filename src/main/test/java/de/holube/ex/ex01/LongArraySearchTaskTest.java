package de.holube.ex.ex01;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LongArraySearchTaskTest {

    private final int longTestLength = 100000000;

    @Test
    void length0Test() {
        Task<long[], Long> task = new LongArraySearchTask(new long[0], 42);
        assertEquals(0, TaskNodeExecutor.executeAll(task));
    }

    @Test
    void length1Test() {
        Task<long[], Long> task = new LongArraySearchTask(new long[]{42}, 42);
        assertEquals(1, TaskNodeExecutor.executeAll(task));
    }

    @Test
    void primLengthTest() {
        int length = 31;
        long[] array = new long[length];
        long target = 42;
        Arrays.fill(array, target);

        Task<long[], Long> task = new LongArraySearchTask(array, target);
        assertEquals(length, TaskNodeExecutor.executeAll(task));
    }

    @Test
    void lengthNotAllTheSameTest() {
        long[] array = new long[21];
        long target = 42;
        Arrays.fill(array, target);
        array[0] = 0;
        array[10] = 7;
        array[20] = 43;

        Task<long[], Long> task = new LongArraySearchTask(array, target);
        assertEquals(18, TaskNodeExecutor.executeAll(task));
    }

    @Test
    void divisibleTest() {
        LongArraySearchTask task = new LongArraySearchTask(new long[12], 3);
        assertTrue(task.isDivisible());

        List<Task<long[], Long>> tasks = task.split();
        for (Task<long[], Long> t : tasks) {
            assertFalse(t.isDivisible());
        }
    }

    @Test
    void longTest() {
        long[] array = new long[longTestLength];
        long target = 4;
        System.out.println("filling array");
        for (int i = 0; i < array.length; i++) {
            array[i] = (long) (Math.random() * 5);
        }

        System.out.println("searching for " + target);
        Task<long[], Long> task = new LongArraySearchTask(array, target);
        System.out.println(TaskNodeExecutor.executeAll(task));
        assertTrue(TaskNodeExecutor.executeAll(task) > 100000);
    }

    @Test
    void longStreamTest() {
        long[] array = new long[longTestLength];
        long target = 4;
        System.out.println("filling array");
        for (int i = 0; i < array.length; i++) {
            array[i] = (long) (Math.random() * 5);
        }

        System.out.println("searching for " + target);
        System.out.println(Arrays.stream(array).filter(x -> x == target).count());
    }

    @Test
    void longParStreamTest() {
        long[] array = new long[longTestLength];
        long target = 4;
        System.out.println("filling array");
        for (int i = 0; i < array.length; i++) {
            array[i] = (long) (Math.random() * 5);
        }

        System.out.println("searching for " + target);
        System.out.println(Arrays.stream(array).parallel().filter(x -> x == target).count());
    }

}
