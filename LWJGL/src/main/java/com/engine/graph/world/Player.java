package com.engine.graph.world;

import org.joml.Vector3f;

import com.engine.graph.Camera;
import com.engine.graph.Mesh;
import com.engine.items.GameItem;

public class Player extends GameItem{
	

	public Player() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Player(Mesh mesh) {
		super(mesh);
		// TODO Auto-generated constructor stub
	}

	public Player(Mesh[] meshes) {
		super(meshes);
		// TODO Auto-generated constructor stub
	}
	public void movePosition(float offsetX, float offsetY, float offsetZ) {
	      if ( offsetZ != 0 ) {
	    	  super.getPosition().x += (float)Math.sin(Math.toRadians( super.getRotation().y)) * -1.0f * offsetZ;
	    	  super.getPosition().z += (float)Math.cos(Math.toRadians( super.getRotation().y)) * offsetZ;
	      }
	      if ( offsetX != 0) {
	    	  super.getPosition().x += (float)Math.sin(Math.toRadians( super.getRotation().y - 90)) * -1.0f * offsetX;
	    	  super.getPosition().z += (float)Math.cos(Math.toRadians( super.getRotation().y - 90)) * offsetX;
	      }
	      super.getPosition().y += offsetY;
	  }

	  public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		  super.getRotation().x += offsetX;
		  super.getRotation().y += offsetY;
		  super.getRotation().z += offsetZ;
	  }
	
}
