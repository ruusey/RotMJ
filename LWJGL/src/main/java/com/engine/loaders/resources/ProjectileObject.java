package com.engine.loaders.resources;

import com.engine.graph.Texture;

public class ProjectileObject extends RotMJGameObject{
	public int correctionAngle;
	public int size;
	public ProjectileObject(String name, int id, Texture tex, boolean drawOnGround, boolean moveable,
					int correctionAngle, int size) {
		super(name, id, tex, drawOnGround, moveable);
		this.correctionAngle = correctionAngle;
		this.size = size;
	}
	public ProjectileObject(String name, int id, Texture tex, int corrAngle,
					int size2) {
		super(name, id, tex, true, true);
	}
	

	
}
