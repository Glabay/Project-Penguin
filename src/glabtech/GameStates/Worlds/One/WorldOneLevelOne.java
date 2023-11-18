package glabtech.GameStates.Worlds.One;

import glabtech.Core.Engine.GamePanel;
import glabtech.Entity.Animation.Explosion;
import glabtech.Entity.Items.Item;
import glabtech.Entity.Items.Misc.Egg;
import glabtech.Entity.NPC.NPC;
import glabtech.Entity.NPC.Enemies.Normal.AbominableSnowball;
import glabtech.Entity.NPC.Enemies.Normal.SpikySnail;
import glabtech.Entity.Objects.Nest;
import glabtech.Entity.Objects.Objects;
import glabtech.Entity.Player.HUD;
import glabtech.Entity.Player.Player;
import glabtech.Entity.Player.PlayerSave;
import glabtech.GameStates.GameState;
import glabtech.GameStates.GameStateManager;
import glabtech.GameStates.Worlds.Menus.Background;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class WorldOneLevelOne extends GameState {

	public WorldOneLevelOne(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void draw(Graphics2D gfx2d) {
		super.Draw(gfx2d);
	}

	@Override
	public void eventDead() {
		eventCount++;
		if (eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if (eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if (eventCount >= 120) {
			if (player.getLives() <= 1) {
				player.reset();
				gsm.setState(GameStateManager.MENUSTATE);
				return;
			} else {
				eventDead = false;
				setBlockInput(false);
				eventCount = 0;
				player.loseLife();
				reset();
			}
		}
	}

	@Override
	public void eventFinish() {
		eventCount++;
		if (eventCount == 1) {
			player.stop();
			// bgMusic.stop();
		} else if (eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if (eventCount == 180) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			PlayerSave.setScore(player.getScore());
			PlayerSave.setFishCoin(player.getFishCoins());
			PlayerSave.setLevel("1-2");
			gsm.setState(GameStateManager.LEVEL_SPLASH_SCREEN);
		}
	}

	@Override
	public void eventRestartWorld() {
		eventCount++;
		if (eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		if (eventCount > 60) {
			tb.get(0).x += 6;
			tb.get(0).y += 4;
			tb.get(0).width -= 12;
			tb.get(0).height -= 8;
		}
	}

	@Override
	public void handleInput() {
		super.handleInput(player);
	}

	@Override
	public void init() {
		eventCount = 0;

		background = new Background("/Backgrounds/grassbg1.GIF", 0.1);

		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/snowtileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		player = new Player(tileMap);
		player.setPosition(100, 200);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setScore(PlayerSave.getScore());
		player.setFishCoin(PlayerSave.getFishCoins());
		player.setTime(9000);

		hud = new HUD(player, "World 1-1");

		tb = new ArrayList<Rectangle>();

		enemies = new ArrayList<NPC>();
		explosions = new ArrayList<Explosion>();
		populateEnemies();

		items = new ArrayList<Item>();
		populateEgg();

		objects = new ArrayList<Objects>();
		populateObjects();
	}

	@Override
	public void update() {
		handleInput();

		if (player.getHealth() == 0 || player.getY() > tileMap.getHeight()) {
			eventDead = true;
			setBlockInput(true);
		}

		if (player.finishedLevel) {
			eventFinish = true;
			player.finishedLevel = false;
		}

		if (eventStart) {
			eventRestartWorld();
		}
		if (eventDead) {
			eventDead();
		}
		if (eventFinish) {
			eventFinish();
		}

		player.update();
		hud.update();

		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		background.setPosition(tileMap.getx(), tileMap.gety());
		player.checkAttack(enemies);
		player.checkItems(items);
		player.checkObjects(objects);

		if (!player.hasEgg && egg.isItemGone()) {
			populateEgg();
		}

		// UPDATE ENEMIES
		for (int i = 0; i < enemies.size(); i++) {
			NPC npc = enemies.get(i);
			npc.update();
			if (npc.isDead()) {
				enemies.remove(i);
				i--;
				player.increaseScore(100);
				explosions.add(new Explosion(npc.getX(), npc.getY()));
			}
		}

		// UPDATE EXPLOSIONS
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}

		// UPDATE ITEMS
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			item.update();
			if (item.isItemGone()) {
				items.remove(i);
				player.increaseScore(1000);
				i--;
			}
		}

		// UPDATE OBJECTS
		for (int i = 0; i < objects.size(); i++) {
			Objects object = objects.get(i);
			object.update();
			if (object.isItemGone()) {
				objects.remove(i);
				i--;
			}
		}
	}

	private void populateEgg() {
		items.clear();

		Point[] EGG = new Point[] { new Point(2925, 80) };
		for (Point element : EGG) {
			egg = new Egg(tileMap, "tutorialEgg");
			egg.setPosition(element.x, element.y);
			items.add(egg);
		}
	}

	private void populateEnemies() {
		enemies.clear();

		SpikySnail snailSpike;
		AbominableSnowball abs;

		Point[] abominable = new Point[] { new Point(790, 200), new Point(1390, 200), new Point(1665, 200), new Point(1755, 200), new Point(1850, 200) };

		for (Point element : abominable) {
			abs = new AbominableSnowball(tileMap);
			abs.setPosition(element.x, element.y);
			enemies.add(abs);
		}

		Point[] snail = new Point[] { new Point(3375, 120) };

		for (Point element : snail) {
			snailSpike = new SpikySnail(tileMap);
			snailSpike.setPosition(element.x, element.y);
			enemies.add(snailSpike);
		}
	}

	private void populateObjects() {
		objects.clear();

		Point[] NEST = new Point[] { new Point(3400, 145) };
		for (Point element : NEST) {
			nest = new Nest(tileMap);
			nest.setPosition(element.x, element.y);
			objects.add(nest);
		}

	}

	private void reset() {
		player.setPosition(100, 200);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(player.getLives());
		player.setScore(PlayerSave.getScore());
		player.setTime(9000);
		player.hasEgg = false;
		populateEnemies();
		populateEgg();
		populateObjects();
		setBlockInput(false);
		eventCount = 0;
		eventStart = true;
	}
}
