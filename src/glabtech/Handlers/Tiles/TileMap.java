package glabtech.Handlers.Tiles;

import glabtech.Core.Engine.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class TileMap {

	private double x;
	private double y;

	private int yMin;
	private int xMin;
	private int yMax;
	private int xMax;

	private double tween;

	private int[][] map;
	private int tileSize;
	private int tileX;
	private int tileY;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;

	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
		tileX = (int) x + this.tileSize;
		tileY = (int) y + this.tileSize;
	}

	public void draw(Graphics2D g) {
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if (row >= numRows) {
				break;
			}
			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
				if (col >= numCols) {
					break;
				}
				if (map[row][col] == 0) {
					continue;
				}
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public int getWidth() {
		return width;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public void loadMap(String str) {
		try {
			InputStream in = getClass().getResourceAsStream("/cache" + str);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xMin = GamePanel.WIDTH - width;
			xMax = 0;
			yMin = GamePanel.HEIGHT - height;
			yMax = 0;

			String delims = "\\s+";
			for (int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadTiles(String str) {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream("/cache" + str));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];

			BufferedImage subImage;
			for (int col = 0; col < numTilesAcross; col++) {
				subImage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subImage, Tile.NORMAL);
				subImage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subImage, Tile.BLOCKED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPosition(double x, double y) {

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;

	}

	public void setTween(double d) {
		tween = d;
	}

	private void fixBounds() {
		if (x < xMin) {
			x = xMin;
		}
		if (y < yMin) {
			y = yMin;
		}
		if (x > xMax) {
			x = xMax;
		}
		if (y > yMax) {
			y = yMax;
		}
	}

	private int getXT() {
		return tileX / tileSize;
	}

	private int getYT() {
		return tileY / tileSize;
	}

	private int setTileX(int tile) {
		return (int) x + tileSize * tile;
	}

	private int setTileY(int tile) {
		return (int) y + tileSize * tile;
	}

}
