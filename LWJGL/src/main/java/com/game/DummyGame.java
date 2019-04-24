package com.game;


import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import org.joml.Quaternionf;
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
import com.engine.loaders.assimp.AnimMeshesLoader;
import com.engine.loaders.assimp.StaticMeshesLoader;
import com.util.OBJLoader;
import static org.lwjgl.stb.STBImage.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;
    
    private final Quaternionf playerInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private static final float CAMERA_POS_STEP = 0.2f;

    private float angleInc;

    private float lightAngle;

    private boolean firstTime;

    private boolean sceneChanged;

    private Animation animation;

    private AnimGameItem animItem;
    
    private Player player;
    private Hud hud;
    private MouseBoxSelectionDetector selectDetector;
    private CameraBoxSelectionDetector itemSelector;
    private boolean leftButtonPressed;
    private GameItem[] gameItems;
    public DummyGame() {
    	hud = new Hud();
        renderer = new Renderer();
        camera = new Camera();
        playerInc = new Quaternionf(0.0f, 0.0f, 0.0f,0f);
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 90;
        firstTime = true;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        hud.init(window);
        scene = new Scene();
        leftButtonPressed = false;

        scene = new Scene();

        float reflectance = 1f;

        float blockScale = 0.5f;
        float skyBoxScale = 100.0f;
        float extension = 2.0f;

        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float inc = blockScale * 2;

        float posx = startx;
        float posz = startz;
        float incy = 0.0f;

        selectDetector = new MouseBoxSelectionDetector();
        itemSelector = new CameraBoxSelectionDetector();
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
        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj", instances);
        mesh.setBoundingRadius(1);
        Texture texture = new Texture("/textures/terrain_textures.png", 2, 1);
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);
        gameItems = new GameItem[instances+1];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameItem gameItem = new GameItem(mesh);
                gameItem.setScale(blockScale);
                int rgb = HeightMapMesh.getRGB(i, j, width, buf);
                incy = rgb / (10 * 255 * 255);
                gameItem.setPosition(posx, starty , posz);
                int textPos = Math.random() > 0.5f ? 0 : 1;
                gameItem.setTextPos(textPos);
                gameItems[i * width + j] = gameItem;

                posx += inc;
            }
            posx = startx;
            posz -= inc;
        }
        
        Mesh[] terrainMesh = StaticMeshesLoader.load("src/main/resources/models/terrain/terrain.obj", "src/main/resources/models/terrain");
        GameItem terrain = new GameItem(terrainMesh);
        terrain.setScale(100.0f);
        Mesh[] playerMesh = StaticMeshesLoader.load("src/main/resources/models/game/Wizard.obj", "src/main/resources/models/game");
        
        player = new Player(playerMesh);
        player.setScale(0.1f);
        player.setPosition(0, 0, 10);
        
        animItem = AnimMeshesLoader.loadAnimGameItem("src/main/resources/models/bob/boblamp.md5mesh", "");
        animItem.setScale(0.05f);
        animation = animItem.getCurrentAnimation();
        
        //scene.setGameItems(new GameItem[]{player, terrain});
        gameItems[gameItems.length-1]=player;
        scene.setGameItems(gameItems);
        // Shadows
        scene.setRenderShadows(true);
        
        // Fog
        Vector3f fogColour = new Vector3f(0.5f, 0.5f, 0.5f);
       // scene.setFog(new Fog(true, fogColour, 0.02f));

        // Setup  SkyBox
        
        SkyBox skyBox = new SkyBox("src/main/resources/models/skybox.obj", new Vector4f(0.65f, 0.65f, 0.65f, 1.0f));
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        camera.getPosition().x = 0f;
        camera.getPosition().y = 3f;
        camera.getPosition().z = 15f;
        camera.getRotation().x = 15f;
        camera.getRotation().y = 0f;
        camera.getRotation().z=-45f;
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
        playerInc.set(0, 0, 0, 0);
        
        if (window.isKeyPressed(GLFW_KEY_W)) {
            sceneChanged = true;
            playerInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            sceneChanged = true;
            playerInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            sceneChanged = true;
            playerInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            sceneChanged = true;
            playerInc.x = 1;
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
            playerInc.w = 1;
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
            sceneChanged = true;
            playerInc.w = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
        	
            sceneChanged = true;
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            sceneChanged = true;
            cameraInc.y = 1;
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
            sceneChanged = true;
            animation.nextFrame();
        }
        itemSelector.selectGameItem(new GameItem[] {player}, camera);
        
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {
        if (mouseInput.isRightButtonPressed()) {
            // Update camera based on mouse            
            Vector2f rotVec = mouseInput.getDisplVec();
            
            //camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            player.movePosition(rotVec.y * MOUSE_SENSITIVITY/4f ,0f,  rotVec.x * MOUSE_SENSITIVITY/4f);
            sceneChanged = true;
        }
        player.moveRotation(0f, playerInc.w* CAMERA_POS_STEP/4f, 0f);
        Vector3f ploc =  player.getPosition();
        //player.getRotation().y=playerInc.y* CAMERA_POS_STEP;
        player.movePosition(playerInc.x * CAMERA_POS_STEP, playerInc.y * CAMERA_POS_STEP, playerInc.z * CAMERA_POS_STEP);
       
        camera.moveRotation(0f, -cameraInc.y* CAMERA_POS_STEP/4f, 0f);
        camera.movePosition(-cameraInc.y* CAMERA_POS_STEP, 0f, -cameraInc.y* CAMERA_POS_STEP);
        
        //camera.getRotation().y=playerInc.y* CAMERA_POS_STEP;;
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

        //Transformation.updateGenericViewMatrix(player.getPosition(), player.g, matrix)
        camera.updateViewMatrix();
        
        boolean aux = mouseInput.isLeftButtonPressed();
        if (aux && !this.leftButtonPressed && this.selectDetector.selectGameItem(gameItems, window, mouseInput.getCurrentPos(), camera)) {
            this.hud.incCounter();
        }
        this.leftButtonPressed = aux;
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