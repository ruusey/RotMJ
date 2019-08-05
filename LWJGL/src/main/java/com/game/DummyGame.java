package com.game;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import com.engine.IGameLogic;
import com.engine.MouseInput;
import com.engine.Scene;
import com.engine.SceneLight;
import com.engine.Window;
import com.engine.graph.Camera;
import com.engine.graph.HeightMapMesh;
import com.engine.graph.Material;
import com.engine.graph.Mesh;
import com.engine.graph.Renderer;
import com.engine.graph.Texture;
import com.engine.graph.Transformation;
import com.engine.graph.anim.AnimGameItem;
import com.engine.graph.anim.Animation;
import com.engine.graph.lights.DirectionalLight;
import com.engine.graph.lights.PointLight;
import com.engine.graph.particles.FlowParticleEmitter;
import com.engine.graph.particles.Particle;
import com.engine.graph.weather.Fog;
import com.engine.graph.world.Player;
import com.engine.items.GameItem;
import com.engine.items.SkyBox;
import com.engine.items.Terrain;
import com.engine.loaders.assimp.AnimMeshesLoader;
import com.engine.loaders.assimp.StaticMeshesLoader;
import com.engine.loaders.resources.EnemyObject;
import com.engine.loaders.resources.assets.SpriteLoader;
import com.engine.loaders.resources.assets.XMLParse;
import com.util.Box2D;
import com.util.OBJLoader;
import static org.lwjgl.stb.STBImage.*;

public class DummyGame implements IGameLogic {

	private static final float MOUSE_SENSITIVITY = 0.2f;

	private final Vector3f cameraInc;

	private final Vector3f playerInc;

	private final Renderer renderer;

	private Camera camera;

	private Scene scene;

	private static final float CAMERA_POS_STEP = 0.2f;

	private float angleInc;

	private float lightAngle;

	private boolean firstTime;

	private boolean sceneChanged;

	private Animation animation;
	private XMLParse parser;
	private SpriteLoader spriteLoader;
	private FlowParticleEmitter particleEmitter;
	private MouseInput input;
	private Player player;
	private Hud hud;
	private MouseBoxSelectionDetector selectDetector;
	private CameraBoxSelectionDetector itemSelector;
	private boolean leftButtonPressed;
	private GameItem[] gameItems;

	public DummyGame() {
		hud = new Hud();
		renderer = new Renderer();
		input = new MouseInput();
		itemSelector = new CameraBoxSelectionDetector();
		playerInc = new Vector3f(0.0f, 0.0f, 0.0f);
		cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
		angleInc = 0;
		lightAngle = 90;
		firstTime = true;
	}

	@Override
	public void init(Window window) throws Exception {
		//parser = new XMLParse("src/main/resources/xml/items.xml");
		//spriteLoader = new SpriteLoader(64, 64, 5, 10, "src/main/resources/textures/images");

		hud.init(window);
		input.init(window);
		renderer.init(window);

		scene = new Scene();

		leftButtonPressed = false;

		float reflectance = 1f;

		float blockScale = 2f;
		float skyBoxScale = 50.0f;
		float extension = 2.0f;

		float startx = extension * (-skyBoxScale + blockScale);
		float startz = extension * (skyBoxScale - blockScale);
		float starty = 0.0f;
		float inc = blockScale * 2;

		float posx = startx;
		float posz = startz;
		float incy = 1.0f;
		ByteBuffer buf;
		int width;
		int height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			URL url = Texture.class.getResource("/textures/heightmap.png");
			File file = Paths.get(url.toURI()).toFile();
			String filePath = file.getAbsolutePath();
			buf = stbi_load(filePath, w, h, channels, 4);
			if (buf == null) {
				throw new Exception("Image file not loaded: " + stbi_failure_reason());
			}

			width = w.get();
			height = h.get();
		}

		int instances = height * width;
		Mesh[] itemMesh = StaticMeshesLoader.load("src/main/resources/models/game/env/8x8.obj",
						"src/main/resources/models/game/env");
		gameItems = new GameItem[instances];
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				GameItem gameItem = new GameItem(itemMesh);
				gameItem.setScale(5);
				int rgb = HeightMapMesh.getRGB(i, j, width, buf);

				if (Math.random() > 0.95f) {
					incy = 2;
				} else {
					incy = 0;
				}
				gameItem.setPosition(posx, starty + incy * 2, posz);
				//gameItem.setScale(0.2f);
				gameItem.setTextPos(0);
				gameItems[i * width + j] = gameItem;

				posx += inc;
			}
			posx = startx;
			posz -= inc;
		}
		
