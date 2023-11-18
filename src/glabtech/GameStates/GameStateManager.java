package glabtech.GameStates;

import glabtech.GameStates.Worlds.Menus.LevelSplasScreen;
import glabtech.GameStates.Worlds.Menus.MainMenu;
import glabtech.GameStates.Worlds.Menus.PauseState;
import glabtech.GameStates.Worlds.One.WorldOneLevelOne;
import glabtech.GameStates.Worlds.One.WorldOneLevelThree;
import glabtech.GameStates.Worlds.One.WorldOneLevelTwo;
import glabtech.GameStates.Worlds.Tutorial.TutorialMap;

import java.awt.Graphics2D;

public class GameStateManager {

	public static final int NUMGAMESTATES = 6;

	public static final int MENUSTATE = 0;
	public static final int TUTORIAL_MAP = 1;
	public static final int LEVEL_SPLASH_SCREEN = 2;
	public static final int WORLD_ONE_LEVEL_ONE = 3;
	public static final int WORLD_ONE_LEVEL_TWO = 4;
	public static final int WORLD_ONE_LEVEL_THREE = 5;

	GameState[] gameStates;

	private int currentState;
	private PauseState pauseState;
	private boolean paused;

	public GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];
		pauseState = new PauseState(this);
		paused = false;
		currentState = MENUSTATE;
		loadState(currentState);
	}

	public void draw(Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch (Exception e) {}
	}

	public void setPaused(boolean b) {
		paused = b;
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		gameStates[currentState].init();
	}

	public void update() {
		if (paused) {
			pauseState.update();
			return;
		}
		if (gameStates[currentState] != null) {
			gameStates[currentState].update();
		}
	}

	private void loadState(int state) {
		switch (state) {
			case MENUSTATE:
				gameStates[state] = new MainMenu(this);
				break;
			case TUTORIAL_MAP:
				gameStates[state] = new TutorialMap(this);
				break;
			case LEVEL_SPLASH_SCREEN:
				gameStates[state] = new LevelSplasScreen(this);
				break;
			case WORLD_ONE_LEVEL_ONE:
				gameStates[state] = new WorldOneLevelOne(this);
				break;
			case WORLD_ONE_LEVEL_TWO:
				gameStates[state] = new WorldOneLevelTwo(this);
				break;
			case WORLD_ONE_LEVEL_THREE:
				gameStates[state] = new WorldOneLevelThree(this);
				break;

			default:
				gameStates[state] = new MainMenu(this);
				break;

		}
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}
}
