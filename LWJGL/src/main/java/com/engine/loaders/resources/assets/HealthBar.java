package com.engine.loaders.resources.assets;

import java.awt.Rectangle;

public class HealthBar {
	public int actualHealth;
	public int maxHealth;
	public int width;
	public int height;
	public Rectangle green;
	public Rectangle red;

	public HealthBar(int x, int y, int width, int height, int health) {
		this.width = width;
		this.height = height;
		actualHealth = health;
		maxHealth = health;
		green = new Rectangle(x, y, width, height);
	}

	public void Hit(int damage) {
		actualHealth -= damage;
		if (actualHealth < 0) actualHealth = 0;
	}

	public void update() {
		float unit = actualHealth * 1.0f / maxHealth * 1.0f;
		if (actualHealth < 0) {
			actualHealth = 0;
		}

		green.width = (int) (unit * width);
		// red.width = (visibleHealth / 100) * width;
	}

}
