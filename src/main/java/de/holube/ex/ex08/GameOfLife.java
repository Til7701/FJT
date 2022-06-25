package de.holube.ex.ex08;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GameOfLife {

    public static void main(String[] args) {
        GameOfLife gol = new GameOfLife(20, 30);

        // only as example: set Glider
        gol.changeState(2, 2);
        gol.changeState(3, 3);
        gol.changeState(4, 3);
        gol.changeState(4, 2);
        gol.changeState(4, 1);

        gol.runSimulation();

    }

    private final Cell[][] cells;
    private final int numberOfRows;
    private final int numberOfColumns;

    private final CyclicBarrier barrier;

    public GameOfLife(int rows, int cols) {
        barrier = new CyclicBarrier((rows * cols) + 1);
        cells = new Cell[rows][cols];
        numberOfRows = rows;
        numberOfColumns = cols;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                cells[r][c] = new Cell(r, c);
                cells[r][c].setDaemon(true);
            }
        }
    }

    public void changeState(int row, int col) {
        cells[row][col].changeState();
    }

    public void print() {
        for (int c = 0; c < cells[0].length + 2; c++) {
            System.out.print('-');
        }
        System.out.println();

        for (int r = 0; r < cells.length; r++) {
            System.out.print('|');
            for (int c = 0; c < cells[0].length; c++) {
                if (cells[r][c].isLiving()) {
                    System.out.print('*');
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println('|');
        }

        for (int c = 0; c < cells[0].length + 2; c++) {
            System.out.print('-');
        }
        System.out.println();
    }

    public void runSimulation() {
        for (Cell[] cs : cells) {
            for (Cell c : cs) {
                c.start();
            }
        }
        try {
            barrier.await();

            while (!Thread.interrupted()) {
                barrier.await();
                barrier.await();

                print();
                Thread.sleep(500);
            }
        } catch (InterruptedException | BrokenBarrierException e) {

        }
    }

    class Cell extends Thread {
        private final int row;
        private final int col;

        private boolean living;

        public Cell(int r, int c) {
            row = r;
            col = c;
            living = false;
        }

        @Override
        public void run() {
            try {
                barrier.await();

                while (!Thread.interrupted()) {
                    boolean nextState = calcNextState();
                    barrier.await();
                    living = nextState;
                    barrier.await();
                }
            } catch (BrokenBarrierException | InterruptedException e) {
                // ignore
            }
        }

        public void changeState() {
            living = !living;
        }

        public void setState(boolean living) {
            this.living = living;
        }

        public boolean isLiving() {
            return living;
        }

        public boolean calcNextState() {
            int livingN = getNoOfLivingNeighbors();
            if (!living) { // dead
                if (livingN == 3) {
                    return true;
                } else {
                    return living;
                }
            } else { // living
                if (livingN < 2 || livingN > 3) {
                    return false;
                } else {
                    return living;
                }
            }
        }

        private int getNoOfLivingNeighbors() {
            int number = 0;
            number = number
                    + (cells[(row + numberOfRows - 1) % numberOfRows][(col + numberOfColumns - 1) % numberOfColumns]
                    .isLiving() ? 1 : 0);
            number = number + (cells[(row + numberOfRows - 1) % numberOfRows][col].isLiving() ? 1 : 0);
            number = number
                    + (cells[(row + numberOfRows - 1) % numberOfRows][(col + 1) % numberOfColumns].isLiving() ? 1 : 0);

            number = number + (cells[row][(col + numberOfColumns - 1) % numberOfColumns].isLiving() ? 1 : 0);
            number = number + (cells[row][(col + 1) % numberOfColumns].isLiving() ? 1 : 0);

            number = number
                    + (cells[(row + 1) % numberOfRows][(col + numberOfColumns - 1) % numberOfColumns].isLiving() ? 1
                    : 0);
            number = number + (cells[(row + 1) % numberOfRows][col].isLiving() ? 1 : 0);
            number = number + (cells[(row + 1) % numberOfRows][(col + 1) % numberOfColumns].isLiving() ? 1 : 0);

            return number;
        }

    }

}
