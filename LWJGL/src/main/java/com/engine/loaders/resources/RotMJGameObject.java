package com.engine.loaders.resources;

import com.engine.graph.Texture;
import com.engine.items.GameItem;

public class RotMJGameObject extends GameItem{
	public boolean drawOnGround;
	public boolean moveable;
	public String name;
	public int id;
	public Texture tex;
	public RotMJGameObject(String name, int id, Texture tex, boolean drawOnGround,
			boolean moveable) {
		super();
		this.name=name;
		this.id=id;
		this.tex=tex;
		this.drawOnGround = drawOnGround;
		this.moveable = moveable;
	}
	
	
	
}
