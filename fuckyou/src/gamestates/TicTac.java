package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public class TicTac extends State implements Statemethods {

    private int[][] board; // 0 for empty, 1 for player 1, 2 for player 2
    private boolean playerOneTurn; // Track whose turn it is
    private String winner; // Track the winner

    private final int ROWS = 6;
    private final int COLUMNS = 7;
    private final int boardWidth = (int) (Game.GAME_WIDTH * 0.7);
    private final int boardHeight = (int) (Game.GAME_HEIGHT * 0.7);
    private final int marginX = (Game.GAME_WIDTH - boardWidth) / 2;
    private final int marginY = (Game.GAME_HEIGHT - boardHeight) / 2;

    public TicTac(Game game) {
        super(game);
        board = new int[ROWS][COLUMNS];
        playerOneTurn = true; // Player 1 starts
        winner = null;
    }

    @Override
    public void update() {
        // Game logic is handled in mousePressed
    }

    @Override
    public void draw(Graphics g) {
        // Clear background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // Draw the Connect 4 board in the center
        int cellWidth = boardWidth / COLUMNS;
        int cellHeight = boardHeight / ROWS;
        g.setColor(Color.WHITE);
        for (int i = 0; i <= COLUMNS; i++) {
            g.drawLine(marginX + i * cellWidth, marginY, marginX + i * cellWidth, marginY + boardHeight);
        }
        for (int i = 0; i <= ROWS; i++) {
            g.drawLine(marginX, marginY + i * cellHeight, marginX + boardWidth, marginY + i * cellHeight);
        }

        // Draw the chips
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int x = marginX + col * cellWidth + cellWidth / 4;
                int y = marginY + row * cellHeight + cellHeight / 4;
                int diameter = cellWidth / 2;
                
                if (board[row][col] == 1) {
                    g.setColor(Color.RED);
                    g.fillOval(x, y, diameter, diameter);
                } else if (board[row][col] == 2) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(x, y, diameter, diameter);
                }
            }
        }

        // Display the winner if there's one
        if (winner != null) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Winner: " + winner, Game.GAME_WIDTH / 2 - 100, Game.GAME_HEIGHT / 2);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (winner != null) return; // Prevent actions if there's a winner

        // Check if the click is within the game board boundaries
        if (e.getX() < marginX || e.getX() > marginX + boardWidth || e.getY() < marginY || e.getY() > marginY + boardHeight) {
            return; // Click outside the board area
        }

        // Determine which column was clicked within the board area
        int col = (e.getX() - marginX) / (boardWidth / COLUMNS);

        // Place chip in the lowest empty row within the chosen column
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = playerOneTurn ? 1 : 2;
                checkWinner(row, col); // Check if this move wins the game
                playerOneTurn = !playerOneTurn;
                break;
            }
        }
    }

    private void checkWinner(int row, int col) {
        int player = board[row][col];
        if (checkDirection(row, col, 1, 0, player) >= 4 || // Check vertical
            checkDirection(row, col, 0, 1, player) >= 4 || // Check horizontal
            checkDirection(row, col, 1, 1, player) >= 4 || // Check diagonal down-right
            checkDirection(row, col, 1, -1, player) >= 4)  // Check diagonal down-left
        {
            winner = (player == 1) ? "Player 1" : "Player 2";
        }
    }

    private int checkDirection(int row, int col, int dRow, int dCol, int player) {
        int count = 1;

        // Check one direction
        int r = row + dRow;
        int c = col + dCol;
        while (isValid(r, c) && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }

        // Check the opposite direction
        r = row - dRow;
        c = col - dCol;
        while (isValid(r, c) && board[r][c] == player) {
            count++;
            r -= dRow;
            c -= dCol;
        }

        return count;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS;
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void resetGame() {
        board = new int[ROWS][COLUMNS];
        playerOneTurn = true;
        winner = null;
    }
}
