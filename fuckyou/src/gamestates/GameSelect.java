package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.SelectButton;
import utilz.LoadSave;

public class GameSelect extends State implements Statemethods {

	private SelectButton[] buttons = new SelectButton[3];
	private BufferedImage backgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;

	public GameSelect(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMGM);

	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);

	}

	private void loadButtons() {
		buttons[0] = new SelectButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamemode.LOCAL);
		buttons[1] = new SelectButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamemode.PVA);
		buttons[2] = new SelectButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamemode.ONLINE);
	}

	@Override
	public void update() {
		for (SelectButton sb : buttons)
			sb.update();
	}

	@Override
	public void draw(Graphics g) {

		g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

		for (SelectButton sb : buttons)
			sb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (SelectButton sb : buttons) {
			if (isIn(e, sb)) {
				sb.setMousePressed(true);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (SelectButton sb : buttons) {
			if (isIn(e, sb)) {
				if (sb.isMousePressed())
					sb.applyGamemode();
				break;
			}
		}

		resetButtons();

	}

	private void resetButtons() {
		for (SelectButton sb : buttons)
			sb.resetBools();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (SelectButton sb : buttons)
			sb.setMouseOver(false);

		for (SelectButton sb : buttons)
			if (isIn(e, sb)) {
				sb.setMouseOver(true);
				break;
			}

	}

	@Override
	public void keyPressed(KeyEvent e) {
//		if (e.getKeyCode() == KeyEvent.VK_ENTER)
//			Gamestate.state = Gamestate.PLAYING;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}

