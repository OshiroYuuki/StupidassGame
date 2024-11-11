package utilz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Connect4AI {
    private static final int SIMULATION_ROUNDS = 10; // Number of simulations for MCTS
    private static final int AI_PLAYER = 2;
    private static final int HUMAN_PLAYER = 1;

    private static final Random random = new Random();

    // Node class to represent game states in the MCTS tree
    static class Node {
        int[][] board;
        int player;
        int visits;
        int wins;
        List<Node> children;
        List<Integer> unvisitedMoves;

        public Node(int[][] board, int player, List<Integer> validMoves) {
            this.board = deepCopyBoard(board);
            this.player = player;
            this.visits = 0;
            this.wins = 0;
            this.children = new ArrayList<>();
            this.unvisitedMoves = new ArrayList<>(validMoves);
        }

        public Node expand(int move) {
            int[][] newBoard = deepCopyBoard(this.board);
            applyMove(newBoard, move, this.player);
            Node childNode = new Node(newBoard, 3 - this.player, getValidMoves(newBoard));
            this.children.add(childNode);
            System.out.println("Expanded node with move: " + move + ", new player: " + (3 - this.player));
            return childNode;
        }

        public void update(int result) {
            this.visits++;
            this.wins += result;
            System.out.println("Backpropagation: Updating node - Visits=" + this.visits + ", Wins=" + this.wins);
        }

        public double uctScore(int totalVisits, double exploration) {
            if (this.visits == 0) return Double.MAX_VALUE;
            double winRate = (double) this.wins / this.visits;
            double uct = winRate + exploration * Math.sqrt(Math.log(totalVisits) / this.visits);
            System.out.println("UCT Score for node - Wins=" + this.wins + ", Visits=" + this.visits +
                               ", TotalVisits=" + totalVisits + ", UCT=" + uct);
            return uct;
        }
    }

    // Main MCTS function
    public static int monteCarloTreeSearch(int[][] board, int player) {
        List<Integer> validMoves = getValidMoves(board);
        Node root = new Node(board, player, validMoves);

        for (int i = 0; i < SIMULATION_ROUNDS; i++) {
            Node node = root;
            List<Node> path = new ArrayList<>();

            // Selection with detailed debug
            System.out.println("Starting selection phase for simulation " + (i + 1));
            while (node.unvisitedMoves.isEmpty() && !node.children.isEmpty()) {
                node = Collections.max(node.children, (a, b) ->
                    Double.compare(a.uctScore(root.visits, 1.41), b.uctScore(root.visits, 1.41)));
                path.add(node);
                System.out.println("Selected node with player: " + node.player);
            }

            // Expansion
            if (!node.unvisitedMoves.isEmpty()) {
                int move = node.unvisitedMoves.remove(0);
                node = node.expand(move);
                path.add(node);
            }

            // Simulation
            int result = simulate(node);
            System.out.println("Simulation result: " + result);

            // Backpropagation
            for (Node n : path) {
                n.update(result);
            }
        }

        // Final move selection debug
        System.out.println("Selecting the best move based on highest visit count:");
        Node bestChild = Collections.max(root.children, (a, b) -> Integer.compare(a.visits, b.visits));
        System.out.println("Best move has visits: " + bestChild.visits);
        return validMoves.get(root.children.indexOf(bestChild));
    }

    // Simulate a random game from the current state to completion
    private static int simulate(Node node) {
        int[][] boardCopy = deepCopyBoard(node.board);
        int currentPlayer = node.player;

        while (true) {
            if (checkWinner(boardCopy, 3 - currentPlayer)) {
                return currentPlayer == AI_PLAYER ? 1 : 0;
            }

            List<Integer> moves = getValidMoves(boardCopy);
            if (moves.isEmpty()) {
                System.out.println("Game ended in a draw.");
                return 0; // Draw
            }

            int move = moves.get(random.nextInt(moves.size()));
            applyMove(boardCopy, move, currentPlayer);
            currentPlayer = 3 - currentPlayer;
        }
    }

    // Check if the given player has won the game
    private static boolean checkWinner(int[][] board, int player) {
        // Check horizontal win
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col <= board[0].length - 4; col++) {
                if (board[row][col] == player &&
                    board[row][col + 1] == player &&
                    board[row][col + 2] == player &&
                    board[row][col + 3] == player) {
                    return true;
                }
            }
        }

        // Check vertical win
        for (int row = 0; row <= board.length - 4; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == player &&
                    board[row + 1][col] == player &&
                    board[row + 2][col] == player &&
                    board[row + 3][col] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (bottom-left to top-right) win
        for (int row = 3; row < board.length; row++) {
            for (int col = 0; col <= board[0].length - 4; col++) {
                if (board[row][col] == player &&
                    board[row - 1][col + 1] == player &&
                    board[row - 2][col + 2] == player &&
                    board[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (top-left to bottom-right) win
        for (int row = 0; row <= board.length - 4; row++) {
            for (int col = 0; col <= board[0].length - 4; col++) {
                if (board[row][col] == player &&
                    board[row + 1][col + 1] == player &&
                    board[row + 2][col + 2] == player &&
                    board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    // Apply a move on the board for a given player
    private static void applyMove(int[][] board, int col, int player) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = player;
                System.out.println("Applied move: Column=" + col + ", Player=" + player);
                return;
            }
        }
    }

    // Placeholder for deep copy function
    private static int[][] deepCopyBoard(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    // Placeholder for getting valid moves
    private static List<Integer> getValidMoves(int[][] board) {
        List<Integer> validMoves = new ArrayList<>();
        for (int col = 0; col < board[0].length; col++) {
            if (board[0][col] == 0) {
                validMoves.add(col);
            }
        }
        System.out.println("Valid moves: " + validMoves);
        return validMoves;
    }
    public int getAIMove(int[][] board) {
        return monteCarloTreeSearch(board, AI_PLAYER);
    }
}
