package glabtech.Handlers;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Content {

	public static boolean debug = true;

	/**
	 * LOAD NPC sprites
	 */
	public static BufferedImage[][] SpikySnail = load("/Sprites/Enemies/SpikySnail.GIF", 30, 30);
	public static BufferedImage[][] abominableSnowball = load("/Sprites/Enemies/Snowball.GIF", 30, 30);

	/**
	 * LOAD MISC GFX sprites
	 */
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/Explosion.gif", 30, 30);
	public static BufferedImage[][] Buttons = load("/Sprites/Buttons/Buttons.gif", 320, 130);

	/**
	 * LOAD ITEM sprites
	 */
	public static BufferedImage[][] tutorialEgg = load("/Sprites/Pickups/tutorialEgg.gif", 30, 30);
	public static BufferedImage[][] tutorialNest = load("/Sprites/Pickups/nest.gif", 30, 30);
	public static BufferedImage[][] fishCoin = load("/Sprites/Pickups/goldfish.gif", 64, 64);

	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream("/cache" + s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}

}
