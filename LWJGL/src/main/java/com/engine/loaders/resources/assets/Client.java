package com.engine.loaders.resources.assets;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.joml.Vector3f;

import com.engine.loaders.resources.EnemyObject;
import com.engine.loaders.resources.PlayerObject;

public final class Client  {
	// OUR LEVEL IN TILES
	public static ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
	public static ArrayList<ArrayList<Tile>> visibleTiles = new ArrayList<ArrayList<Tile>>();
	// ALL ITEMS IN THE GAME (WIP)
	public static ArrayList<Entity> gameObject = new ArrayList<Entity>();
	// ALL ENEMIES AND VISIBLE ENEMIES
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static ArrayList<Enemy> visibleEnemies = new ArrayList<Enemy>();
	public static ArrayList<Lootbag> loot = new ArrayList<Lootbag>();
	// THE TILE WIDTH AND HEIGHT
	static int tileSize = 64;

	// CAMERA LOCATION
	int camX;
	int camY;

	// GAME ENTITIES
	public static Enemy e;
	public static Player p;
	public static SpriteLoader sl;
	public static XMLParse loader;

	
	public void setup() {

		// MAKE THE TILE SIZE MAGNIFIED
		loader = new XMLParse("src/main/java/xml/items.xml");
		tileSize *= 1;

		sl = new SpriteLoader(64, 64, 5, 10, "src/main/java/images");
		// CONSTRUCT TILE MAP
		for (int x = 0; x < 100; x++) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			for (int y = 0; y < 100; y++) {
				if (random(1) > 0.9) {

					row.add(new Tile(loader.getGameObject("Wall2"), x * tileSize, y * tileSize, tileSize, tileSize,
							color(0, 0, 255), true, this));
				} else {

					row.add(new Tile(loader.getGameObject("Wall1"), x * tileSize, y * tileSize, tileSize, tileSize,
							random(255), false, this));
				}

			}
			tiles.add(row);

		}
		

		// LOAD SPRITES IN DIRECTORY
		PlayerObject pl = loader.getPlayerObject("Archer");
		p = new Player(pl, 500, 500, 64);

		for (int i = 0; i < 5; i++) {
			EnemyObject eo = loader.getEnemyObject("Enemy1");
			Enemy en = new Enemy(eo, random(1000), random(1000), new PVector(0, 0), 64, this);
			enemies.add(en);
		}
		// NEW GUI (WIP)
	
	}


	

	

	
	// CONVERT TO BUFFERD IMAGE AND PRESERVER ARGB TRANSPARENCY
	public static BufferedImage scale(BufferedImage i, int newW, int newH) {
		Image toolkitImage = i.getScaledInstance(newW, newH, Image.SCALE_FAST);
		int width = toolkitImage.getWidth(null);
		int height = toolkitImage.getHeight(null);

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImage.getGraphics();
		g.drawImage(toolkitImage, 0, 0, null);
		g.dispose();
		return newImage;
	}

	// FLIP A BUFFEREDIMAGE OVER ITS HORIZONTAL AXIS
	public BufferedImage flipHorz(BufferedImage i) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-i.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter((BufferedImage) i, null);
	}

	public static Tile getTile(Vector3f pos) {
		if (pos.x < 0 || pos.y < 0)
			return null;
		return tiles.get((int) (pos.x / tileSize)).get((int) (pos.y / tileSize));

	}

	public static ArrayList<Tile> getAdjacentTiles(Tile t) {
		ArrayList<Tile> res = new ArrayList<Tile>();
		Vector3f indexT = indexInTiles(t);
		int x = (int) indexT.x;
		int y = (int) indexT.y;

		if (x > 0 && y > 0) {
			res.add(tiles.get(x).get(y));

		}
		if (x + 1 < tiles.size())
			res.add(tiles.get(x + 1).get(y));
		if (x - 1 > -1)
			res.add(tiles.get(x - 1).get(y));
		if (y + 1 < tiles.size())
			res.add(tiles.get(x).get(y + 1));
		if (y - 1 > -1)
			res.add(tiles.get(x).get(y - 1));
		if (x + 1 < tiles.size() && y + 1 < tiles.size())
			res.add(tiles.get(x + 1).get(y + 1));
		if (x - 1 > -1 && y - 1 > -1)
			res.add(tiles.get(x - 1).get(y - 1));
		if (x - 1 > -1 && y + 1 < tiles.size())
			res.add(tiles.get(x - 1).get(y + 1));
		if (x + 1 < tiles.size() && y - 1 > -1)
			res.add(tiles.get(x + 1).get(y - 1));

		return res;

	}

	public static Vector3f indexInTiles(Tile t) {
		int x = 0, y = 0;
		for (ArrayList<Tile> t1 : tiles) {
			if (t1.contains(t)) {
				x = tiles.indexOf(t1);
				y = t1.indexOf(t);
			}

		}
		return new Vector3f(x, y,0);
	}

	public boolean tilesContains(Tile t, ArrayList<ArrayList<Tile>> toTest) {
		for (ArrayList<Tile> t1 : toTest) {
			for (Tile t2 : t1) {
				if (t2.equals(t))
					return true;
			}
		}
		return false;
	}

}