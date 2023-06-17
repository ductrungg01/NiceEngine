package editor.windows;

import editor.NiceImGui;
import editor.uihelper.ButtonColor;
import imgui.ImGui;
import system.Window;
import util.Settings;

import java.util.ArrayList;
import java.util.List;

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
    private final int MAX_DEBUGLOG_SIZE = 50;
    private boolean isRemoved = false;
    static boolean scrollToBottom = true;
    static boolean firstFrame = true;

    public void imgui() {
        ImGui.setNextWindowSizeConstraints(Settings.MIN_WIDTH_GROUP_WIDGET, Settings.MIN_HEIGHT_GROUP_WIDGET, Window.getWidth(), Window.getHeight());

        ImGui.begin("Console");

        if (NiceImGui.drawButton("Clear", new ButtonColor())) {
            debugLogs.clear();
        }

        ImGui.beginChild("consoleItems", 0, 0, true);

        if (debugLogs.size() > MAX_DEBUGLOG_SIZE) {
            debugLogs.remove(0);
            isRemoved = true;
        }

        if (isRemoved) {
            ImGui.text("The old value was be removed by because low performance");
        }

        for (int i = 0; i < debugLogs.size(); i++) {
            ImGui.text(debugLogs.get(i));
        }

        if (ImGui.getScrollY() < ImGui.getScrollMaxY()) {
            scrollToBottom = false;
        } else {
            scrollToBottom = true;
        }

        if (scrollToBottom || firstFrame) {
            ImGui.setScrollHereY(1.0f);
        }
        ImGui.endChild();

        firstFrame = false;

        ImGui.end();
    }
}
