package com.engine.loaders.resources;

import java.util.ArrayList;

import com.engine.graph.Texture;
import com.engine.loaders.resources.assets.AttackPattern;

public class EnemyObject extends RotMJGameObject{
	public int hp;
	public int def;
	public int size;
	public ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern>();
	public EnemyObject(String name, int id, Texture tex, boolean drawOnGround, boolean moveable, int hp, int def,
					int size, ArrayList<AttackPattern> patterns) {
		super(name, id, tex, drawOnGround, moveable);
		this.hp = hp;
		this.def = def;
		this.size = size;
		this.patterns = patterns;
	}
	
	

}
