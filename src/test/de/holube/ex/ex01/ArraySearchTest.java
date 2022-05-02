package de.holube.ex.ex01;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * This class tests the {@link ArraySearch} class.
 *
 * @author Tilman Holube
 */
class ArraySearchTest {

    private final int[] oddLengthArray = new int[]{1, 5, 2, 7, 4, 9, -4};
    private final Map<Integer, boolean[]> expectedResultMapOdd = Map.of(
            1, new boolean[]{true, false},
            5, new boolean[]{true, false},
            2, new boolean[]{true, false},
            7, new boolean[]{true, false},
            4, new boolean[]{false, true},
            9, new boolean[]{false, true},
            -4, new boolean[]{false, true}
    );

    private final int[] evenLengthArray = new int[]{1, 5, 2, 7, 4, 9, -4, -3};
    private final Map<Integer, boolean[]> expectedResultMapEven = Map.of(
            1, new boolean[]{true, false},
            5, new boolean[]{true, false},
            2, new boolean[]{true, false},
            7, new boolean[]{true, false},
            4, new boolean[]{false, true},
            9, new boolean[]{false, true},
            -4, new boolean[]{false, true},
            -3, new boolean[]{false, true}
    );

    @Test
    void testSearchOddLengthArray() {
        for (int value : expectedResultMapOdd.keySet()) {
            searchTestHelper(oddLengthArray, value, expectedResultMapOdd.get(value));
        }
    }

    @Test
    void testSearchEvenLengthArray() {
        for (int value : expectedResultMapEven.keySet()) {
            searchTestHelper(evenLengthArray, value, expectedResultMapEven.get(value));
        }
    }

    private void searchTestHelper(int[] array, int value, boolean[] expectedResult) {
        boolean[] result = ArraySearch.searchForEntryAndWait(array, value);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    void arraySearchBothTest() {
        int[] array = new int[]{1, 5, 9, 7, 4, 9, -4};
        boolean[] result = ArraySearch.searchForEntryAndWait(array, 9);
        assertArrayEquals(new boolean[]{true, true}, result);
    }

    @Test
    void arraySearchNoneTest() {
        int[] array = new int[]{1, 5, 9, 7, 4, 9, -4};
        boolean[] result = ArraySearch.searchForEntryAndWait(array, 42);
        assertArrayEquals(new boolean[]{false, false}, result);
    }

    @Test
    void arraySearchNullTest() {
        boolean[] result = ArraySearch.searchForEntryAndWait(null, 42);
        assertArrayEquals(new boolean[]{false, false}, result);
    }

    @Test
    void arraySearchLength0Test() {
        int[] array = new int[]{};
        boolean[] result = ArraySearch.searchForEntryAndWait(array, -4);
        assertArrayEquals(new boolean[]{false, false}, result);
    }

    @Test
    void arraySearchLength1Test() {
        int[] array = new int[]{42};
        boolean[] result = ArraySearch.searchForEntryAndWait(array, 42);
        assertArrayEquals(new boolean[]{true, true}, result);
    }

    @Test
    void arraySearchLength2Test() {
        int[] array = new int[]{42, -42};
        boolean[] result = ArraySearch.searchForEntryAndWait(array, 42);
        assertArrayEquals(new boolean[]{true, false}, result);
    }
}
