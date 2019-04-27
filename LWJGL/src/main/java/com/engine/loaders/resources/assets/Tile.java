package com.engine.loaders.resources.assets;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.engine.graph.Texture;
import com.engine.loaders.resources.RotMJGameObject;
import com.engine.loaders.resources.WallObject;

public class Tile extends Entity{

	public Tile(String name, int id, Texture tex, boolean drawOnGround, boolean moveable) {
		super(name, id, tex, drawOnGround, moveable);
		// TODO Auto-generated constructor stub
	}
	public float color;
	public boolean takesCol;
	boolean isWall;
	public List<Entity> entities = new ArrayList<Entity>();
	
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
}
