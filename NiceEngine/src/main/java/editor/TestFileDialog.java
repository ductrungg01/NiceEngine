package editor;

import components.Sprite;
import editor.uihelper.NiceImGui;
import imgui.ImGui;
import org.lwjgl.system.CallbackI;
import util.FileUtils;

import java.io.File;

public class TestFileDialog {
    //region Singleton
    private TestFileDialog() {
    }

    private static TestFileDialog instance = null;
    static Sprite spr = FileUtils.getDefaultSprite();
    static Sprite spr2 = FileUtils.getDefaultSprite();

    public static TestFileDialog getInstance() {
        if (instance == null) {
            instance = new TestFileDialog();
        }

        return instance;
    }

    //endregion

    public void imgui() {
        ImGui.begin("Test FileDialog");
        String id = "TestOpenFileDialogButton";
        ImGui.pushID(id);
        if (ImGui.button("Open FileDialog")) {
            FileDialog.getInstance().open(id, ReferenceType.SPRITE);
        }
        spr = (Sprite) FileDialog.getInstance().getSelectedObject(id, spr);

        ImGui.image(spr.getTexId(), 300, 300);

        ImGui.popID();
        String id2 = "TestOpenFileDialogButton2";
        ImGui.pushID(id2);
        if (ImGui.button("Open FileDialog 2")) {
            FileDialog.getInstance().open(id2, ReferenceType.SPRITE);
        }
        spr2 = (Sprite) FileDialog.getInstance().getSelectedObject(id2, spr2);

        ImGui.image(spr2.getTexId(), 300, 300);

        ImGui.popID();
        ImGui.end();
    }
}
