package glabtech.Entity.Player;

public class PlayerSave {

	private static int lives = 3;
	private static int health = 3;
	private static long time = 0;
	private static int score = 0;
	private static int fishCoin;

	private static String level = "Tutorial";

	public static int getFishCoins() {
		return fishCoin;
	}

	public static int getHealth() {
		return health;
	}

	public static String getLevel() {
		return level;
	}

	public static int getLives() {
		return lives;
	}

	public static int getScore() {
		return score;
	}

	public static long getTime() {
		return time;
	}

	public static void init() {
		lives = 3;
		health = 3;
		time = 0;
	}

	public static void setFishCoin(int fishCoins) {
		PlayerSave.fishCoin = fishCoins;
	}

	public static void setHealth(int i) {
		health = i;
	}

	public static void setLevel(String level) {
		PlayerSave.level = level;
	}

	public static void setLives(int i) {
		lives = i;
	}

	public static void setScore(int score) {
		PlayerSave.score = score;
	}

	public static void setTime(long t) {
		time = t;
	}

}
