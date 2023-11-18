package glabtech.Entity.Player;

import glabtech.Entity.Entity;
import glabtech.Entity.Animation.Animation;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class SnowBall extends Entity {

	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	public SnowBall(TileMap tm, boolean right) {
		super(tm);
		facingRight = right;
		moveSpeed = 1.8;
		width = 15;
		height = 15;
		cWidth = 7;
		cHeight = 7;

		if (right) {
			dx = moveSpeed;
		} else {
			dx = -moveSpeed;
		}

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/Player/snowball.GIF"));

			sprites = new BufferedImage[4];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}
			hitSprites = new BufferedImage[3];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(i * width, height, width, height);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(3);
	}

	public void draw(Graphics2D gfx2d) {
		setMapPosition();
		super.draw(gfx2d);
	}

	public void setHit() {
		if (hit) { return; }
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(3);
		dx = 0;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void update() {
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (dx == 0 && !hit) {
			setHit();
		}
		animation.update();
		if (hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
}
