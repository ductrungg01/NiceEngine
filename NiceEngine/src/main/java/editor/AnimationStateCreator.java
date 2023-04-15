package editor;

import components.Sprite;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import renderer.Texture;
import util.AssetPool;

public class AnimationStateCreator {

    void imgui() {
        ImGui.begin("Animation State Creator", new ImBoolean(true));

        ImVec2 size = new ImVec2(300, 200);
        ImVec2 center = new ImVec2(ImGui.getMainViewport().getPosX() + ImGui.getMainViewport().getSizeX() / 2 - size.x / 2, ImGui.getMainViewport().getPosY() + ImGui.getMainViewport().getSizeY() / 2 - size.y / 2);
        ImGui.setNextWindowPos(center.x, center.y);


        ImGui.text("Image:");
        ImGui.inputText("##image", new ImString("assets/images/folder-icon.png"), 256);

        if (ImGui.button("Load")) {
            // Load image
        }

        Sprite spr = new Sprite();
        spr.setTexture(AssetPool.getTexture("assets/images/logo.png"));
        ImGui.image(spr.getTexId(), 200, 200);

        ImGui.end();
    }
}
