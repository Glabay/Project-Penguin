package glabtech.Entity.Objects;

import glabtech.Entity.Entity;
import glabtech.Handlers.Tiles.TileMap;

public class Objects extends Entity {

	private boolean gone = false;

	public Objects(TileMap tm) {
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
