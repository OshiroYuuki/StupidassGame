package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Gamemode;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;

public class SelectButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = B_WIDTH / 2;
	private Gamemode mode;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	public SelectButton(int xPos, int yPos, int rowIndex, Gamemode mode) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.mode = mode;
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);

	}

	private void loadImgs() {
		imgs = new BufferedImage[3];
		System.out.print(LoadSave.MENU_BUTTONS);
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
	}

	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}
 
	public void applyGamemode() {
		Gamemode.mode = mode;
		Gamestate.state = Gamestate.TICTAC;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

}
