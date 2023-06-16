package editor.windows;

import components.Sprite;
import components.SpriteRenderer;
import editor.Debug;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import system.Window;
import util.Settings;
import util.Time;

import javax.swing.*;

import static editor.uihelper.NiceShortCall.COLOR_Blue;
import static editor.uihelper.NiceShortCall.COLOR_Red;

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

    final float DEFAULT_BUTTON_SIZE = 45;

    boolean isClick = false;
    boolean isCreateChild = false;
    boolean isRemove = false;

    public String prefabNeedToHighlight = "";
    private final float HIGHLIGHT_TIME = 2f;
    private float highlightTimeRemain = HIGHLIGHT_TIME;

    public void imgui() {
        ImGui.setNextWindowSizeConstraints(Settings.MIN_WIDTH_GROUP_WIDGET, Settings.MIN_HEIGHT_GROUP_WIDGET, Window.getWidth(), Window.getHeight());

        if (!prefabNeedToHighlight.isEmpty()) {
            ImGui.setWindowFocus("Prefabs");
        }

        ImGui.begin("Prefabs");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 oldItemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(oldItemSpacing);
        final Vector2f ITEM_SPACING_DEFAULT = new Vector2f(12, 12);
        ImGui.getStyle().setItemSpacing(ITEM_SPACING_DEFAULT.x, ITEM_SPACING_DEFAULT.y);
        float windowX2 = windowPos.x + windowSize.x;
        ImVec2 itemSpacing = new ImVec2(ITEM_SPACING_DEFAULT.x, ITEM_SPACING_DEFAULT.y);

        int itemsPerRow = (int) (windowSize.x / (itemSpacing.x * 2 + DEFAULT_BUTTON_SIZE));
        int numOfRow = GameObject.PrefabLists.size() / itemsPerRow + (GameObject.PrefabLists.size() % itemsPerRow != 0 ? 1 : 0);

        for (int i = 0; i < GameObject.PrefabLists.size(); i++) {
            GameObject prefab = GameObject.PrefabLists.get(i);

            isClick = false;
            isCreateChild = false;
            isRemove = false;

            drawPrefabButton(prefab);

            if (prefab.prefabId.equals(prefabNeedToHighlight)) {
                int rowOfHighLightItem = (i + 1) / itemsPerRow + ((i + 1) % itemsPerRow != 0 ? 1 : 0);
                ImGui.setScrollHereY((float) rowOfHighLightItem / numOfRow);
            }

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + DEFAULT_BUTTON_SIZE;
            if (nextButtonX2 <= windowX2) {
                ImGui.sameLine();
            }

            if (isClick) {
                SceneHierarchyWindow.clearSelectedGameObject();
                Window.getImguiLayer().getInspectorWindow().setActiveGameObject(prefab);
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
                    i--;
                    prefab.removeAsPrefab();

                    GameObject activeGoInspector = Window.getImguiLayer().getInspectorWindow().getActiveGameObject();
                    if (activeGoInspector == prefab) {
                        Window.getImguiLayer().getInspectorWindow().clearSelected();
                    }
                }
            }
        }

        ImGui.getStyle().setItemSpacing(oldItemSpacing.x, oldItemSpacing.y);
        ImGui.end();

        if (!prefabNeedToHighlight.isEmpty()) {
            highlightTimeRemain -= Time.deltaTime;
            if (highlightTimeRemain < 0) {
                prefabNeedToHighlight = "";
                highlightTimeRemain = HIGHLIGHT_TIME;
            }
        }

        Debug.Log(highlightTimeRemain);
    }

    private void drawPrefabButton(GameObject prefab) {
        Sprite sprite = prefab.getComponent(SpriteRenderer.class).getSprite();

        Vector2f[] texCoords = sprite.getTexCoords();

        String idPush = sprite.getTexId() + "### Prefab shower" + prefab.hashCode();
        ImGui.pushID(idPush);

        boolean needToHighLight = prefab.prefabId.equals(prefabNeedToHighlight);
        if (!needToHighLight) {
            GameObject go = Window.getImguiLayer().getInspectorWindow().getActiveGameObject();
            if (go != null) {
                if (go.isPrefab() && go.prefabId.equals(prefab.prefabId)) {
                    needToHighLight = true;
                }
            }
        }

        Vector2f oldCursorScreenPos = new Vector2f(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        if (needToHighLight) {
            Vector4f highlightBtnCol = COLOR_Red;
            ImGui.pushStyleColor(ImGuiCol.Button, highlightBtnCol.x, highlightBtnCol.y, highlightBtnCol.z, highlightBtnCol.w);
            ImGui.setCursorScreenPos(ImGui.getCursorScreenPosX() - itemSpacing.x / 2, ImGui.getCursorScreenPosY() - itemSpacing.y / 2);
            ImGui.button("", DEFAULT_BUTTON_SIZE + itemSpacing.x * 1.5f, DEFAULT_BUTTON_SIZE + itemSpacing.y * 1.5f);
            ImGui.setCursorScreenPos(oldCursorScreenPos.x, oldCursorScreenPos.y);

        }

        if (ImGui.imageButton(sprite.getTexId(), DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
            isClick = true;
        }

        if (ImGui.isItemHovered()) {
            if (ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                ImGui.openPopup("RightClick of Prefab" + prefab.hashCode());
            } else {
                ImGui.beginTooltip();
                Vector4f color = Settings.NAME_COLOR;
                ImGui.textColored(color.x, color.y, color.z, color.w, prefab.name);
                ImGui.text("Click to see details in Inspectors window!");
                ImGui.text("Right-click if you want to create a child!");
                ImGui.endTooltip();
            }
        }

        if (needToHighLight) {
            ImGui.popStyleColor();
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
    }
}
