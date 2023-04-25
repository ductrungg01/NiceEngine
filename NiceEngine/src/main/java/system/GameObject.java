package system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import components.ObjectInfo;
import components.SpriteRenderer;
import editor.uihelper.NiceImGui;
import imgui.ImGui;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    //region Fields
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean isDead = false;
    //endregion

    //region Contructors
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
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

        return obj;
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
     * // Update is called once per frame
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
        ObjectInfo objectInfo = this.getComponent(ObjectInfo.class);
        objectInfo.name = NiceImGui.inputText("Name", objectInfo.name);

        for (Component c : components) {
            if (c.getClass() == ObjectInfo.class) continue;

            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
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
    //endregion
}
