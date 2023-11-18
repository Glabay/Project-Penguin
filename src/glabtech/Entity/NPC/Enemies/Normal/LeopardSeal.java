package glabtech.Entity.NPC.Enemies.Normal;

import glabtech.Entity.Animation.Animation;
import glabtech.Entity.NPC.NPC;
import glabtech.Entity.Player.Player;
import glabtech.Handlers.Content;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class LeopardSeal extends NPC {

	private BufferedImage[] sprites;
	private Player player;

	public LeopardSeal(TileMap tm, Player player) {
		super(tm);

		this.player = player;

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallingSpeed = 10.0;
		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;
		jumpStart = -4.8;
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
		gfx2d.setColor(Color.GREEN);
		gfx2d.drawRect(getX() - width, getY() - height, cWidth * 3, cHeight * 3);
	}

	public void jump() {
		dy = -3.5f;
		falling = true;
	}

	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		detectionBox = new Rectangle(getX() - width, getY() - height, cWidth * 3, cHeight * 3);

		if (getDetectionBox().intersects(player.getDetectionBox())) {
			jumping = true;
		}

		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		}
		animation.update();
	}

	private void getNextPosition() {
		if (jumping && !falling) {
			jump();
		}

		if (falling) {
			dy += fallSpeed;
			if (dy > 0) {
				jumping = false;
			}
			if (dy < 0 && !jumping) {
				dy += stopJumpSpeed;
			}
			if (dy > maxFallingSpeed) {
				dy = maxFallingSpeed;
			}
		}
	}
}
