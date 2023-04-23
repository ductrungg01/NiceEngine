package editor;

import editor.uihelper.ButtonColor;
import components.Sprite;
import editor.uihelper.NiceImGui;
import imgui.ImGui;
import imgui.flag.ImGuiDir;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector2f;
import util.AssetPool;

import static editor.uihelper.NiceImGui.NiceButton;
import static editor.uihelper.NiceShortCall.*;

public class AnimationStateCreator {

    //region Singleton
    private AnimationStateCreator() {
    }

    private static AnimationStateCreator instance = null;

    public static AnimationStateCreator getInstance() {
        if (instance == null) {
            instance = new AnimationStateCreator();
        }

        return instance;
    }
    //endregion

    public static boolean isShow = true;
    private boolean showFileDialog = false;

    ImString imagePath = new ImString("Assets/images/something.png");
    Vector2f deviceBy = new Vector2f(1, 1);
    ImString frameTimeSmall = new ImString("0.02s");
    ImString title = new ImString("This is the name of this Anim State");
    ImBoolean isLoop = new ImBoolean(true);

    public void imgui() {
        if (!isShow) return;

        ImGui.setNextWindowSize(1000, 1000);
        ImGui.begin("Animation State Creator", new ImBoolean(true),
                ImGuiWindowFlags.NoResize);

        //region LEFT COLUMN
        // Bên trái
        ImGui.columns(2);
        ImGui.setColumnWidth(0, 600);

        // Dòng 1
        ImGui.text("Image path:");
        ImGui.sameLine();
        int INPUT_TEXT_BUF_SIZE = 100;
        ImGui.inputText("", imagePath, INPUT_TEXT_BUF_SIZE);
        ImGui.sameLine();
        if (ImGui.button("Browse")) {
            // Handle browse button click event
            showFileDialog = true;
        }

        // Dòng 2
//        ImGui.text("Device by: ");
        ImGui.beginChild("##image device", 500, 30, false);
        NiceImGui.drawVec2Control("Device by:", deviceBy, 1, 100, 300, 0, 1);
        ImGui.endChild();

        // Dòng 3
        ImGui.text("Image preview:");

        ImGui.beginChild("##image loaded", 500, 500, true);

        Sprite spr = new Sprite();
        spr.setTexture(AssetPool.getTexture("assets/images/folder-icon.png"));
        ImGui.image(spr.getTexId(), 450, 450);

        ImGui.endChild();

        // Dòng 4
        ImGui.arrowButton("##down", ImGuiDir.Down);
        ImGui.sameLine();
        ImGui.arrowButton("##left", ImGuiDir.Left);
        ImGui.sameLine();
        ImGui.arrowButton("##right", ImGuiDir.Right);

        // Dòng 5
        ImGui.beginChild("##frame-preview", 250, 250, true);
        ImGui.dummy(200, 200);
        ImGui.endChild();
        ImGui.sameLine();
        ImGui.text("Frame time:");
        ImGui.sameLine();
        ImGui.inputText("", frameTimeSmall, INPUT_TEXT_BUF_SIZE);
//        if (ImGui.button("Add to frame list")) {
//            // Handle add frame button click event
//        }
        NiceButton("Add to frame list", new ButtonColor(COLOR_Blue, COLOR_LightBlue, COLOR_DarkBlue));
        //endregion

        //region RIGHT COLUMN
        // Bên phải
        ImGui.nextColumn();

        // Dòng 1
        ImGui.text("Title:");
        ImGui.sameLine();
        ImGui.inputText("", title, INPUT_TEXT_BUF_SIZE);

        // Dòng 2
        ImGui.checkbox("Is loop?", isLoop);

        // Dòng 3
        ImGui.beginChild("##frame-list", 500, 500, true);
        for (int i = 0; i < 111; i++) {
            ImGui.pushID(i);
            if (ImGui.button("Frame: " + i + " | " + "Time: " + 0.04 + " ")) {

            }
            ImGui.sameLine();
            if (ImGui.button("Delete")) {
                // Handle delete button click event
            }
            ImGui.popID();
        }
        ImGui.endChild();

        // Dòng 4
        ImGui.dummy(250, 250);

        // Dòng cuối
        if (NiceButton("Cancel", new ButtonColor(COLOR_Red, COLOR_LightRed, COLOR_DarkRed))) {
            // handle
            Debug.Log("You are click Cancel button");
        }
        ImGui.sameLine();
        if (NiceButton("Save", new ButtonColor(COLOR_Green, COLOR_LightGreen, COLOR_DarkGreen))) {
            // handle
            Debug.Log("You are click Save button");
        }
        //endregion


        if (showFileDialog) {
            ImGui.openPopup("File Dialog");
        }

        if (ImGui.beginPopupModal("File Dialog")) {
            ImGui.text("Select an image file");
            ImGui.separator();

            // File dialog contents go here

            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
                showFileDialog = false;
            }

            ImGui.endPopup();
        }

        ImGui.columns(1);
        ImGui.end();
    }
}
