package com.engine.loaders.resources;

import com.engine.graph.Texture;

public class WallObject extends RotMJGameObject{
	Texture topTex;

	public WallObject(String name, int id, Texture tex, boolean drawOnGround, boolean moveable, Texture topTex) {
		super(name, id, tex, drawOnGround, moveable);
		this.topTex = topTex;
	}

	
	
}
