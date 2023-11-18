package glabtech.Entity.Player;

import glabtech.Entity.Items.Item;

public class Abilities {

	public static int HEART_POINT = 0;

	public static int CURRENT_ABILITY = -1;

	public static int getCurrentAbility() {
		return CURRENT_ABILITY;
	}

	public Abilities(int ability) {
		CURRENT_ABILITY = ability;
	}

	public void appendAbility(Item item) {
		System.out.println("Appending Ability for item: " + item.getClass().getName());
	}
}
