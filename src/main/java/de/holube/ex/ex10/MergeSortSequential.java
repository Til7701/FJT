package de.holube.ex.ex10;

public class MergeSortSequential {

    @SuppressWarnings("unchecked")
    public static <E> void sort(Comparable<E>[] a) {
        Comparable<E>[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    private static <E> void sort(Comparable<E>[] a, Comparable<E>[] aux, int lo, int hi) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    private static <E> void merge(Comparable<E>[] a, Comparable<E>[] aux, int lo, int mid, int hi) {
        System.arraycopy(a, lo, aux, lo, (hi - lo) + 1);

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    @SuppressWarnings("unchecked")
    private static <E> boolean less(Comparable<E> v, Comparable<E> w) {
        return v.compareTo((E) w) < 0;
    }
}