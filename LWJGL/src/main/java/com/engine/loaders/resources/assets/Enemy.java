package com.engine.loaders.resources.assets;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.engine.loaders.resources.EnemyObject;

public class Enemy extends Entity implements Lootable{
	public Enemy(String name, int id, com.engine.graph.Texture tex, boolean drawOnGround, boolean moveable) {
		super(name, id, tex, drawOnGround, moveable);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Shot> shots = new ArrayList<Shot>();
	public AttackPattern current;
	int frame;
	long lastFire;
	long lastPatternChange;
	int patternIndex = 0;
	boolean firing=true;
	public HealthBar hp;
	public EnemyObject en;
	
	

	public void calculateDrops() {
		com.engine.graph.Texture lootTexture = null;
		try {
			lootTexture = new com.engine.graph.Texture("135.png", 2, 9);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Lootbag loot = new Lootbag("test",1,lootTexture,true,true,null);
		loot.contents[0]=Client.loader.weapons.get("Coral Bow");
		Client.loot.add(loot);
		
	}

	public void dropLoot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void death() {
		// TODO Auto-generated method stub
		
	}
}
