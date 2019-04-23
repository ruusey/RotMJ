package com.engine.graph.particles;

import java.util.List;

import com.engine.items.GameItem;

public interface IParticleEmitter {
	void cleanup();
  
  Particle getBaseParticle();
  
  List<GameItem> getParticles();
}
