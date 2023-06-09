package editor;

import components.Sprite;
import components.SpriteRenderer;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import system.Window;
import util.Settings;

import javax.swing.*;

public class PrefabsWindow {
    //region Singleton
    private PrefabsWindow() {
    }

    private static PrefabsWindow instance = null;

    public static PrefabsWindow getInstance() {
        if (instance == null) {
            instance = new PrefabsWindow();
        }

        return instance;
    }
    //endregion

    final float DEFAULT_BUTTON_SIZE = 64;

    boolean isClick = false;
    boolean isCreateChild = false;
    boolean isRemove = false;

    public void imgui() {
        ImGui.setNextWindowSizeConstraints(Settings.MIN_WIDTH_GROUP_WIDGET, Settings.MIN_HEIGHT_GROUP_WIDGET, Window.getWidth(), Window.getHeight());

        ImGui.begin("Prefabs");

        for (int i = 0; i < GameObject.PrefabLists.size(); i++) {
            GameObject prefab = GameObject.PrefabLists.get(i);

            isClick = false;
            isCreateChild = false;
            isRemove = false;

            drawPrefabButton(prefab);

            if (isClick) {
                SceneHierarchyWindow.clearSelectedGameObject();
                Window.getImguiLayer().getInspectorWindow().setActiveGameObject(prefab, InspectorWindow.InspectorBottomButtonTitle.OverrideAllChildren);
            }
            if (isCreateChild) {
                GameObject childGo = prefab.generateChildGameObject();
                Window.getScene().getMouseControls().pickupObject(childGo);
            }
            if (isRemove) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Remove prefab '" + prefab.name + "'?",
                        "REMOVE PREFAB",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    prefab.removeAsPrefab();

                    GameObject activeGoInspector = Window.getImguiLayer().getInspectorWindow().getActiveGameObject();
                    if (activeGoInspector == prefab) {
                        Window.getImguiLayer().getInspectorWindow().clearSelected();
                    }
                }
                i--;
            }

        }

        ImGui.end();
    }

    private void drawPrefabButton(GameObject prefab) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;

        Sprite sprite = prefab.getComponent(SpriteRenderer.class).getSprite();

        Vector2f[] texCoords = sprite.getTexCoords();

        String idPush = sprite.getTexId() + "### Prefab shower" + prefab.hashCode();
        ImGui.pushID(idPush);
        if (ImGui.imageButton(sprite.getTexId(), DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
            isClick = true;
        }

        if (ImGui.isItemHovered() && ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            ImGui.openPopup("RightClick of Prefab" + prefab.hashCode());
        }

        if (ImGui.beginPopup("RightClick of Prefab" + prefab.hashCode())) {
            if (ImGui.menuItem("Create a child game object")) {
                isCreateChild = true;
            }
            if (ImGui.menuItem("Remove this prefab")) {
                isRemove = true;
            }
            ImGui.endPopup();
        }

        ImGui.popID();

        ImVec2 lastButtonPos = new ImVec2();
        ImGui.getItemRectMax(lastButtonPos);
        float lastButtonX2 = lastButtonPos.x;
        float nextButtonX2 = lastButtonX2 + itemSpacing.x + DEFAULT_BUTTON_SIZE;
        if (nextButtonX2 <= windowX2) {
            ImGui.sameLine();
        }


    }
}
