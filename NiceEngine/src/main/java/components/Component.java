package components;

import editor.ReferenceType;
import editor.NiceImGui;
import imgui.ImGui;
import imgui.flag.ImGuiComboFlags;
import imgui.type.ImInt;
import system.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import util.AssetPool;
import util.FileUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    //region Fields
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public transient GameObject gameObject = null;
    //endregion

    //region Properties

    public int getUid() {
        return this.uid;
    }

    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType) {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntegerValue : enumType.getEnumConstants()) {
            enumValues[i] = enumIntegerValue.name();
            i++;
        }
        return enumValues;
    }
    //endregion

    //region Methods

    /**
     * Start is called before the first frame update
     */
    public void start() {

    }

    /**
     * // Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    public void update(float dt) {

    }

    public void editorUpdate(float dt) {

    }

    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void endCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void postSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) {
                    continue;
                }

                boolean isPublic = Modifier.isPublic(field.getModifiers());

                field.setAccessible(true);


                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);


                if (type == int.class) {
                    int val = (int) value;
                    field.set(this, NiceImGui.dragInt(name, val));
                } else if (type == float.class) {
                    float val = (float) value;
                    field.set(this, NiceImGui.dragFloat(name, val));
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val)) {
                        field.set(this, !val);
                    }
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f) value;
                    NiceImGui.drawVec2Control(name, val, " " + this.hashCode() + name);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    NiceImGui.colorPicker4(name, val);
                } else if (type.isEnum()) {
                    String[] enumValues = getEnumValues(type);
                    if (value == null) {
                        value = type.getEnumConstants()[0];
                        field.set(this, value);
                    }
                    String enumType = ((Enum) value).name();
                    ImInt index = new ImInt(indexOf(enumType, enumValues));

                    if (ImGui.combo(name, index, enumValues, enumValues.length)) {
                        field.set(this, type.getEnumConstants()[index.get()]);
                    }
                } else if (type == String.class) {
                    field.set(this,
                            NiceImGui.inputText(
                                    name + ": ",
                                    (String) value,
                                    name + ":" + gameObject.hashCode()));
                } else if (type.isArray() && name.equals("Tag")) {
                    String text = NiceImGui.inputArrayText(name + ":", (String[]) value);
                    //format & set value
                    text = text.replaceAll(" ", "");
                    String[] strArray = null;
                    strArray = text.split(",");
                    field.set(this, strArray);
                } else if (type == Sprite.class) {
                    if (value == null) {
                        value = FileUtils.getDefaultSprite();
                    }

                    if (((Sprite) value).getTexId() == -1) {
                        String texturePath = ((Sprite) value).getTexture().getFilePath();
                        ((Sprite) value).setTexture(AssetPool.getTexture(texturePath));
                    }

                    field.set(this, NiceImGui.ReferenceButton(name,
                            ReferenceType.SPRITE,
                            value,
                            "Sprite" + name + gameObject.hashCode()));
                    if (value != null) {
                        ImGui.sameLine();
                        NiceImGui.showImage((Sprite) value, new Vector2f(30, 30), true, "Value of " + name, true, new Vector2f(300, 300), false);
                    }
                }

                if (!isPublic) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void generateId() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    private int indexOf(String str, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (str.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public void destroy() {

    }

    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }
    //endregion
}
