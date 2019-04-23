package com.engine.items;

import org.joml.Vector4f;

import com.engine.graph.Material;
import com.engine.graph.Mesh;
import com.engine.graph.Texture;
import com.engine.loaders.assimp.StaticMeshesLoader;

public class SkyBox extends GameItem {

  public SkyBox(String objModel, String textureFile) throws Exception {
      super();
      Mesh skyBoxMesh = StaticMeshesLoader.load(objModel, "")[0];
      Texture skyBoxtexture = new Texture(textureFile);
      skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
      setMesh(skyBoxMesh);
      setPosition(0, 0, 0);
  }

  public SkyBox(String objModel, Vector4f colour) throws Exception {
      super();
      Mesh skyBoxMesh = StaticMeshesLoader.load(objModel, "", 0)[0];
      Material material = new Material(colour, 0);
      skyBoxMesh.setMaterial(material);
      setMesh(skyBoxMesh);
      setPosition(0, 0, 0);
  }
}
