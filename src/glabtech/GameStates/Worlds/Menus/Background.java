package glabtech.GameStates.Worlds.Menus;

import glabtech.Core.Engine.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {

	private BufferedImage image;

	private double x;
	private double y;
	private double dx;
	private double dy;

	private double moveScale;

	public Background(String s, double ms) {

		try {
			image = ImageIO.read(getClass().getResourceAsStream("/cache" + s));
			moveScale = ms;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D gfx) {
		gfx.drawImage(image, (int) x, (int) y, GamePanel.WIDTH, GamePanel.HEIGHT, null);

		if (x < 0) {
			gfx.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		}
		if (x > 0) {
			gfx.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		}
	}

	public void setPosition(double x, double y) {
		this.x = x * moveScale % GamePanel.WIDTH;
		this.y = y * moveScale % GamePanel.HEIGHT;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void update() {
		x += dx;
		y += dy;
	}
}
