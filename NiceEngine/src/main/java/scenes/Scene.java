package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.*;
import editor.Debug;
import editor.MessageBox;
import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import observers.events.EventType;
import org.lwjgl.glfw.GLFW;
import system.*;
import org.joml.Vector2f;
import physics2d.Physics2D;
import renderer.Renderer;
import util.AssetPool;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {

    //region Fields
    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private List<GameObject> pendingObjects;
    private Physics2D physics2D;
    private SceneInitializer sceneInitializer;
    //endregion

    //region Constructors
    public Scene(SceneInitializer sceneInitializer) {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.pendingObjects = new ArrayList<>();
        this.isRunning = false;
    }
    //endregion

    //region Properties
    public Physics2D getPhysics() {
        return this.physics2D;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();

        return result.orElse(null);
    }

    public GameObject getGameObject(String gameObjectName) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.name.equals(gameObjectName))
                .findFirst();

        return result.orElse(null);
    }
    //endregion

    //region Methods
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    /**
     * Start is called before the first frame update
     */
    public void start() {

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            pendingObjects.add(go);
        }
    }

    public void destroy() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
    }

    public <T extends Component> GameObject findGameObjectWith(Class<T> clazz) {
        for (GameObject go : gameObjects) {
            if (go.getComponent(clazz) != null) {
                return go;
            }
        }

        return null;
    }

    public void editorUpdate(float dt) {
        if ((KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL)) && KeyListener.isKeyRelease(GLFW.GLFW_KEY_S)) {
            EventSystem.notify(null, new Event(EventType.SaveLevel));
        }

        if (KeyListener.isKeyRelease(GLFW.GLFW_KEY_L) && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL))) {
            EventSystem.notify(null, new Event(EventType.LoadLevel));
        }

        this.camera.adjustProjection();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects) {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingObjects.clear();
    }

    /**
     * Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    public void update(float dt) {
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.update(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects) {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingObjects.clear();
    }

    public void render() {
        this.renderer.render();
    }

    public Camera camera() {
        return this.camera;
    }

    public void imgui() {
        this.sceneInitializer.imgui();
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new ObjectInfo(name));
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);

        return go;
    }

    public void save(boolean isShowMessage) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");

            List<GameObject> objsToSerilize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    objsToSerilize.add(obj);
                }
            }

            writer.write(gson.toJson(objsToSerilize));
            writer.close();
            if (isShowMessage) MessageBox.setContext(true, MessageBox.TypeOfMsb.NORMAL_MESSAGE, "Save successfully");
        } catch (IOException e) {
            e.printStackTrace();
            if (isShowMessage) MessageBox.setContext(true, MessageBox.TypeOfMsb.NORMAL_MESSAGE, "Save failed");
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;

            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }

                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;

            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }
    //endregion
}
