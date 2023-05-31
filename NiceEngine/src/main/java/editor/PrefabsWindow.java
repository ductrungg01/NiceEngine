package editor;

import components.MouseControls;
import components.Sprite;
import components.SpriteRenderer;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

import java.util.ArrayList;
import java.util.List;

public class PrefabsWindow {
    //region Singleton
    private PrefabsWindow() {
        gameObject = new GameObject("PrefabWindow Object");
        gameObject.setNoSerialize();
        gameObject.addComponent(new MouseControls());
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

    private GameObject gameObject;


    public void imgui() {
        ImGui.begin("Prefabs");

        for (GameObject prefab : GameObject.PrefabLists) {
            if (drawPrefabButton(prefab)) {
                GameObject childGo = prefab.generateChildGameObject();
                gameObject.getComponent(MouseControls.class).pickupObject(childGo);

                SceneHierarchyWindow.clearSelectedGameObject();
                Window.getImguiLayer().getInspectorWindow().setActiveGameObject(prefab, InspectorWindow.InspectorBottomButtonTitle.OverrideAllChildren);
            }
        }

        ImGui.end();
    }

    private boolean drawPrefabButton(GameObject go) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;

        Sprite sprite = go.getComponent(SpriteRenderer.class).getSprite();

//        float offset = Math.min(DEFAULT_BUTTON_SIZE / sprite.getTexture().getWidth(), DEFAULT_BUTTON_SIZE / sprite.getTexture().getHeight());
//        float spriteWidth = sprite.getTexture().getWidth() * offset;
//        float spriteHeight = sprite.getTexture().getHeight() * offset;
        Vector2f[] texCoords = sprite.getTexCoords();

        boolean isClick = false;

        ImGui.pushID(sprite.getTexId() + "### Prefab shower" + go.hashCode());
        if (ImGui.imageButton(sprite.getTexId(), DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
            isClick = true;
        }
        ImGui.popID();

        ImVec2 lastButtonPos = new ImVec2();
        ImGui.getItemRectMax(lastButtonPos);
        float lastButtonX2 = lastButtonPos.x;
        float nextButtonX2 = lastButtonX2 + itemSpacing.x + DEFAULT_BUTTON_SIZE;
        if (nextButtonX2 <= windowX2) {
            ImGui.sameLine();
        }

        return isClick;
    }
}
