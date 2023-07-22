package editor.windows;

import editor.NiceImGui;
import editor.uihelper.ButtonColor;
import imgui.ImGui;
import org.joml.Vector2f;
import system.MouseListener;
import system.Window;
import util.Settings;

import java.util.ArrayList;
import java.util.List;

public class ConsoleWindow {
    static boolean scrollToBottom = true;
    static boolean firstFrame = true;
    private static ConsoleWindow instance = null;

    //endregion
    private final int MAX_DEBUGLOG_SIZE = 200;
    public List<String> debugLogs = new ArrayList<>();
    public boolean isRemoved = false;

    //region Singleton
    private ConsoleWindow() {
    }

    public static ConsoleWindow getInstance() {
        if (instance == null) {
            instance = new ConsoleWindow();
        }

        return instance;
    }

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

        if (isRemoved && debugLogs.size() >= MAX_DEBUGLOG_SIZE) {
            ImGui.text("The old value was removed");
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
