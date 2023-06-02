package editor;

import editor.uihelper.ButtonColor;
import imgui.ImGui;
import system.MouseListener;

import java.util.ArrayList;
import java.util.List;

import static editor.NiceImGui.drawButton;
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

    static boolean alwaysShowInBottom = true;

    public void imgui() {
        ImGui.begin("Console");

        if (NiceImGui.drawButton("Clear", new ButtonColor())) {
            debugLogs.clear();
        }
        boolean tmpBoolean = alwaysShowInBottom;
        if (ImGui.checkbox("Dock in the bottom", tmpBoolean)) {
            alwaysShowInBottom = !tmpBoolean;
        }

        ImGui.beginChild("consoleItem", ImGui.getContentRegionMaxX() - 4f, ImGui.getContentRegionMaxY() - NiceImGui.getHeightOfALine() * 4, true);

        if (debugLogs.size() > MAX_DEBUGLOG_SIZE) {
            removeOldValue();
        }

        if (isRemoved) {
            ImGui.text("The old value was be removed by because low performance");
        }

        for (int i = 0; i < debugLogs.size(); i++) {
            ImGui.text(debugLogs.get(i));
        }

        ImGui.text("From MouseListener: X=" + MouseListener.getX() + " : Y=" + MouseListener.getY());

        if (alwaysShowInBottom) {
            ImGui.setScrollHereY();
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
