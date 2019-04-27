package com.engine.loaders.resources.assets;

import org.joml.Vector3f;

import com.engine.graph.Texture;
import com.engine.loaders.resources.ProjectileObject;
import com.engine.loaders.resources.RotMJGameObject;

public class Shot extends RotMJGameObject {

	public Shot(String name, int id, Texture tex, boolean drawOnGround, boolean moveable) {
		super(name, id, tex, drawOnGround, moveable);
		// TODO Auto-generated constructor stub
	}
	int dmg;
	Vector3f source;
	int diameter;
	public float angle;
	float mag;
	float range;
	public boolean isHit;

	
}
