package glabtech.Entity;

import glabtech.Core.Engine.GamePanel;
import glabtech.Entity.Animation.Animation;
import glabtech.Entity.Items.Item;
import glabtech.Entity.NPC.Enemies.Normal.AbominableSnowball;
import glabtech.Entity.Objects.Objects;
import glabtech.Entity.Player.Player;
import glabtech.Handlers.Content;
import glabtech.Handlers.Tiles.Tile;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {

	protected TileMap tileMap;
	protected int tileSize;
	protected double mapX;
	protected double mapY;

	protected double x;
	protected double y;
	protected double dx;
	protected double dy;

	protected int width;
	protected int height;

	protected int cWidth;
	protected int cHeight;

	protected int currRow;
	protected int currCol;
	protected double xDest;
	protected double yDest;
	protected double xTemp;
	protected double yTemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;

	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallingSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	protected Rectangle detectionBox;

	public Entity(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}

	public void calculateCorners(double x, double y) {
		int leftTile = (int) (x - cWidth / 2) / tileSize;
		int rightTile = (int) (x + cWidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cHeight / 2) / tileSize;
		int bottomTile = (int) (y + cHeight / 2 - 1) / tileSize;
		if (topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}

	public void checkTileMapCollision() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;

		xDest = x + dx;
		yDest = y + dy;

		xTemp = x;
		yTemp = y;

		calculateCorners(x, yDest);
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				yTemp = currRow * tileSize + cHeight / 2;
			} else {
				yTemp += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				yTemp = (currRow + 1) * tileSize - cHeight / 2;
			} else {
				yTemp += dy;
			}
		}

		calculateCorners(xDest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xTemp = currCol * tileSize + cWidth / 2;
			} else {
				xTemp += dx;
			}
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xTemp = (currCol + 1) * tileSize - cWidth / 2;
			} else {
				xTemp += dx;
			}
		}
		if (!falling) {
			calculateCorners(x, yDest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}

	public boolean contains(Entity object) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = object.getRectangle();
		return r1.contains(r2);
	}

	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}

	public void draw(Graphics2D gfx2d) {
		if (Content.debug) {
			gfx2d.setColor(Color.RED);
			gfx2d.draw(getRectangle());
		}
		if (facingRight) {
			gfx2d.drawImage(animation.getImage(), (int) (x + mapX - width / 2), (int) (y + mapY - height / 2), width, height, null);
		} else {
			gfx2d.drawImage(animation.getImage(), (int) (x + mapX - width / 2 + width), (int) (y + mapY - height / 2), -width, height, null);

		}
	}

	public int getcHeight() {
		return cHeight;
	}

	public int getcWidth() {
		return cWidth;
	}

	public Rectangle getDetectionBox() {
		return detectionBox;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getRectangle() {
		return new Rectangle(getX() - width / 2, getY() - height / 2, width, height);
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public boolean intersects(Entity object) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = object.getRectangle();
		return r1.intersects(r2);
	}

	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}

	public boolean intersectsItem(Entity object) {
		if (intersects(object)) {
			if (object instanceof Item) { return true; }
			if (object instanceof Objects) { return true; }
		}
		return false;
	}

	public boolean jumpsOn(Player player, Entity object) {
		if (intersects(object)) {
			if (object instanceof AbominableSnowball) {
				AbominableSnowball abs = (AbominableSnowball) object;
				if (player.getX() + player.getWidth() > abs.getX()) {
					if (player.getX() < abs.getX() + abs.getWidth()) {
						if (player.getY() + player.getHeight() > abs.getY()) {
							if (player.falling) {
								player.jump();
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean notOnScreen() {
		return x + mapX + width < 0 || x + mapX - width > GamePanel.WIDTH || y + mapY + height < 0 || y + mapY - height > GamePanel.HEIGHT;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setMapPosition() {
		mapX = tileMap.getx();
		mapY = tileMap.gety();
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setVector(double dX, double dY) {
		dx = dX;
		dy = dY;
	}
}