//		int maxParticles = 200;
//        Vector3f particleSpeed = new Vector3f(0, 0, 0);
//        particleSpeed.mul(2.5f);
//        long ttl = 4000;
//        long creationPeriodMillis = 300;
//        float range = 0.2f;
//        float scale = 1.0f;
//        Mesh partMesh = OBJLoader.loadMesh("/models/particle.obj", maxParticles);
//        Texture particleTexture = new Texture("/textures/particle_anim.png", 4, 4);
//        Material partMaterial = new Material(particleTexture, reflectance);
//        partMesh.setMaterial(partMaterial);
//        Particle particle = new Particle(partMesh, particleSpeed, ttl, 100);
//        particle.setScale(scale);
//        particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
//        particleEmitter.setActive(false);
//        particleEmitter.setPositionRndRange(range);
//        particleEmitter.setSpeedRndRange(range);
//        particleEmitter.setAnimRange(10);
//        this.scene.setParticleEmitters(new FlowParticleEmitter[]{particleEmitter});

//        Mesh[] itemMesh = StaticMeshesLoader.load("src/main/resources/models/game/env/101.obj", "src/main/resources/models/game/env");
//        
//        
//        GameItem gameItem = new GameItem(itemMesh);
//        gameItem.setTextPos(1);
//        gameItem.setPosition(0, 10, 5);
//        scene.setGameItems(new GameItem[] {gameItem});
		scene.setGameItems(gameItems);

		Mesh[] playerMesh = StaticMeshesLoader.load("src/main/resources/models/game/Wizard.obj",
						"src/main/resources/models/game");

		player = new Player(playerMesh);
		player.setScale(0.4f);
		player.setPosition(0, 6, 10);
		player.setRotation(new Quaternionf(180,180,180,0));
		scene.setGameItems(new GameItem[] { player });

		camera = new Camera(player);

		// Shadows
		scene.setRenderShadows(true);

		// Fog
		Vector3f fogColour = new Vector3f(0.5f, 0.5f, 0.5f);
		// scene.setFog(new Fog(true, fogColour, 0.02f));

		// Setup SkyBox

		SkyBox skyBox = new SkyBox("src/main/resources/models/skybox.obj", new Vector4f(0.65f, 0.65f, 0.65f, 1.0f));
		skyBox.setScale(skyBoxScale);
		scene.setSkyBox(skyBox);

		// Setup Lights
		setupLights();

	}

	private void setupLights() {
		SceneLight sceneLight = new SceneLight();
		scene.setSceneLight(sceneLight);

		// Ambient Light
		sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
		sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

		// Directional Light
		float lightIntensity = 1.0f;
		Vector3f lightDirection = new Vector3f(0, 1, 1);
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
		sceneLight.setDirectionalLight(directionalLight);
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		sceneChanged = false;
		cameraInc.set(0, 0, 0);
		playerInc.set(0, 0, 0);

		if (window.isKeyPressed(GLFW_KEY_W)) {
			sceneChanged = true;
			playerInc.z = -1;
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			sceneChanged = true;
			playerInc.z = 1;
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			sceneChanged = true;
			playerInc.x = -1;
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			sceneChanged = true;
			playerInc.x = 1;
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) {

			sceneChanged = true;
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			sceneChanged = true;
			cameraInc.y = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Q)) {
			sceneChanged = true;
			// playerInc.w = 1;
		} else if (window.isKeyPressed(GLFW_KEY_E)) {
			sceneChanged = true;
			// playerInc.w = -1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) {

			sceneChanged = true;
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			sceneChanged = true;
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			sceneChanged = true;
			angleInc -= 0.05f;
		} else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			sceneChanged = true;
			angleInc += 0.05f;
		} else {
			angleInc = 0;
		}
		if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			
			
		}else {
			
		}

	}

	@Override
	public void update(float interval, MouseInput mouseInput, Window window) {
		Vector2f rotVec = null;
		if (mouseInput.isRightButtonPressed()) {
			// Update camera based on mouse
			rotVec = mouseInput.getDisplVec();

			// camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y *
			// MOUSE_SENSITIVITY, 0);
			// player.movePosition(rotVec.y * MOUSE_SENSITIVITY/4f ,0f, rotVec.x *
			// MOUSE_SENSITIVITY/4f);
			sceneChanged = true;
		}
		Vector3f pos = new Vector3f(player.getPosition());
		
		GameItem currentBlock = this.itemSelector.selectGameItemMove(gameItems, pos,
						new Vector3f(0f, 0, 0f));

		Vector3f ploc = player.getPosition();
		//player.getRotation().y=playerInc.y* CAMERA_POS_STEP;

		// camera.moveRotation(-cameraInc.x* CAMERA_POS_STEP/4f, -cameraInc.y*
		// CAMERA_POS_STEP/4f, 0f);
		// camera.movePosition(cameraInc.x* CAMERA_POS_STEP, cameraInc.y*
		// CAMERA_POS_STEP, cameraInc.z* CAMERA_POS_STEP);

		// camera.getRotation().y=playerInc.y* CAMERA_POS_STEP;;
		// player.setSelected(true);
		lightAngle += angleInc;
		if (lightAngle < 0) {
			lightAngle = 0;
		} else if (lightAngle > 180) {
			lightAngle = 180;
		}
		float zValue = (float) Math.cos(Math.toRadians(lightAngle));
		float yValue = (float) Math.sin(Math.toRadians(lightAngle));
		Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
		lightDirection.x = 0;
		lightDirection.y = yValue;
		lightDirection.z = zValue;
		lightDirection.normalize();
		if(currentBlock!=null) {
			Box2D bb = getBoundingBox(currentBlock);
			Box2D pBox = getBoundingBox(player);
			//if (bb.contains(player.getPosition().x, player.getPosition().z) || pBox.contains(bb.x,bb.y)) {
				camera.move(input);
				camera.setRotation(camera.getRoll(), camera.getYaw(), camera.getPitch());
				camera.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);

				//player.setRotation(player.getRotation().setFromUnnormalized(camera.getViewMatrix().invert()));
				Transformation trans = new Transformation();
				Matrix4f newRot = trans.buildModelMatrix(player);
				player.getPosition().sub(playerInc);
			//} else {
				
			//}
		}
		camera.move(input);
		camera.setRotation(-camera.getRoll(), camera.getYaw(), camera.getPitch());
		camera.setPosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);

		player.setRotation(player.getRotation().setFromUnnormalized(camera.getViewMatrix().invert()));
		Transformation trans = new Transformation();
		Matrix4f newRot = trans.buildModelMatrix(player);

		player.getPosition().add(playerInc.mulDirection(newRot));
		

		// player.setPosition(player.getPosition().x+playerInc.x * CAMERA_POS_STEP,
		// player.getPosition().y+playerInc.y * CAMERA_POS_STEP,
		// player.getPosition().z+playerInc.z * CAMERA_POS_STEP);
		camera.updateViewMatrix();

		boolean aux = false;
		if (aux && !this.leftButtonPressed
						&& this.selectDetector.selectGameItem(gameItems, window, mouseInput.getCurrentPos(), camera)) {
			this.hud.incCounter();
			GameItem go = this.selectDetector.selectMovementTile(gameItems, window, mouseInput.getCurrentPos(), camera);
			Thread t = new Thread(() -> {

				while (true) {

					Vector3f newPos = new Vector3f();
					player.getPosition().lerp(go.getPosition(), 0.001f, newPos);
//            	   Vector3f dif = newPos.sub(player.getPosition());
//            	   dif.normalize(CAMERA_POS_STEP);
					System.out.println(go.getPosition().toString());
					player.setPosition(newPos.x, newPos.y, newPos.z);
					// camera.movePosition(dif.x, dif.y, dif.z);
					// player.getPosition().z=-go.getPosition().z+0.5f;
					sceneChanged = true;
					if (player.getPosition().distance(go.getPosition()) <= 0.1f) {
						player.movePosition(0, 0.4f, 0);
						System.out.println(player.getPosition().distance(go.getPosition()));
						sceneChanged = true;
						break;
					}

				}
			});
			t.start();

		}
		this.leftButtonPressed = aux;
	}

	private Box2D getBoundingBox(GameItem terrainBlock) {
		float scale = terrainBlock.getScale();
		Vector3f position = terrainBlock.getPosition();
		float topLeftX = HeightMapMesh.STARTX * scale + position.x;
		float topLeftZ = HeightMapMesh.STARTZ * scale + position.z;
		float width = Math.abs(HeightMapMesh.STARTX * 2) * scale;
		float height = Math.abs(HeightMapMesh.STARTZ * 2) * scale;
		Box2D boundingBox = new Box2D(topLeftX, topLeftZ, width, height);
		return boundingBox;
	}

	@Override
	public void render(Window window) {
		if (firstTime) {
			sceneChanged = true;
			firstTime = false;
		}
		renderer.render(window, camera, scene, sceneChanged);
		hud.render(window);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();

		scene.cleanup();
	}
}