package editor;

import components.Sprite;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import renderer.Texture;
import util.AssetPool;

public class AnimationStateCreator {

    private AnimationStateCreator() {
    }

    private static AnimationStateCreator instance = null;

    public static AnimationStateCreator getInstance() {
        if (instance == null) {
            instance = new AnimationStateCreator();
        }

        return instance;
    }

    public static boolean isShow = false;

    public void imgui() {
        if (!isShow) return;

        if (ImGui.begin("Animation State Creator",
                new ImBoolean(true),
                ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoSavedSettings)) {

            ImVec2 size = new ImVec2(300, 200);
            ImVec2 center = new ImVec2(ImGui.getMainViewport().getPosX() + ImGui.getMainViewport().getSizeX() / 2 - size.x / 2, ImGui.getMainViewport().getPosY() + ImGui.getMainViewport().getSizeY() / 2 - size.y / 2);
            ImGui.setNextWindowPos(center.x, center.y);


            ImGui.text("Image:");
            ImGui.inputText("##image", new ImString("assets/images/folder-icon.png"), 256);

            if (ImGui.button("Load")) {
                // Load image
            }

            Sprite spr = new Sprite();
            spr.setTexture(AssetPool.getTexture("assets/images/folder-icon.png"));
            ImGui.image(spr.getTexId(), 200, 200);

            ImGui.end();
        } else {
            System.out.println("Animation State Creator windows is closed");
            AnimationStateCreator.getInstance().isShow = false;
            ImGui.end();
        }

    }
}
