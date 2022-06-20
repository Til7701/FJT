package de.holube.ex.ex07.trees;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BinTreeBFSThread extends Thread {

    private final int nr;
    private final BlockingDeque<BinTree>[] workDeque;
    private final TerminationMonitor barrier;

    private final String data;

    private final AtomicReference<BinTree> result = new AtomicReference<>();

    public BinTreeBFSThread(int nr, String data, BlockingDeque<BinTree>[] workDeque, TerminationMonitor barrier) {
        setDaemon(true);

        this.nr = nr;
        this.data = data;
        this.workDeque = workDeque;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        final int len = this.workDeque.length;
        BinTree tree;

        // Hole Elemente aus der dem Thread zugeordneten Queue
        tree = this.workDeque[this.nr].pollFirst();
        while (true) {
            // Prüfe, ob in der dem Task zugeordneten Queue Elemente vorhanden sind
            while (tree != null && !this.barrier.isFound()) {
                if (tree.getValue().equals(this.data)) {
                    this.result.set(tree);
                    this.barrier.setFound(true);
                    return;
                }

                BinTree left = tree.getLeft();
                if (left != null && !left.isEmpty()) {
                    this.workDeque[nr].add(left);
                }
                BinTree right = tree.getRight();
                if (right != null && !right.isEmpty()) {
                    this.workDeque[nr].add(right);
                }

                tree = this.workDeque[this.nr].pollFirst();
            }

            // Queue ist jetzt leer
            this.barrier.setActive(false);
            if (this.barrier.isFound()) {
                return;
            }

            // Work-Stealing-Procedure

            // Wenn es nur einen Task gibt, ist Work-Stealing sinnlos
            if (len == 1) {
                return;
            }

            while (tree == null) {
                // Suche "Victim-Queue", Strategie Round-Robin
                for (int i = 1; i < len; i++) {
                    int victimQueue = (this.nr + i) % len;
                    this.barrier.setActive(true);

                    // Hole Item aus der Victim-Queue
                    tree = this.workDeque[victimQueue].pollLast();
                    if (tree != null) {
                        break; // Element ist vorhanden
                    }
                    this.barrier.setActive(false);
                }
                // Alle Elemente sind abgearbeitet
                if (this.barrier.isTerminated()) {
                    return;
                }
            }
        }
    }

    /**
     * Returns the result of the search. This should only be called after the thread
     * has finished.
     *
     * @return the result of the search
     */
    public BinTree getResult() {
        return result.get();
    }

    public static class TerminationMonitor {

        private final AtomicInteger count;
        private final AtomicBoolean found;

        TerminationMonitor(int no) {
            this.count = new AtomicInteger(no);
            this.found = new AtomicBoolean(false);
        }

        void setActive(boolean active) {
            if (active)
                count.getAndIncrement();
            else
                count.getAndDecrement();
        }

        boolean isTerminated() {
            return count.get() == 0 || found.get();
        }

        void setFound(boolean found) {
            this.found.set(found);
        }

        boolean isFound() {
            return found.get();
        }
    }

}
