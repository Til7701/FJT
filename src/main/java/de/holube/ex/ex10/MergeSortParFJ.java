package de.holube.ex.ex10;

import java.util.concurrent.RecursiveAction;

public class MergeSortParFJ<E> extends RecursiveAction {

    @SuppressWarnings("unchecked")
    public static <E> void sort(Comparable<E>[] a) {
        Comparable<E>[] aux = new Comparable[a.length];
        MergeSortParFJ<E> mspfj = new MergeSortParFJ<>(a, aux, 0, a.length - 1);
        mspfj.invoke();
    }


    private final Comparable<E>[] a;
    private final Comparable<E>[] aux;
    private final int start;
    private final int end;

    private MergeSortParFJ(Comparable<E>[] a, Comparable<E>[] aux, int start, int end) {
        this.a = a;
        this.aux = aux;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start < 1)
            return;
        int mid = start + (end - start) / 2;
        invokeAll(new MergeSortParFJ<>(a, aux, start, mid), new MergeSortParFJ<>(a, aux, mid + 1, end));
        merge(a, aux, start, mid, end);
    }

    private void merge(Comparable<E>[] a, Comparable<E>[] aux, int lo, int mid, int hi) {
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
    private boolean less(Comparable<E> v, Comparable<E> w) {
        return v.compareTo((E) w) < 0;
    }

}
