package de.holube.ex.ex01;

public class ArraySearch extends Thread {

    private static final String output = "%d ist im %s Teil %s";
    private static final String in = "vorhanden";
    private static final String notIn = "nicht vorhanden";

    private final int[] array;
    private final int start;
    private final int end;
    private final int target;
    private final String part;

    private ArraySearch(int[] array, int start, int end, int target, String part) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.target = target;
        this.part = part;
    }

    @Override
    public void run() {
        if (end < start) {
            printResult(false);
            return;
        }
        for (int i = start; i <= end; i++) {
            if (array[i] == target) {
                printResult(true);
                return;
            }
        }
        printResult(false);
    }

    private void printResult(boolean result) {
        System.out.printf((output) + "%n", target, part, result ? in : notIn);
    }

    public static void searchForEntry(int[] array, int target) {
        int middle = array.length / 2;
        new ArraySearch(array, 0, middle, target, "linken").start();
        new ArraySearch(array, middle + 1, array.length - 1, target, "rechten").start();
    }

    public static void main(String[] args) {
        int[] array = new int[] {1, 5, 2, 7, 4, 9, -4};
        searchForEntry(array, 9);
    }
}
