package com.engine.loaders.resources.assets;

public class Object {
	public String name;
	public int id;
	public Texture tex;
	public Object(String name, int id, Texture tex) {
		super();
		this.name = name;
		this.id = id;
		this.tex = tex;
	}
	public Object(String name2, int id2, com.engine.graph.Texture tex2) {
		// TODO Auto-generated constructor stub
	}
}
