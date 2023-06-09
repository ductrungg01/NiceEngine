package editor;

import components.*;
import editor.uihelper.ButtonColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import org.reflections.Reflections;
import system.GameObject;
import org.joml.Vector4f;
import renderer.PickingTexture;

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

    boolean firstTime = true;

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

    String searchText = "";
    boolean showAddComponentMenu = false;

    public enum InspectorBottomButtonTitle {
        SaveAsPrefab,
        OverrideThePrefab,
        OverrideAllChildren
    }

    private final String SAVE_AS_PREFAB_BUTTON_TITLE = "Save as prefab";
    private final String OVERRIDE_THE_PREFAB_BUTTON_TITLE = "Override the prefab";
    private final String OVERRIDE_ALL_CHILDREN_BUTTON_TITLE = "Override all children";
    private boolean alwaysOverrideChildren = false;
    private String bottomButtonTitle = "";

    //region Methods
    public void imgui() {
        ImGui.begin("Inspector");

        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null) {
            activeGameObject = activeGameObjects.get(0);
        }

//        if (activeGameObjects.size() > 1) activeGameObject = null;

        if (activeGameObject == null) {
            ImGui.end();
            return;
        }

        Vector2f windowCursorPos = new Vector2f(ImGui.getCursorPosX(), ImGui.getCursorPosY());

        activeGameObject.imgui();

        ImGui.separator();

        if (NiceImGui.drawButton("Add component",
                new ButtonColor(COLOR_DarkBlue, COLOR_Blue, COLOR_Blue),
                new Vector2f(ImGui.getContentRegionAvailX(), 50f))) {
            showAddComponentMenu = true;
            searchText = "";
            ImGui.openPopup("AddComponentMenu");
        }

        if (drawBottomButton(windowCursorPos)) {
            if (bottomButtonTitle.equals(SAVE_AS_PREFAB_BUTTON_TITLE)) {
                activeGameObject.setAsPrefab();
            }
            if (bottomButtonTitle.equals(OVERRIDE_ALL_CHILDREN_BUTTON_TITLE)) {
                activeGameObject.overrideAllChildGameObject();
            }
        }

        if (showAddComponentMenu) {
            if (ImGui.beginPopup("AddComponentMenu")) {
                searchText = NiceImGui.inputTextWithHintAndNoLabel("Search", searchText, "AddingComponent" + activeGameObject.hashCode());

                ImGui.beginChild("ComponentList", 500, 350, true, ImGuiWindowFlags.HorizontalScrollbar);
                for (Class<? extends Component> aClass : classes) {
                    String className = aClass.getSimpleName();
                    if (searchText.isEmpty() || className.toLowerCase().contains(searchText.toLowerCase())) {
                        if (ImGui.menuItem(className)) {
                            Component component = null;
                            try {
                                component = aClass.getDeclaredConstructor().newInstance();
                                if (activeGameObject.getComponent(aClass) != null) continue;
                            } catch (InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException | NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }

                            showAddComponentMenu = false;
                            if (activeGameObject.getComponent(aClass) == null) {
                                activeGameObject.addComponent(component);
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

    private boolean drawBottomButton(Vector2f windowCursorPos) {
        ImGui.pushID("BottomButton");

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        windowSize.y -= 35f; // Offset, I don't know why

        float bottomMiddlePosX = windowCursorPos.x + windowSize.x / 2f;
        float bottomMiddlePosY = windowCursorPos.y + windowSize.y;

        Vector2f buttonSize = NiceImGui.getSizeOfButton(bottomButtonTitle);

        float buttonPosX = bottomMiddlePosX - buttonSize.x / 2f;
        float buttonPosY = bottomMiddlePosY - buttonSize.y;

        Vector2f oldCursorPos = new Vector2f(ImGui.getCursorPosX(), ImGui.getCursorPosY());
        ImGui.setCursorPos(buttonPosX, buttonPosY);

        Vector4f buttonColor = new Vector4f(14 / 255f, 14 / 255f, 28 / 255f, 1);

        boolean isClick = false;

        if (NiceImGui.drawButton(bottomButtonTitle,
                new ButtonColor(buttonColor, COLOR_Blue, COLOR_DarkBlue))) {
            isClick = true;
        }

        if (bottomButtonTitle.equals(OVERRIDE_ALL_CHILDREN_BUTTON_TITLE)) {
            float checkboxTitleLength = NiceImGui.getLengthOfText("Always override children");
            ImGui.setCursorPos(bottomMiddlePosX - checkboxTitleLength / 2f, ImGui.getCursorPosY());

            boolean tmpBoolean = this.alwaysOverrideChildren;
            if (ImGui.checkbox("Always override children", tmpBoolean)) {
                this.alwaysOverrideChildren = !tmpBoolean;
            }
        }

        ImGui.setCursorPos(oldCursorPos.x, oldCursorPos.y);

        ImGui.popID();

        return isClick;
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
        setActiveGameObject(go, InspectorBottomButtonTitle.SaveAsPrefab);
    }

    public void setActiveGameObject(GameObject go, InspectorBottomButtonTitle buttonTitle) {
        if (this.activeGameObject != null && this.activeGameObject == go) return;

        if (go != null) {
            clearSelected();
            this.activeGameObjects.add(go);
        }

        this.alwaysOverrideChildren = false;

        switch (buttonTitle) {
            case SaveAsPrefab -> {
                bottomButtonTitle = SAVE_AS_PREFAB_BUTTON_TITLE;
            }
            case OverrideThePrefab -> {
                bottomButtonTitle = OVERRIDE_THE_PREFAB_BUTTON_TITLE;
            }
            case OverrideAllChildren -> {
                bottomButtonTitle = OVERRIDE_ALL_CHILDREN_BUTTON_TITLE;
            }
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
