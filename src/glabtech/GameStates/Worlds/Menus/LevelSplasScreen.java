package glabtech.GameStates.Worlds.Menus;

import glabtech.Core.Engine.GamePanel;
import glabtech.Entity.Player.PlayerSave;
import glabtech.GameStates.GameState;
import glabtech.GameStates.GameStateManager;
import glabtech.Handlers.Keys;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class LevelSplasScreen extends GameState {

	private BufferedImage avatar;
	private BufferedImage image;
	private BufferedImage hearts;
	private Font titleFont;

	public LevelSplasScreen(GameStateManager gsm) {
		super(gsm);

		try {
			avatar = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/HUD/avatar.png"));
			image = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/Player/SplashScreen.gif"));
			hearts = ImageIO.read(getClass().getResourceAsStream("/cache/Sprites/HUD/heart.gif"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setTitleFont(new Font("Arial", Font.PLAIN, 14));
	}

	@Override
	public void draw(Graphics2D gfx2d) {
		gfx2d.setColor(Color.BLACK);
		gfx2d.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		gfx2d.setColor(Color.WHITE);
		gfx2d.setFont(titleFont);
		gfx2d.drawString("Congratulations!", 100, 50);
		gfx2d.drawString("=== Next Level ===", 90, 70);

		gfx2d.setFont(new Font("Arial", Font.PLAIN, 10));
		gfx2d.drawImage(avatar, 10, 230, null);
		gfx2d.drawString("Score: " + PlayerSave.getScore(), 215, 235);
		gfx2d.drawString("x " + PlayerSave.getLives(), 25, 235);

		for (int i = 0; i < PlayerSave.getHealth(); i++) {
			gfx2d.drawImage(hearts, 50 + i * 15, 222, null);
		}

		gfx2d.drawString("World: " + PlayerSave.getLevel(), 130, 100);
		gfx2d.drawImage(image, 100, 110, null);
	}

	@Override
	public void eventDead() {}

	@Override
	public void eventFinish() {}

	@Override
	public void eventRestartWorld() {}

	public Font getTitleFont() {
		return titleFont;
	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ENTER)) {
			if (PlayerSave.getLevel().equals("1-1")) {
				gsm.setState(GameStateManager.WORLD_ONE_LEVEL_ONE);
				return;
			}
			if (PlayerSave.getLevel().equals("1-2")) {
				gsm.setState(GameStateManager.WORLD_ONE_LEVEL_TWO);
				return;
			}
			if (PlayerSave.getLevel().equals("1-3")) {
				gsm.setState(GameStateManager.WORLD_ONE_LEVEL_THREE);
				return;
			}
			System.out.println("Error loading next world..... loading MainMenu instead.");
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	@Override
	public void init() {}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	@Override
	public void update() {
		handleInput();
	}

}
