package glabtech.Entity.Items.Misc;

import glabtech.Entity.Animation.Animation;
import glabtech.Entity.Items.Item;
import glabtech.Handlers.Content;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Egg extends Item {

	private BufferedImage[] sprite;

	public Egg(TileMap tm, String eggName) {
		super(tm);
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallingSpeed = 10.0;
		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;

		sprite = getEgg(eggName);

		animation = new Animation();
		animation.setFrames(sprite);
		animation.setDelay(1);

		facingRight = true;
	}

	public void draw(Graphics2D gfx2d) {
		setMapPosition();
		super.draw(gfx2d);
	}

	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		animation.update();
	}

	private BufferedImage[] getEgg(String name) {
		switch (name) {
			case "tutorialEgg":
				return Content.tutorialEgg[0];
			default:
				return Content.tutorialEgg[0];
		}
	}

	private void getNextPosition() {
		if (falling) {
			dy += fallSpeed;
		}
	}
}
