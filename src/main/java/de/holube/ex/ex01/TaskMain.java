package de.holube.ex.ex01;

public class TaskMain {

    public static void main(String[] args) {
        long[] array = new long[100000000];
        long target = 4;
        System.out.println("filling array");
        for (int i = 0; i < array.length; i++) {
            array[i] = (long) (Math.random() * 5);
        }

        System.out.println("searching for " + target);
        Task<long[], Long> task = new LongArraySearchTask(array, target);
        System.out.println(TaskNodeExecutor.executeAll(task));
    }
}
