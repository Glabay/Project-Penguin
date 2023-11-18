package glabtech.Entity.NPC.Enemies.Normal;

import glabtech.Entity.Animation.Animation;
import glabtech.Entity.NPC.NPC;
import glabtech.Handlers.Content;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpikySnail extends NPC {

	private BufferedImage[] sprites;

	public SpikySnail(TileMap tm) {
		super(tm);

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallingSpeed = 10.0;
		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;
		health = maxHealth = 2;
		damage = 1;

		sprites = Content.SpikySnail[0];

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(3);
		left = true;
		facingRight = false;
	}

	public void draw(Graphics2D gfx2d) {
		setMapPosition();
		super.draw(gfx2d);
	}

	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		}
		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		animation.update();
	}

	private void getNextPosition() {
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		if (falling) {
			dy += fallSpeed;
		}
	}
}
