package glabtech.GameStates.Worlds.Menus;

import glabtech.Core.Engine.GamePanel;
import glabtech.Entity.Player.PlayerSave;
import glabtech.GameStates.GameState;
import glabtech.GameStates.GameStateManager;
import glabtech.Handlers.Content;
import glabtech.Handlers.Keys;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MainMenu extends GameState {

	private Background bg;

	private BufferedImage[] buttons;

	private final int NORMAL = 0;
	private final int SELECTED = 1;

	private int selectedOption = 0;

	private String[] OPTIONS = { "New Game", "Load Game", "Options", "Credits", "Quit" };

	private Color titleColor;
	private Font titleFont;
	private Font font;

	public MainMenu(GameStateManager gsm) {
		super(gsm);
		try {
			bg = new Background("/Backgrounds/MainMenu.png", 1);
			bg.setVector(0, 0);

			buttons = Content.Buttons[0];

			setTitleColor(new Color(128, 0, 0));
			setTitleFont(new Font("Arial", Font.PLAIN, 10));
			font = new Font("Arial", Font.PLAIN, 12);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D gfx2d) {
		FontMetrics fontMetrics = gfx2d.getFontMetrics();

		gfx2d.setColor(Color.BLACK);

		bg.draw(gfx2d);
		gfx2d.setFont(font);
		gfx2d.drawString("Project Penguins", GamePanel.WIDTH / 2 - fontMetrics.stringWidth("Project Penguins") / 2, 75);
		gfx2d.setFont(font);
		for (int i = 0; i < OPTIONS.length; i++) {
			if (i == selectedOption) {
				gfx2d.setColor(Color.BLACK);
				gfx2d.drawImage(getButton(SELECTED), GamePanel.WIDTH / 2 - fontMetrics.stringWidth(OPTIONS[i]) / 2 - 20, 100 + i * 25, fontMetrics.stringWidth(OPTIONS[i]) + 20, 20, null);
			} else {
				gfx2d.setColor(Color.RED);
				gfx2d.drawImage(getButton(NORMAL), GamePanel.WIDTH / 2 - fontMetrics.stringWidth(OPTIONS[i]) / 2 - 20, 100 + i * 25, fontMetrics.stringWidth(OPTIONS[i]) + 20, 20, null);
			}
			gfx2d.drawString(OPTIONS[i], GamePanel.WIDTH / 2 - fontMetrics.stringWidth(OPTIONS[i]) / 2 - 10, 115 + i * 25);
		}
	}

	@Override
	public void eventDead() {}

	@Override
	public void eventFinish() {}

	@Override
	public void eventRestartWorld() {}

	public BufferedImage getButton(int id) {
		return buttons[id];
	}

	public Color getTitleColor() {
		return titleColor;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ENTER)) {
			select();
		}
		if (Keys.isPressed(Keys.UP)) {
			if (selectedOption > 0) {
				selectedOption--;
			}
		}
		if (Keys.isPressed(Keys.DOWN)) {
			if (selectedOption < OPTIONS.length - 1) {
				selectedOption++;
			}
		}
	}

	@Override
	public void init() {}

	public void setTitleColor(Color titleColor) {
		this.titleColor = titleColor;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	@Override
	public void update() {
		handleInput();
		bg.update();
	}

	private void select() {
		switch (selectedOption) {
			case 0: // Start option
				PlayerSave.init();
				gsm.setState(GameStateManager.TUTORIAL_MAP);
				break;
			case 1: // Load Game
				break;
			case 2: // Options
				break;
			case 3: // Credits option
				System.out.println("Credits option pressed");
				break;
			case 4: // Quit Option
				System.exit(0);
				break;
			default:
				System.out.println("Unknown option pressed: " + selectedOption);
				break;
		}
	}
}
