package de.holube.ex.ex01;

public class QuickSortPar {

    // sortiert das uebergebene Array in aufsteigender Reihenfolge
    // gemaess dem QuickSort-Algorithmus
    public static void sort(int[] zahlen) {
        quickSort(zahlen, 0, zahlen.length - 1);
    }

    // der Quicksort-Algorithmus wird auf dem Array zwischen den
    // angegebenen Indizes ausgefuehrt
    private static void quickSort(int[] zahlen, int linkerIndex, int rechterIndex) {
        if (linkerIndex < rechterIndex) {
            int pivotIndex = zerlege(zahlen, linkerIndex, rechterIndex);
            Thread t1 = new Thread(() -> quickSort(zahlen, linkerIndex, pivotIndex - 1));
            Thread t2 = new Thread(() -> quickSort(zahlen, pivotIndex + 1, rechterIndex));
            t1.start();
            t2.start();
            while (t1.isAlive()) {
                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // liefert den Index des Pivot-Elementes und ordnet das Array innerhalb
    // der angegebenen Indizes so um, dass alle Zahlen links vom Index
    // kleiner oder gleich und alle Zahlen rechts vom Index groesser
    // oder gleich dem Pivot-Element sind
    private static int zerlege(int[] zahlen, int linkerIndex, int rechterIndex) {
        int pivotIndex = waehlePivotIndex(zahlen, linkerIndex, rechterIndex);
        int pivotWert = zahlen[pivotIndex];
        // das Pivot-Element kommt nach ganz rechts im Array
        tauschen(zahlen, pivotIndex, rechterIndex);
        int l = linkerIndex - 1;
        int r = rechterIndex;
        // ordne das Array so um, dass jeweils alle Elemente links vom
        // Zeiger l kleiner und alle Elemente rechts vom Zeiger r groesser
        // als das Pivot-Element sind
        do {
            l++;
            while (l <= rechterIndex && zahlen[l] <= pivotWert)
                l++;
            r--;
            while (r >= linkerIndex && zahlen[r] >= pivotWert)
                r--;
            if (l < r) {
                tauschen(zahlen, l, r);
            }
        } while (l < r);
        // platziere das Pivot-Element an seine korrekte Position
        if (l < rechterIndex) {
            tauschen(zahlen, l, rechterIndex);
            return l;
        } else {
            return rechterIndex;
        }
    }

    // waehlt einen beliebigen Index zwischen den angegebenen Indizes
    private static int waehlePivotIndex(int[] zahlen, int linkerIndex, int rechterIndex) {
        // in diesem Fall einfach der mittleren Index
        return (linkerIndex + rechterIndex) / 2;
        /*
         * Alternative 1: return rechterIndex;
         */
        /*
         * Alternative 2 (mittleres von drei Elementen): int index1 = (linkerIndex +
         * rechterIndex) / 2; int index2 = (linkerIndex + index1) / 2; int index3 =
         * (index1 + rechterIndex) / 2; if (zahlen[index1] <= zahlen[index2] &&
         * zahlen[index2] <= zahlen[index3]) return zahlen[index2]; if (zahlen[index2]
         * <= zahlen[index3] && zahlen[index3] <= zahlen[index1]) return zahlen[index3];
         * return zahlen[index1];
         */
    }

    // tauscht die Elemente des Arrays an den angegebenen Indizes
    private static void tauschen(int[] zahlen, int index1, int index2) {
        if (index1 != index2) {
            int help = zahlen[index1];
            zahlen[index1] = zahlen[index2];
            zahlen[index2] = help;
        }
    }
}
