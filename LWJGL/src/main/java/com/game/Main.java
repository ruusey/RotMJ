package com.game;


import com.engine.GameEngine;
import com.engine.IGameLogic;
import com.engine.Window;

public class Main {
	 
	 public static void main(String[] args) {
     try {
         boolean vSync = true;
         IGameLogic gameLogic = new DummyGame();
         Window.WindowOptions opts = new Window.WindowOptions();
         opts.cullFace = true;
         opts.showFps = true;
         opts.compatibleProfile = true;
         opts.antialiasing = true;
         opts.frustumCulling = true;
         GameEngine gameEng = new GameEngine("RotMGJ", vSync, opts, gameLogic);
         gameEng.start();
     } catch (Exception excp) {
         excp.printStackTrace();
         System.exit(-1);
     }
 }
}
