package editor;

import editor.uihelper.ButtonColor;
import editor.uihelper.NiceImGui;
import editor.uihelper.ReferenceConfig;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static editor.uihelper.NiceImGui.NiceButton;
import static util.FileUtils.getAllFilesWithExtensions;

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
    private final int MAX_DEBUGLOG_SIZE = 2000;
    private boolean isRemoved = false;

    public void imgui() {
        ImGui.begin("Console");

        if (NiceButton("Clear", new ButtonColor())) {
            debugLogs.clear();
        }

        ImGui.beginChild("consoleItem", ImGui.getContentRegionMaxX() - 50, ImGui.getContentRegionMaxY() - 100, true);

        if (debugLogs.size() > MAX_DEBUGLOG_SIZE) {
            removeOldValue();
        }

        for (int i = 0; i < debugLogs.size(); i++) {
            ImGui.text(debugLogs.get(i));
        }

        if (isRemoved) {
            ImGui.text("The old value was be removed by because low performance");
        }

        ImGui.endChild();
        ImGui.end();
    }

    // TODO: this is temporary method, we need to find better solution, we will implement LinkedList instead of List in the future
    private void removeOldValue() {
        int n = debugLogs.size();
        for (int i = 0; i < n - 1; i++) {
            debugLogs.set(i, debugLogs.get(i + 1));
        }

        debugLogs.remove(n - 1);
        isRemoved = true;
    }
}
