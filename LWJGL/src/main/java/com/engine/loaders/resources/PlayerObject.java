package com.engine.loaders.resources;

import com.engine.graph.Texture;

public class PlayerObject extends RotMJGameObject{
	public int[] slotTypes;
	public int[] equipment;
	public int maxHp;
	public int hp;
	public int maxAtt;
	public int att;
	public int maxDef;
	public int def;
	public int maxSpeed;
	public int spd;
	public int maxDex;
	public int dex;
	public int maxVit;
	public int vit;
	public PlayerObject(String name, int id, Texture tex, boolean drawOnGround, boolean moveable, int[] slotTypes,
					int[] equipment, int maxHp, int hp, int maxAtt, int att, int maxDef, int def, int maxSpeed, int spd,
					int maxDex, int dex, int maxVit, int vit) {
		super(name, id, tex, drawOnGround, moveable);
		this.slotTypes = slotTypes;
		this.equipment = equipment;
		this.maxHp = maxHp;
		this.hp = hp;
		this.maxAtt = maxAtt;
		this.att = att;
		this.maxDef = maxDef;
		this.def = def;
		this.maxSpeed = maxSpeed;
		this.spd = spd;
		this.maxDex = maxDex;
		this.dex = dex;
		this.maxVit = maxVit;
		this.vit = vit;
	}
	
	
	
	
	

}
