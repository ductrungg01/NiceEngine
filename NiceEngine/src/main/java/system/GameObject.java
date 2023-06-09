package system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.StateMachine;
import deserializers.ComponentDeserializer;
import components.SpriteRenderer;
import deserializers.GameObjectDeserializer;
import deserializers.PrefabDeserializer;
import editor.NiceImGui;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import util.AssetPool;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameObject {
    //region Fields
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public String name = "";
    public String tag = "";
    private boolean isPrefab = false;
    public String prefabId = "";
    public String parentId = "";
    public static List<GameObject> PrefabLists = new ArrayList<>();
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean isDead = false;
    //endregion

    //region Constructors
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    public GameObject(String name, String prefabId) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;

        this.prefabId = prefabId;
        this.isPrefab = true;
    }
    //endregion

    //region Methods
    public GameObject copy() {
        // TODO: come up with cleaner solution
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();

        for (Component c : obj.getAllComponents()) {
            c.generateId();
        }

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilePath()));
        }

        StateMachine stateMachine = obj.getComponent(StateMachine.class);
        if (stateMachine != null) {
            stateMachine.refreshTextures();
        }

        return obj;
    }

    public GameObject copyFromPrefab() {
        // TODO: come up with cleaner solution
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new PrefabDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();

        for (Component c : obj.getAllComponents()) {
            c.generateId();
        }

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilePath()));
        }

        StateMachine stateMachine = obj.getComponent(StateMachine.class);
        if (stateMachine != null) {
            stateMachine.refreshTextures();
        }

        obj.prefabId = "";
        obj.parentId = this.prefabId;
        obj.isPrefab = false;

        return obj;
    }

    public void removeAsPrefab() {
        for (GameObject go : Window.getScene().getGameObjects()) {
            if (go.parentId.equals(this.prefabId) ||
                    (go.isPrefab && go.prefabId.equals(this.prefabId))) {
                go.parentId = "";
            }
        }

        GameObject.PrefabLists.remove(this);
    }

    public void destroy() {
        isDead = true;
        for (int i = 0; i < components.size(); i++) {
            components.get(i).destroy();
        }
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    /**
     * Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    public void update(float dt) {
        for (int i = 0; i < this.components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    /**
     * Start is called before the first frame update
     */
    public void start() {
        for (int i = 0; i < this.components.size(); i++) {
            components.get(i).start();
        }
    }

    public void imgui() {
        this.name = NiceImGui.inputText("Name", this.name, "Name of " + this.hashCode());
        this.tag = NiceImGui.inputText("Tag", this.tag, "Tag of " + this.hashCode());
//        ImGui.text("isPrefab? : " + this.isPrefab);
//        ImGui.text("prefab ID : " + this.prefabId);
//        ImGui.text("parent ID : " + this.parentId);


        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);

            ImBoolean removeComponentButton = new ImBoolean(true);

            if (ImGui.collapsingHeader(c.getClass().getSimpleName(), removeComponentButton)) {
                c.imgui();
            }

            if (!removeComponentButton.get()) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Remove component '" + c.getClass().getSimpleName() + "' from game object '" + this.name + "'?",
                        "REMOVE COMPONENT",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    components.remove(i);
                    i--;
                }
            }

        }
    }

    public void editorUpdate(float dt) {
        for (int i = 0; i < this.components.size(); i++) {
            components.get(i).editorUpdate(dt);
        }
    }
    //endregion

    //region Properties
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getUid() {
        return this.uid;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public void generateUid() {
        this.uid = ID_COUNTER++;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }

    public boolean compareTag(String tag) {
        return this.tag.equals(tag);
    }

    public boolean isPrefab() {
        return this.isPrefab;
    }

    public void setAsPrefab() {
        GameObject prefab = this.copy();

        prefab.transform.position = new Vector2f();
        prefab.isPrefab = true;
        prefab.isDead = true;
        prefab.parentId = "";
        prefab.generatePrefabId();
        GameObject.PrefabLists.add(prefab);

        this.parentId = prefab.prefabId;
    }

    public void setIsNotPrefab() {
        this.isPrefab = false;
        this.prefabId = "";
        GameObject.PrefabLists.remove(this);
    }

    public GameObject generateChildGameObject() {
        if (!this.isPrefab) return null;
        GameObject newGo = this.copyFromPrefab();

        newGo.isPrefab = false;
        newGo.prefabId = "";
        newGo.isDead = false;
        newGo.doSerialization();
        newGo.parentId = this.prefabId;

        return newGo;
    }

    public void overrideAllChildGameObject() {
        List<GameObject> gameObjects = Window.getScene().getGameObjects();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            if (go.parentId.equals(this.prefabId)) {
                Vector2f oldPosition = go.transform.position;

                go.destroy();
                GameObject newGameObject = this.copyFromPrefab();
                newGameObject.transform.position = oldPosition;
                Window.getScene().addGameObjectToScene(newGameObject);
            }
        }
    }

    public void generatePrefabId() {
        do {
            this.prefabId = generateRandomString();
        } while (isExistedId(this.prefabId));
    }

    private boolean isExistedId(String id) {
        for (GameObject go : GameObject.PrefabLists) {
            String prefabId = go.prefabId;
            if (id.equals(prefabId)) {
                return true;
            }
        }
        return false;
    }

    private String generateRandomString() {
        int length = 10;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    //endregion
}
