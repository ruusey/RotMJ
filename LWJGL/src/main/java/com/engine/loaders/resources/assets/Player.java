package com.engine.loaders.resources.assets;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.engine.graph.Texture;
import com.engine.loaders.resources.PlayerObject;
import com.engine.loaders.resources.WeaponObject;

public class Player extends Entity {

	public boolean firing;
	ArrayList<Shot> shots = new ArrayList<Shot>();
	public HealthBar hp;
	public long lastCheck;
	public long updateCheck;
	public WeaponObject w;
	public PlayerObject p;
	public Player(String name, int id, Texture tex, boolean drawOnGround, boolean moveable, boolean firing,
					ArrayList<Shot> shots, HealthBar hp, long lastCheck, long updateCheck, WeaponObject w,
					PlayerObject p) {
		super(name, id, tex, drawOnGround, moveable);
		this.firing = firing;
		this.shots = shots;
		this.hp = hp;
		this.lastCheck = lastCheck;
		this.updateCheck = updateCheck;
		this.w = w;
		this.p = p;
	}

	
}
