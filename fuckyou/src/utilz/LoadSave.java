package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {

	public static final String PLAYER_ATLAS = "playersprites.png";
	public static final String LEVEL_ATLAS = "outsidesprites.png";
	public static final String LEVEL_ONE_DATA = "levelone_data_long.png";
	public static final String MENU_BUTTONS = "p.png";
	public static final String MENU_BACKGROUND = "menubackground.png";
	public static final String PAUSE_BACKGROUND = "pausemenu.png";
	public static final String SOUND_BUTTONS = "soundbutton.png";
	public static final String URM_BUTTONS = "urmbuttons.png";
	public static final String VOLUME_BUTTONS = "volumebuttons.png";
	public static final String MENU_BACKGROUND_IMG = "backgroundmenu.png";
	public static final String PLAYING_BG_IMG = "playingbg_img.png";
	public static final String BIG_CLOUDS = "bigclouds.png";
	public static final String SMALL_CLOUDS = "smallclouds.png";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream isjka = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(isjka);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				isjka.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

}