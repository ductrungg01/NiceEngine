package editor.windows;

import components.*;
import editor.NiceImGui;
import editor.uihelper.ButtonColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import org.reflections.Reflections;
import physics2d.components.Box2DCollider;
import physics2d.components.Capsule2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import system.GameObject;
import org.joml.Vector4f;
import renderer.PickingTexture;
import system.Window;
import util.Settings;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static editor.uihelper.NiceShortCall.*;

public class InspectorWindow {

    //region Fields
    List<GameObject> activeGameObjects;
    private List<Vector4f> activeGameObjectOriginalColor;
    private GameObject activeGameObject = null;
    private GameObject copyGameObject = null;
    private PickingTexture pickingTexture;

    String searchText = "";
    boolean showAddComponentMenu = false;

    Set<Class<? extends Component>> classes;
    //endregion

    //region Constructors
    public InspectorWindow(PickingTexture pickingTexture) {
        this.activeGameObjects = new ArrayList<>();
        this.pickingTexture = pickingTexture;
        this.activeGameObjectOriginalColor = new ArrayList<>();

        Reflections reflections = new Reflections("physics2d.components");
        classes = reflections.getSubTypesOf(Component.class);
        reflections = new Reflections("components");
        classes.addAll(reflections.getSubTypesOf(Component.class));

        List<Class<? extends Component>> classesToRemove = new ArrayList<>();

        for (Class<? extends Component> aClass : classes) {
            try {
                Component component = aClass.getDeclaredConstructor().newInstance();
                if (component instanceof INonAddableComponent) {
                    classesToRemove.add(aClass);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    cause.printStackTrace();
                } else {
                    e.printStackTrace();
                }
            }
        }

        classes.removeAll(classesToRemove);
    }
    //endregion

    //region Methods
    public void imgui() {
        ImGui.setNextWindowSizeConstraints(Settings.MIN_WIDTH_GROUP_WIDGET, Settings.MIN_HEIGHT_GROUP_WIDGET, Window.getWidth(), Window.getHeight());

        ImGui.begin("Inspector");

        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null) {
            activeGameObject = activeGameObjects.get(0);
        }

//        if (activeGameObjects.size() > 1) activeGameObject = null;

        if (activeGameObject == null) {
            ImGui.end();
            return;
        }

        activeGameObject.imgui();

        ImGui.separator();

        if (NiceImGui.drawButton("Add component",
                new ButtonColor(COLOR_DarkBlue, COLOR_Blue, COLOR_Blue),
                new Vector2f(ImGui.getContentRegionAvailX(), 50f))) {
            showAddComponentMenu = true;
            searchText = "";
            ImGui.openPopup("AddComponentMenu");
        }

        if (showAddComponentMenu) {
            if (ImGui.beginPopup("AddComponentMenu")) {
                searchText = NiceImGui.inputText("", searchText, "AddingComponent" + activeGameObject.hashCode());

                ImGui.beginChild("ComponentList", 500, 350, true, ImGuiWindowFlags.HorizontalScrollbar);
                for (Class<? extends Component> aClass : classes) {
                    String className = aClass.getSimpleName();
                    if (searchText.isEmpty() || className.toLowerCase().contains(searchText.toLowerCase())) {
                        if (ImGui.menuItem(className)) {
                            Component component = null;
                            try {
                                component = aClass.getDeclaredConstructor().newInstance();
                                if (activeGameObject.getComponent(aClass) != null) {
                                    JOptionPane.showMessageDialog(null, "Component '" + className + "' is existed!",
                                            "ERROR", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                                if ((component instanceof Box2DCollider) || (component instanceof CircleCollider) || (component instanceof Capsule2DCollider)) {
                                    if (activeGameObject.getComponent(RigidBody2D.class) == null) {
                                        JOptionPane.showMessageDialog(null, "You need add RigidBody2D before add any Collider!",
                                                "ERROR", JOptionPane.ERROR_MESSAGE);
                                        continue;
                                    }
                                }
                            } catch (InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException | NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }

                            showAddComponentMenu = false;
                            if (activeGameObject.getComponent(aClass) == null) {
                                activeGameObject.addComponent(component);
                                component.start();
                            }
                            ImGui.closeCurrentPopup();
                        }
                    }
                }
                ImGui.endChild();
                ImGui.endPopup();
            }
        }

        ImGui.end();
    }

    public GameObject getActiveGameObject() {
        return activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) : null;
    }

    public void clearSelected() {
        if (activeGameObjectOriginalColor.size() > 0) {
            int i = 0;
            for (GameObject go : activeGameObjects) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr != null) {
                    spr.setColor(activeGameObjectOriginalColor.get(i));
                }
                i++;
            }
        }

        this.activeGameObjects.clear();
        this.activeGameObject = null;
        this.activeGameObjectOriginalColor.clear();
    }

    public List<GameObject> getActiveGameObjects() {
        return this.activeGameObjects;
    }

    public void setActiveGameObject(GameObject go) {
        if (this.activeGameObject != null && this.activeGameObject == go) return;

        if (go != null) {
            clearSelected();
            this.activeGameObjects.add(go);
        }
    }

    public void addActiveGameObject(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            this.activeGameObjectOriginalColor.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        } else {
            this.activeGameObjectOriginalColor.add(new Vector4f());
        }
        this.activeGameObjects.add(go);
    }

    public PickingTexture getPickingTexture() {
        return this.pickingTexture;
    }

    public GameObject getCopyGameObject() {
        return copyGameObject;
    }

    public void setCopyGameObject(GameObject copyGameObject) {
        this.copyGameObject = copyGameObject;
    }

    //endregion
}
