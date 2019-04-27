package com.engine.loaders.resources.assets;

import com.engine.graph.Texture;
import com.engine.loaders.resources.RotMJGameObject;

public class Lootbag extends RotMJGameObject{
	public Item[] contents = new Item[8];

	public Lootbag(String name, int id, Texture tex, boolean drawOnGround, boolean moveable, Item[] contents) {
		super(name, id, tex, drawOnGround, moveable);
		this.contents = contents;
	}
	

	
}
