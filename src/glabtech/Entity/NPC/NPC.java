package glabtech.Entity.NPC;

import glabtech.Entity.Entity;
import glabtech.Handlers.Tiles.TileMap;

public abstract class NPC extends Entity {

	protected int health;
	protected int maxHealth;
	protected int damage;

	protected boolean dead;
	protected boolean flinching;
	protected long flinchTime;

	public NPC(TileMap tm) {
		super(tm);
	}

	public int getDamage() {
		return damage;
	}

	public void hit(int damage) {
		if (dead || flinching) { return; }
		health -= damage;
		if (health < 0) {
			health = 0;
		}
		if (health == 0) {
			dead = true;
		}
		flinching = true;
		flinchTime = System.nanoTime();
	}

	public boolean isDead() {
		return dead;
	}

	public abstract void update();
}