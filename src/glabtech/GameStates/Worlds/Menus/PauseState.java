package glabtech.GameStates.Worlds.Menus;

import glabtech.Core.Engine.GamePanel;
import glabtech.GameStates.GameState;
import glabtech.GameStates.GameStateManager;
import glabtech.Handlers.Keys;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PauseState extends GameState {

	private Font font;

	public PauseState(GameStateManager gsm) {
		super(gsm);
		font = new Font("Century Gothic", Font.PLAIN, 14);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 90, 90);
	}

	@Override
	public void eventDead() {}

	@Override
	public void eventFinish() {}

	@Override
	public void eventRestartWorld() {}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE)) {
			gsm.setPaused(false);
		}
		if (Keys.isPressed(Keys.ENTER)) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	@Override
	public void init() {}

	@Override
	public void update() {
		handleInput();
	}
}
