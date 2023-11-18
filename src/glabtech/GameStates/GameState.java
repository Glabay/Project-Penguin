package glabtech.GameStates;

import glabtech.Core.Sound.AudioPlayer;
import glabtech.Entity.Animation.Explosion;
import glabtech.Entity.Items.Item;
import glabtech.Entity.Items.Misc.Egg;
import glabtech.Entity.Items.Misc.Fish;
import glabtech.Entity.NPC.NPC;
import glabtech.Entity.Objects.Nest;
import glabtech.Entity.Objects.Objects;
import glabtech.Entity.Player.HUD;
import glabtech.Entity.Player.Player;
import glabtech.GameStates.Worlds.Menus.Background;
import glabtech.Handlers.Keys;
import glabtech.Handlers.Tiles.TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class GameState {

	protected GameStateManager gsm;

	protected boolean blockInput = false;
	protected int eventCount = 0;
	protected ArrayList<Rectangle> tb;
	protected boolean eventStart;
	protected boolean eventFinish;
	protected boolean eventDead;

	protected Background clouds;
	protected Background mountians;
	protected Background background;

	protected Player player;
	protected TileMap tileMap;

	protected ArrayList<NPC> enemies;
	protected ArrayList<Item> items;
	protected ArrayList<Explosion> explosions;
	protected ArrayList<Objects> objects;

	protected HUD hud;
	protected Egg egg;
	protected Fish fish;
	protected Nest nest;
	protected AudioPlayer bgMusic;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	public abstract void draw(Graphics2D gfx2D);

	public void Draw(Graphics2D gfx2d) {
		background.draw(gfx2d);

		tileMap.draw(gfx2d);

		hud.draw(gfx2d);

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(gfx2d);
		}

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).draw(gfx2d);
		}

		for (int i = 0; i < items.size(); i++) {
			items.get(i).draw(gfx2d);
		}

		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).draw(gfx2d);
		}

		player.draw(gfx2d);

	}

	public abstract void eventDead();

	public abstract void eventFinish();

	public abstract void eventRestartWorld();

	public abstract void handleInput();

	public void handleInput(Player player) {
		if (Keys.isPressed(Keys.ESCAPE)) {
			gsm.setPaused(true);
		}
		if (blockInput || player.getLives() == 0) { return; }
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.JUMP]);
		player.setSliding(Keys.keyState[Keys.DOWN]);
		if (Keys.isPressed(Keys.SLAPPING)) {
			player.setSlapping();
		}
		if (Keys.isPressed(Keys.THROWN)) {
			player.setFiring();
		}
	}

	public abstract void init();

	public abstract void update();

	protected void setBlockInput(boolean blockInput) {
		this.blockInput = blockInput;
	}

}
