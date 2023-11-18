package glabtech.Entity.Items;

import glabtech.Entity.Entity;
import glabtech.Handlers.Tiles.TileMap;

public class Item extends Entity {

	private boolean gone = false;

	public Item(TileMap tm) {
		super(tm);
	}

	public boolean isItemGone() {
		return gone;
	}

	public void remove() {
		gone = true;
	}

	public void update() {}
}
