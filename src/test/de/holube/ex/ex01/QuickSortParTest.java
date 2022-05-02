package de.holube.ex.ex01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickSortParTest {

    @Test
    void quickSortParTest() {
        int[] result = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] input = new int[]{10, 3, 6, 1, 9, 4, 5, 2, 7, 8};

        QuickSortPar.sort(input);
        assertArrayEquals(result, input);
    }

    @Test
    void quickSortParLength0Test() {
        int[] result = new int[]{};
        int[] input = new int[]{};

        QuickSortPar.sort(input);
        assertArrayEquals(result, input);
    }

    @Test
    void quickSortParLength1Test() {
        int[] result = new int[]{42};
        int[] input = new int[]{42};

        QuickSortPar.sort(input);
        assertArrayEquals(result, input);
    }

    @Test
    void quickSortParLongTest() {
        int n = 10000;
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = (int) (Math.random() * n);
        }

        QuickSortPar.sort(array);
        testSorted(array);
    }

    private void testSorted(int[] input) {
        for (int i = 0; i < input.length - 1; i++) {
            assertTrue(input[i] <= input[i + 1]);
        }
    }

}
