package glabtech.Entity.Player;

import glabtech.Core.Sound.AudioPlayer;
import glabtech.Entity.Entity;
import glabtech.Entity.Animation.Animation;
import glabtech.Entity.Items.Item;
import glabtech.Entity.Items.Misc.Egg;
import glabtech.Entity.Items.Misc.Fish;
import glabtech.Entity.NPC.NPC;
import glabtech.Entity.Objects.Nest;
import glabtech.Entity.Objects.Objects;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Player extends Entity {

	private static final int IDLE = 0;

	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int SLIDING = 4;
	private static final int SNOWBALL = 5;
	private static final int WING_SLAP = 6;
	private static final int CROUCHING = 4;

	private ArrayList<NPC> enemies;
	private int health;
	private int maxHealth;

	private int lives;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean knockback;
	private boolean flinching;

	private int flinchCount;
	private int score;
	private int fishCoins;
	private boolean firing;
	public boolean hasEgg = false;
	public boolean finishedLevel = false;

	private int fireCost;
	private int snowBallDamage;

	private int snowBallsThrown;
	private ArrayList<SnowBall> snowBalls;

	private long time;
	private boolean slapping;
	private int slapDamage;
	private int slapRange;
	private boolean sliding;
	private boolean crouching;
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 1, 4, 1, 2, 4, 2, 5, 1 };

	private HashMap<String, AudioPlayer> sfx;

	public Player(TileMap tm) {
		super(tm);

		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallingSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		fire = maxFire = 2500;

		fireCost = 200;
		snowBallDamage = 5;
		snowBallsThrown = 0;
		snowBalls = new ArrayList<SnowBall>();

		slapDamage = 8;
		slapRange = 40;

		lives = 3;
		health = 3;
		maxHealth = 3;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/Player/playersprites.GIF"));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					if (i != WING_SLAP) {
						bi[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spriteSheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}

				}
				sprites.add(bi);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		animation = new Animation();

		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(-1);

		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));

		detectionBox = new Rectangle(getRectangle().x + getRectangle().width / 2 - cWidth, getRectangle().y + getRectangle().height / 2 - cHeight, cWidth * 3, cHeight * 3);

	}

	public void checkAttack(ArrayList<NPC> enemies) {
		for (int i = 0; i < enemies.size(); i++) {
			NPC npc = enemies.get(i);
			if (slapping) {
				if (facingRight) {
					if (npc.getX() > x && npc.getX() < x + slapRange && npc.getY() > y - height / 2 && npc.getY() < y + height / 2) {
						npc.hit(slapDamage);
					}
				} else {
					if (npc.getX() < x && npc.getX() > x - slapRange && npc.getY() > y - height / 2 && npc.getY() < y + height / 2) {
						npc.hit(slapDamage);
					}
				}

			}
			for (int j = 0; j < snowBalls.size(); j++) {
				if (snowBalls.get(j).intersects(npc)) {
					npc.hit(snowBallDamage);
					snowBalls.get(j).setHit();
					break;
				}
			}
			if (jumpsOn(this, npc)) {
				npc.hit(slapDamage);
			} else if (intersects(npc)) {
				getHitPlayer();
			}
		}
	}

	public void checkItems(ArrayList<Item> items) {
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if (intersectsItem(item)) {
				pickUpItem(item);
			}
		}
	}

	public void checkObjects(ArrayList<Objects> object) {
		for (int i = 0; i < object.size(); i++) {
			Objects obj = object.get(i);
			if (intersectsItem(obj)) {
				if (obj instanceof Nest) {
					if (hasEgg) {
						finishedLevel = true;
						hasEgg = false;
					} else {
						finishedLevel = false;
					}
				} else {
					System.err.println("NOT EGG!");
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();

		for (int i = 0; i < snowBalls.size(); i++) {
			snowBalls.get(i).draw(g);
		}

		if (flinching && !knockback) {
			if (flinchCount % 10 < 5) { return; }
		}

		super.draw(g);
		g.setColor(Color.GREEN);
		g.draw(getDetectionBox());
	}

	public void gainLife() {
		lives++;
	}

	public ArrayList<NPC> getEnemies() {
		return enemies;
	}

	public int getFire() {
		return fire;
	}

	public int getFishCoins() {
		return fishCoins;
	}

	public int getHealth() {
		return health;
	}

	public void getHitPlayer() {
		if (flinching) { return; }
		stop();
		health--;
		if (health < 0) {
			health = 0;
		}
		flinching = true;
		flinchCount = 0;
		if (facingRight) {
			dx = -1;
		} else {
			dx = 1;
		}
		dy = -3;
		hasEgg = false;
		knockback = true;
		falling = true;
		jumping = false;
	}

	public int getLives() {
		return lives;
	}

	public int getMaxFire() {
		return maxFire;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getScore() {
		return score;
	}

	public long getTime() {
		return time;
	}

	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) (time % 3600 / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}

	public void increaseFishCoin() {
		fishCoins++;
		if (fishCoins >= 100) {
			resetFishCoins();
			gainLife();
		}
	}

	public void increaseScore(int score) {
		this.score += score;
	}

	public void init(ArrayList<NPC> enemies) {
		setEnemies(enemies);
	}

	public boolean isDead() {
		return dead;
	}

	public void jump() {
		sfx.get("jump").play();
		dy = jumpStart;
		falling = true;
	}

	public void loseLife() {
		lives--;
		if (lives < 0) {
			lives = 0;
		}
	}

	public void reset() {
		health = getMaxHealth();
		facingRight = true;
		currentAction = IDLE;
		stop();
	}

	public void resetFishCoins() {
		fishCoins = 0;
	}

	public void setDead() {
		health = 0;
		stop();
	}

	public void setEnemies(ArrayList<NPC> enemies) {
		this.enemies = enemies;
	}

	public void setFiring() {
		firing = true;
	}

	public void setFishCoin(int fishCoins2) {
		fishCoins = fishCoins2;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setSlapping() {
		slapping = true;
	}

	public void setSliding(boolean b) {
		sliding = b;
		crouching = b;
	}

	public void setTime(long t) {
		time = t;
	}

	public void stop() {
		left = right = up = down = flinching = jumping = false;
	}

	public void update() {
		// time--;
		if (time <= 0) {
			setDead();
		}
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);
		detectionBox = new Rectangle(getX() - width, getY() - height, cWidth * 3, cHeight * 3);

		if (currentAction == WING_SLAP) {
			if (animation.hasPlayedOnce()) {
				slapping = false;
			}
		}

		if (currentAction == SNOWBALL) {
			if (animation.hasPlayedOnce()) {
				firing = false;
			}
		}

		fire += 1500;
		if (fire > maxFire) {
			fire = maxFire;
			if (snowBallsThrown < 3) {
				if (firing && currentAction != SNOWBALL) {
					if (fire > fireCost) {
						SnowBall sb = new SnowBall(tileMap, facingRight);
						sb.setPosition(x, y);
						snowBalls.add(sb);
						snowBallsThrown++;
					}
				}
			}
		}
		for (int i = 0; i < snowBalls.size(); i++) {
			snowBalls.get(i).update();
			if (snowBalls.get(i).shouldRemove()) {
				snowBalls.remove(i);
				i--;
				snowBallsThrown--;
			}
		}
		if (flinching) {
			flinchCount++;
			if (flinchCount > 120) {
				flinching = false;
			}
		}

		if (slapping) {
			if (currentAction != WING_SLAP) {
				sfx.get("scratch").play();
				currentAction = WING_SLAP;
				animation.setFrames(sprites.get(WING_SLAP));
				animation.setDelay(3);
				width = 60;
			}
		} else if (firing) {
			if (currentAction != SNOWBALL) {
				currentAction = SNOWBALL;
				animation.setFrames(sprites.get(SNOWBALL));
				animation.setDelay(2);
				width = 30;
			}
		} else if (dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(1);
				width = 30;
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		} else if (left || right) {
			if (sliding) {
				if (currentAction != SLIDING) {
					currentAction = SLIDING;
					animation.setFrames(sprites.get(SLIDING));
					animation.setDelay(3);
					width = 30;
					height = 30;
					maxSpeed = 3.5;
				}
			} else {
				if (currentAction != WALKING) {
					currentAction = WALKING;
					animation.setFrames(sprites.get(WALKING));
					animation.setDelay(4);
					width = 30;
					height = 30;
					maxSpeed = 1.6;
				}
			}
		} else if (crouching) {
			if (currentAction != CROUCHING) {
				currentAction = CROUCHING;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(4);
				width = 30;
				height = 30;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(-1);
				width = 30;
			}
		}
		animation.update();
		if (currentAction != WING_SLAP && currentAction != SNOWBALL) {
			if (right) {
				facingRight = true;
			}
			if (left) {
				facingRight = false;
			}
		}

	}

	private void getNextPosition() {
		if (knockback) {
			dy += fallSpeed * 2;
			if (!falling) {
				knockback = false;
			}
			return;
		}
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		if ((currentAction == WING_SLAP || currentAction == SNOWBALL) && !(jumping || falling)) {
			dx = 0;
		}

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

			if (getY() >= 420 && !isDead()) {
				setDead();
			}
		}

	}

	private void pickUpItem(Item item) {
		item.remove();
		if (item instanceof Egg) {
			hasEgg = true;
			increaseScore(1000);
			HUD.updateEgg = true;
		} else if (item instanceof Fish) {
			increaseScore(10);
			increaseFishCoin();
		} else {
			System.err.println("Pickup Item not supported");
		}
	}

}