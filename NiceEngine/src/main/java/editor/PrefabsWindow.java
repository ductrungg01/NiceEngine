package editor;

import components.Sprite;
import components.SpriteRenderer;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import renderer.Texture;
import system.GameObject;
import system.PrefabObject;
import system.Prefabs;
import system.Window;

import java.util.ArrayList;
import java.util.List;

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

    private List<PrefabObject> prefabs = new ArrayList<>();

    public void imgui() {
        if (prefabs.isEmpty()) {
            this.prefabs = Prefabs.prefabObjects;
        }

        ImGui.begin("Prefabs");

        for (PrefabObject prefab : prefabs) {
            if (drawPrefabButton(prefab)) {
                SceneHierarchyWindow.clearSelectedGameObject();
                Window.getImguiLayer().getInspectorWindow().setActiveGameObject(prefab, InspectorWindow.InspectorBottomButtonTitle.OverrideAllChildren);
            }
        }

        ImGui.end();
    }

    private boolean drawPrefabButton(PrefabObject prefab) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;

        Sprite sprite = prefab.getComponent(SpriteRenderer.class).getSprite();

        float offset = Math.min(DEFAULT_BUTTON_SIZE / sprite.getWidth(), DEFAULT_BUTTON_SIZE / sprite.getHeight());
        float spriteWidth = sprite.getWidth() * offset;
        float spriteHeight = sprite.getHeight() * offset;
        Vector2f[] texCoords = sprite.getTexCoords();

        boolean isClick = false;

        ImGui.pushID(sprite.getTexId() + "### Prefab shower" + prefab.hashCode());
        if (ImGui.imageButton(sprite.getTexId(), spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
            isClick = true;
        }
        ImGui.popID();

        ImVec2 lastButtonPos = new ImVec2();
        ImGui.getItemRectMax(lastButtonPos);
        float lastButtonX2 = lastButtonPos.x;
        float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
        if (nextButtonX2 <= windowX2) {
            ImGui.sameLine();
        }

        return isClick;
    }
}
