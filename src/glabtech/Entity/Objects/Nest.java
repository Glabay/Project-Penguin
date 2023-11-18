package glabtech.Entity.Objects;

import glabtech.Entity.Animation.Animation;
import glabtech.Handlers.Content;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Nest extends Objects {

	private BufferedImage[] sprite;

	public Nest(TileMap tm) {
		super(tm);
		facingRight = right;
		width = 30;
		height = 30;
		cWidth = 30;
		cHeight = 15;

		sprite = Content.tutorialNest[0];

		animation = new Animation();
		animation.setFrames(sprite);
		animation.setDelay(1);
	}

	public void draw(Graphics2D gfx2d) {
		setMapPosition();
		super.draw(gfx2d);
	}

	public void update() {
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		animation.update();
	}
}
