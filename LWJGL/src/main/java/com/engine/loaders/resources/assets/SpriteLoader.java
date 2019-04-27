package com.engine.loaders.resources.assets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteLoader{
	static BufferedImage spriteSheet;
	public static HashMap<String, BufferedImage> spritesMap = new HashMap<String, BufferedImage>();
	int width;
	int height;
	int rows;
	int columns;
	BufferedImage[] sprites;
	public static final int TILE_SIZE = 8;

	public SpriteLoader(int width, int height, int rows, int columns,
			String path) {
		this.width = width;
		this.height = height;
		this.rows = rows;
		this.columns = columns;

		loadSprites(path);

	}

	public BufferedImage getSprite(String key, int xGrid, int yGrid) {

		return spritesMap.get(key).getSubimage(xGrid * TILE_SIZE,
				yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	public BufferedImage getSprite(Texture tex) {

		return spritesMap.get(tex.fileName).getSubimage(tex.x * TILE_SIZE,
				tex.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}

	public void loadSprites(String path) {
		File[] files = new File(path).listFiles();

		for (File f : files) {
			System.out.println(f.getName());
			try {
				BufferedImage res = ImageIO.read(f);
			
				spritesMap.put(f.getName(), res);
			} catch (IOException e) {

				continue;
			}
		}

	}

}