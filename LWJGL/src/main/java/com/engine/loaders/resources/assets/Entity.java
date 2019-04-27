package com.engine.loaders.resources.assets;

import java.awt.Rectangle;

import org.joml.Vector3f;

import com.engine.graph.Texture;
import com.engine.items.GameItem;
import com.engine.loaders.resources.RotMJGameObject;



public class Entity extends RotMJGameObject{
	public Entity(String name, int id, Texture tex, boolean drawOnGround, boolean moveable) {
		super(name, id, tex, drawOnGround, moveable);
		// TODO Auto-generated constructor stub
	}
	public Vector3f pos;
	public Vector3f vel;
	public Rectangle colBox;
	public int width;
	public int height;
	public Tile tile;
	
	//4111 1111 1111 1111
	//8121 2121 2121 2121
						
	
	
}
