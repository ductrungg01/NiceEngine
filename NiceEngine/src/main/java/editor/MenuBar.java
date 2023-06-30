package editor;

import editor.windows.OpenProjectWindow;
import imgui.ImGui;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;

public class MenuBar {
    //region Methods
    public void imgui() {
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Open", "Ctrl+O")) {
                OpenProjectWindow.open(true);
            }

            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
    //endregion
}