package de.holube.ex.ex08;

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

    public GameOfLife(int rows, int cols) {
        cells = new Cell[rows][cols];
        numberOfRows = rows;
        numberOfColumns = cols;
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                cells[r][c] = new Cell(r, c);
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
        boolean[][] newStates = new boolean[numberOfRows][numberOfColumns];
        while (true) {
            for (int r = 0; r < cells.length; r++) {
                for (int c = 0; c < cells[0].length; c++) {
                    newStates[r][c] = cells[r][c].calcNextState();
                }
            }
            for (int r = 0; r < cells.length; r++) {
                for (int c = 0; c < cells[0].length; c++) {
                    cells[r][c].setState(newStates[r][c]);
                }
            }
            print();
            try {
                Thread.sleep(500);
            } catch (Exception exc) {
            }
        }
    }

    class Cell {
        private final int row;
        private final int col;

        private boolean living;

        public Cell(int r, int c) {
            row = r;
            col = c;
            living = false;
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
