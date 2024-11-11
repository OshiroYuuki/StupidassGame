package gamestates;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;
import ui.PauseOverlay;
import utilz.Connect4AI;
import utilz.LoadSave;

public class TicTac extends State implements Statemethods {

    private int[][] board; // 0 for empty, 1 for player 1, 2 for player 2 or AI
    private boolean isPlayerTurn; // true if it's player 1's turn
    private String winner; // Track the winner
    private final int ROWS = 6;
    private final int COLUMNS = 7;
    private final int boardWidth = 700;
    private final int boardHeight = 600;
    private final int marginX = (Game.GAME_WIDTH - boardWidth) / 2;
    private final int marginY = Game.GAME_HEIGHT - boardHeight - 150;
    private Connect4AI ai; // AI instance
    private BufferedImage player1Sprite, player2Sprite;
    private BufferedImage boardSprite;
    private BufferedImage temp;// Sprites for players
    private int animRow, animCol; // Position of the animated falling piece
    private boolean animating; // True if a piece is currently animating
    private int animationY; // Y position of the animated piece
    private boolean gamePaused;
    private PauseOverlay pauseOverlay;
    
    private static final String ONLINE_GAME_URL = "https://www.youtube.com/watch?v=EB-NJtNERBQ"; // Replace with actual URL


    public TicTac(Game game) {
        super(game);
        board = new int[ROWS][COLUMNS];
        isPlayerTurn = true; // Player 1 starts
        winner = null;
        ai = new Connect4AI();
        pauseOverlay = new PauseOverlay(this);
        gamePaused = false;
        loadSprites();
        animating = false;
        
    }
    
    private void openOnlineGame() {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI(ONLINE_GAME_URL));
            } else {
                System.out.println("Unable to open the URL. BROWSE action not supported.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void loadSprites() {
        player1Sprite = LoadSave.GetSpriteAtlas(LoadSave.P1);
        player2Sprite = LoadSave.GetSpriteAtlas(LoadSave.P2);
        boardSprite = LoadSave.GetSpriteAtlas(LoadSave.BOARD);
        temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    @Override
    public void update() {
    	if(!gamePaused) {
	    	if (Gamemode.mode == Gamemode.ONLINE) {
	            openOnlineGame();
	            Gamestate.state = Gamestate.MENU; // Return to main menu or another state
	        }
	        
	        if (Gamemode.mode == Gamemode.PVA && !isPlayerTurn) {
	            // AI's turn (after animation completes)
	            int aiMoveColumn = ai.getAIMove(board);
	            animRow = getAvailableRow(aiMoveColumn);
	            animCol = aiMoveColumn;
	            animating = true;
	        }
	        
	        if (animating) {
	            // Update animation
	            animationY += 10; // Speed of the falling animation
	            
	            if (animationY >= marginY + animRow * (boardHeight / ROWS)) {
	                board[animRow][animCol] = isPlayerTurn ? 1 : 2; // Place the piece on the board
	                animating = false;
	                animationY = 0;
	
	                // Check for winner after the piece lands
	                checkWinner(animCol, isPlayerTurn ? 1 : 2);
	                if (winner != null) {
	                    resetGame();
	                    Gamestate.state = Gamestate.MENU;
	                    return;
	                }
	                
	                isPlayerTurn = !isPlayerTurn;
	             
	            }
	        }
    	} else {
    		pauseOverlay.update();
    	}
    }

    @Override
    public void draw(Graphics g) {
        // Draw background
    	g.drawImage(temp, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

    	g.setColor(new Color(210, 180, 140)); // RGB for tan color
        g.fillRect(marginX, marginY, boardWidth, boardHeight);
        

        int cellWidth = boardWidth / COLUMNS;
        int cellHeight = boardHeight / ROWS;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int x = marginX + col * cellWidth + cellWidth / 2 - player1Sprite.getWidth() / 2;
                int y = marginY + row * cellHeight + cellHeight / 2 - player1Sprite.getHeight() / 2;
                if (board[row][col] == 1) {
                    g.drawImage(player1Sprite, x, y, null);
                } else if (board[row][col] == 2) {
                    g.drawImage(player2Sprite, x, y, null);
                }
            }
        }

        // Draw animated falling piece if in progress
        if (animating) {
            int x = marginX + animCol * cellWidth + cellWidth / 2 - player1Sprite.getWidth() / 2;
            BufferedImage sprite = isPlayerTurn ? player1Sprite : player2Sprite;
            g.drawImage(sprite, x, animationY, null);
        }
        
        g.drawImage(boardSprite, marginX, marginY, boardWidth, boardHeight, null);
        
        
        if (gamePaused)
			pauseOverlay.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	if (gamePaused) {
			pauseOverlay.mousePressed(e);
    	} else {
	        if (winner != null || animating) return; // Ignore clicks if game is over or animation is in progress
	        if (Gamemode.mode == Gamemode.PVA && !isPlayerTurn) return; // Wait for the AI's turn in PLAYER_VS_AI mode
	
	        // Determine which column was clicked
	        int col = (e.getX() - marginX) / (boardWidth / COLUMNS);
	        if (col < 0 || col >= COLUMNS) return;
	
	        // Find available row in the selected column
	        int row = getAvailableRow(col);
	        if (row == -1) return; // Column is full
	
	        // Start the animation for this move
	        animRow = row;
	        animCol = col;
	        animating = true;
	        animationY = 0;
    	}
    }

    private int getAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) return row;
        }
        return -1;
    }

    private void checkWinner(int col, int player) {
        int row = getLastRowInColumn(col);
        if (row == -1) return;

        if (checkDirection(row, col, 1, 0, player) >= 4 || // Vertical
            checkDirection(row, col, 0, 1, player) >= 4 || // Horizontal
            checkDirection(row, col, 1, 1, player) >= 4 || // Diagonal down-right
            checkDirection(row, col, 1, -1, player) >= 4)  // Diagonal down-left
        {
            winner = (player == 1) ? "Player 1" : "Player 2";
        }
    }

    private int getLastRowInColumn(int col) {
        for (int row = 0; row < ROWS; row++) {
            if (board[row][col] != 0) return row;
        }
        return -1;
    }

    private int checkDirection(int row, int col, int dRow, int dCol, int player) {
        int count = 1;
        int r = row + dRow, c = col + dCol;
        while (isValid(r, c) && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        r = row - dRow; c = col - dCol;
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
    public void mouseReleased(MouseEvent e) {
    	if (gamePaused)
			pauseOverlay.mouseReleased(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    	if (gamePaused)
			pauseOverlay.mouseMoved(e);
    }
    public void mouseDragged(MouseEvent e) {
		if (gamePaused)
			pauseOverlay.mouseDragged(e);
	}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
    	switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			gamePaused = !gamePaused;
			break;
		}
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    public void resetGame() {
        board = new int[ROWS][COLUMNS];
        isPlayerTurn = true;
        winner = null;
        animating = false;
    }
    
    public void unpauseGame() {
		gamePaused = false;
	}
}
