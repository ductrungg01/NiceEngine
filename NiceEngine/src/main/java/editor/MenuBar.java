package editor;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;

public class MenuBar {

    boolean isShowAnimStateCreator = false;
    AnimationStateCreator animationStateCreator = new AnimationStateCreator();

    //region Methods
    public void imgui() {
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Windows")) {
            if (ImGui.menuItem("Animation State Creator")) {
                isShowAnimStateCreator = true;
            }
            ImGui.endMenu();
        }

        ImGui.endMenuBar();

        if (isShowAnimStateCreator) {
            animationStateCreator.imgui();
        }

        ImGui.showDemoWindow();
    }
    //endregion
}