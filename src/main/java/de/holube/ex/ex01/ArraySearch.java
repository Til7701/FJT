package de.holube.ex.ex01;

/**
 * This class implements the search for an element in an array.
 * It only returns a boolean value for the left and right part of the array.
 *
 * @author Tilman Holube
 */
public class ArraySearch extends Thread {

    private static final String OUTPUT = "%d ist im %s Teil %s";
    private static final String IN = "vorhanden";
    private static final String NOT_IN = "nicht vorhanden";

    private final int[] array;
    private final int start;
    private final int end;
    private final int target;

    private boolean result;

    /**
     * Constructor for the ArraySearch class
     *
     * @param array  the array to search in
     * @param start  the start index of the array to start searching from
     * @param end    the end index of the array to end searching at (inclusive)
     * @param target the target to search for
     */
    private ArraySearch(int[] array, int start, int end, int target) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.target = target;
    }

    @Override
    public void run() {
        if (end < start) {
            setResult(false);
            return;
        }
        for (int i = start; i <= end; i++) {
            if (array[i] == target) {
                setResult(true);
                return;
            }
        }
        setResult(false);
    }

    /**
     * Getter for the result. If the search is not finished yet, it waits until it is finished.
     *
     * @return true, if the target is in the array, false otherwise
     */
    private boolean getResult() {
        while (this.isAlive()) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return this.result;
    }

    /**
     * Setter for the result. It also prints the result to the console.
     *
     * @param result the result to set
     */
    private void setResult(boolean result) {
        this.result = result;

        String part;
        if (start == 0 && end == array.length - 1) part = "gesamten";
        else if (start == 0) part = "linken";
        else if (end == array.length - 1) part = "rechten";
        else part = "mittleren";

        System.out.printf((OUTPUT) + "%n", target, part, result ? IN : NOT_IN);
    }

    /**
     * This method starts the search for an entry in the array. It splits the array in two parts and searches for the
     * target in each part. Then it waits until the search is finished and returns and prints the result.
     * <p>
     * If the array is empty or null the method returns false for both parts. If the length is 1 the method returns true
     * for both parts.
     *
     * @param array  the array to search in
     * @param target the target to search for
     * @return an entry is true, if the target is in the array, false otherwise. [0] -> left part, [1] -> right part
     */
    public static boolean[] searchForEntryAndWait(int[] array, int target) {
        if (array == null || array.length == 0) return new boolean[]{false, false};
        if (array.length == 1) {
            if (array[0] == target) return new boolean[]{true, true};
            else return new boolean[]{false, false};
        }

        int middle = (array.length - 1) / 2;
        ArraySearch arraySearch1 = new ArraySearch(array, 0, middle, target);
        ArraySearch arraySearch2 = new ArraySearch(array, middle + 1, array.length - 1, target);

        arraySearch1.start();
        arraySearch2.run();

        return new boolean[]{arraySearch1.getResult(), arraySearch2.getResult()};
    }

    /**
     * This method starts the search for an entry in the array. It splits the array in two parts and searches for the
     * target in each part. It does not wait until the search is finished. Once it is finished it prints the result to
     * the console.
     * <p>
     * If the array is empty or null the method returns false for both parts. If the length is 1 the method returns true
     * for both parts.
     *
     * @param array  the array to search in
     * @param target the target to search for
     */
    public static void searchForEntry(int[] array, int target) {
        int middle = (array.length - 1) / 2;

        new ArraySearch(array, 0, middle, target).start();
        new ArraySearch(array, middle + 1, array.length - 1, target).start();
    }
}
