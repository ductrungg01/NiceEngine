package editor;

import editor.uihelper.ButtonColor;
import imgui.ImGui;
import imgui.ImVec2;

import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceImGui.NiceButton;

public class ConsoleWindow {
    //region Singleton
    private ConsoleWindow() {
    }

    private static ConsoleWindow instance = null;

    public static ConsoleWindow getInstance() {
        if (instance == null) {
            instance = new ConsoleWindow();
        }

        return instance;
    }

    //endregion

    public List<String> debugLogs = new ArrayList<>();

    public void imgui() {
        ImGui.begin("Console");

        if (NiceButton("Clear", new ButtonColor())) {
            debugLogs.clear();
        }

        ImGui.beginChild("consoleItem", ImGui.getContentRegionMaxX(), ImGui.getContentRegionMaxY(), false);

        for (int i = 0; i < debugLogs.size(); i++) {
            ImGui.text(debugLogs.get(i));
        }

        ImGui.endChild();
        ImGui.end();
    }
}
