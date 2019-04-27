package com.engine.loaders.resources;

import com.engine.graph.Texture;
import com.engine.items.GameItem;

public class RotMJGameObject extends GameItem{
	public boolean drawOnGround;
	public boolean moveable;
	
	public RotMJGameObject(String name, int id, Texture tex, boolean drawOnGround,
			boolean moveable) {
		super();
		this.drawOnGround = drawOnGround;
		this.moveable = moveable;
	}
	
	
	
}
