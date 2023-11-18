package glabtech.Handlers;

import java.awt.event.KeyEvent;

public class Keys {

	public static final int NUM_KEYS = 16;

	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int SLAPPING = 4;
	public static int THROWN = 5;
	public static int JUMP = 6;
	public static int BUTTON4 = 7;
	public static int ENTER = 8;
	public static int ESCAPE = 9;

	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}

	public static void keySet(int i, boolean b) {
		if (i == KeyEvent.VK_UP) {
			keyState[UP] = b;
		} else if (i == KeyEvent.VK_LEFT) {
			keyState[LEFT] = b;
		} else if (i == KeyEvent.VK_DOWN) {
			keyState[DOWN] = b;
		} else if (i == KeyEvent.VK_RIGHT) {
			keyState[RIGHT] = b;
		} else if (i == KeyEvent.VK_V) {
			keyState[SLAPPING] = b;
		} else if (i == KeyEvent.VK_B) {
			keyState[THROWN] = b;
		} else if (i == KeyEvent.VK_SPACE) {
			keyState[JUMP] = b;
		} else if (i == KeyEvent.VK_F) {
			keyState[BUTTON4] = b;
		} else if (i == KeyEvent.VK_ENTER) {
			keyState[ENTER] = b;
		} else if (i == KeyEvent.VK_ESCAPE) {
			keyState[ESCAPE] = b;
		}
	}

	public static void update() {
		for (int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}

}