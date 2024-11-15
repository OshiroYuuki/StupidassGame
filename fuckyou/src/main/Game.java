package main;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseListener;

import gamestates.GameSelect;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.TicTac;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private TicTac ticTac;
	private GameSelect gameSelect;
	private Menu menu;
	private boolean paused = false;
	
	public final static float SCALE = 1.5f;
	public final static int GAME_WIDTH = 1920;
	public final static int GAME_HEIGHT = 1080;

	public Game() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		startGameLoop();

	}

	private void initClasses() {
		menu = new Menu(this);
		gameSelect = new GameSelect(this);
		ticTac = new TicTac(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case GAMESELECT:
			gameSelect.update();
			break;
		case TICTAC:
            ticTac.update();
            break;
		case OPTIONS:
		case QUIT:
		default:
			System.exit(0);
			break;

		}
	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case GAMESELECT:
			gameSelect.draw(g);
			break;
		case TICTAC:
            ticTac.draw(g);
            break;
		default:
			break;
		}
	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if(!paused) {
				if (deltaU >= 1) {
					update();
					updates++;
					deltaU--;
				}
	
				if (deltaF >= 1) {
					gamePanel.repaint();
					frames++;
					deltaF--;
				}
	
				if (System.currentTimeMillis() - lastCheck >= 1000) {
					lastCheck = System.currentTimeMillis();
					System.out.println("FPS: " + frames + " | UPS: " + updates);
					frames = 0;
					updates = 0;
	
				}
			}
		}

	}

	public void windowFocusLost() {
		paused = true;
	}
	
	public void windowFocusGained() {
		paused = false;	
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public GameSelect getGameSelect() {
		return gameSelect;
	}


	public TicTac getTicTac() {
		return ticTac;
	}

	
}