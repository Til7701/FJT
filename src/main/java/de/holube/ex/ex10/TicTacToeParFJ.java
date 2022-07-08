package de.holube.ex.ex10;

import java.util.Scanner;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

// https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
//Java program to find the
//next optimal move for a player
public class TicTacToeParFJ {
    static class Move {
        int row;
        int col;
    }

    static char player = 'x';
    static char opponent = 'o';

    //This function returns true if there are moves
    //remaining on the board. It returns false if
    //there are no moves left to play.
    static boolean isMovesLeft(char board[][]) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == '_')
                    return true;
        return false;
    }

    //This is the evaluation function as discussed
    //in the previous article ( http://goo.gl/sJgv68 )
    static int evaluate(char b[][]) {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
                if (b[row][0] == player)
                    return +10;
                else if (b[row][0] == opponent)
                    return -10;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
                if (b[0][col] == player)
                    return +10;

                else if (b[0][col] == opponent)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            if (b[0][0] == player)
                return +10;
            else if (b[0][0] == opponent)
                return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2] == player)
                return +10;
            else if (b[0][2] == opponent)
                return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }

    static class MiniMaxTask extends RecursiveTask<Integer> {

        private final char[][] board;
        private final boolean isMax;

        public MiniMaxTask(char[][] board, boolean isMax) {
            this.board = board;
            this.isMax = isMax;
        }

        @Override
        protected Integer compute() {
            int score = evaluate(board);

            // If Maximizer has won the game
            // return his/her evaluated score
            if (score == 10)
                return score;

            // If Minimizer has won the game
            // return his/her evaluated score
            if (score == -10)
                return score;

            // If there are no more moves and
            // no winner then it is a tie
            if (!isMovesLeft(board))
                return 0;

            // If this maximizer's move
            if (isMax) {
                int best = -1000;

                // Traverse all cells
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        // Check if cell is empty
                        if (board[i][j] == '_') {
                            // Make the move
                            board[i][j] = player;

                            // Call minimax recursively and choose
                            // the maximum value
                            MiniMaxTask task = new MiniMaxTask(board, !isMax);
                            best = Math.max(best, task.compute());

                            // Undo the move
                            board[i][j] = '_';
                        }
                    }
                }
                return best;
            }

            // If this minimizer's move
            else {
                int best = 1000;

                // Traverse all cells
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        // Check if cell is empty
                        if (board[i][j] == '_') {
                            // Make the move
                            board[i][j] = opponent;

                            // Call minimax recursively and choose
                            // the minimum value
                            MiniMaxTask task = new MiniMaxTask(board, !isMax);
                            best = Math.min(best, task.compute());

                            // Undo the move
                            board[i][j] = '_';
                        }
                    }
                }
                return best;
            }
        }

    }


    //This will return the best possible
    //move for the player
    static Move findBestMove(char board[][]) {
        int bestVal = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;

        MiniMaxTask[][] tasks = new MiniMaxTask[board.length][board[0].length];

        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if cell is empty
                if (board[i][j] == '_') {
                    // Make the move
                    char[][] boardCopy = new char[board.length][board[0].length];
                    for (int k = 0; k < board.length; k++) {
                        System.arraycopy(board[k], 0, boardCopy[k], 0, board[k].length);
                    }
                    boardCopy[i][j] = player;

                    // compute evaluation function for this
                    // move.
                    MiniMaxTask task = new MiniMaxTask(boardCopy, false);
                    tasks[i][j] = task;
                    task.fork();
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if cell is empty
                if (tasks[i][j] != null) {
                    int moveVal = tasks[i][j].join();

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal || (moveVal == bestVal && ThreadLocalRandom.current().nextBoolean())) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    //Driver code
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            char board[][] = {
                    {'_', '_', '_'},
                    {'_', '_', '_'},
                    {'_', '_', '_'}};
            print(board);
            char currentPlayer = player;
            System.out.println("Do you want to start? (yes/no): ");
            if (scanner.next().equalsIgnoreCase("yes")) {
                currentPlayer = opponent;
            }
            while (true) {
                if (!isMovesLeft(board)) {
                    System.out.println("drawn game");
                    break;
                }
                if (currentPlayer == player) {
                    Move bestMove = findBestMove(board);
                    board[bestMove.row][bestMove.col] = player;
                    print(board);
                    if (evaluate(board) == 10) {
                        System.out.println("computer is winner");
                        break;
                    }
                    currentPlayer = opponent;
                } else {
                    while (true) {
                        System.out.println("row (0..2): ");
                        int row = Integer.parseInt(scanner.next());
                        System.out.println("column (0..2): ");
                        int col = Integer.parseInt(scanner.next());
                        if (row < 0 || row > 2 || col < 0 || col > 2 || board[row][col] != '_') {
                            System.out.println("invalid turn");
                        } else {
                            board[row][col] = opponent;
                            break;
                        }
                    }
                    print(board);
                    if (evaluate(board) == -10) {
                        System.out.println("you are winner");
                        break;
                    }
                    currentPlayer = player;
                }
            }
        }
    }

    static void print(char[][] board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

}
