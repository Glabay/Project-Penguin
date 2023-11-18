package glabtech.Entity.Player;

import glabtech.Core.Engine.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HUD {

	public static boolean updateEgg = false;

	private Player player;
	private BufferedImage fishCoin;
	private BufferedImage avatar;
	private BufferedImage hearts;

	private BufferedImage eggItem = null;

	private String world = "test world";

	public HUD(Player player, String world) {
		this.player = player;
		this.world = world;
		try {
			fishCoin = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/Pickups/goldfish.gif"));
			avatar = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/HUD/avatar.png"));
			hearts = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/HUD/heart.gif"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void addItem() {
		try {
			eggItem = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/HUD/egg.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D gfx2d) {
		gfx2d.setColor(Color.BLACK);
		gfx2d.fillRect(0, 0, GamePanel.WIDTH, 20);

		if (eggItem == null) {
			gfx2d.fillRect(265, 3, 15, 15);
		} else {
			gfx2d.drawImage(eggItem, 265, 3, null);
		}

		gfx2d.setColor(Color.WHITE);
		gfx2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

		gfx2d.drawString("World: " + world, 2, 11);
		gfx2d.drawImage(avatar, 0, 13, null);
		gfx2d.drawString("x " + player.getLives(), 20, 19);

		for (int i = 0; i < player.getHealth(); i++) {
			gfx2d.drawImage(hearts, 90 + i * 15, 3, null);
		}

		gfx2d.drawString("Score: " + player.getScore(), 190, 15);
		gfx2d.drawString(player.getTimeToString(), 295, 15);

		gfx2d.drawImage(fishCoin, 145, 5, 16, 12, null);
		gfx2d.drawString("x " + player.getFishCoins(), 165, 15);
	}

	public void removeItem() {
		eggItem = null;
	}

	public void update() {
		if (updateEgg) {
			if (player.hasEgg) {
				addItem();
			} else {
				removeItem();
			}
		}
	}
}
