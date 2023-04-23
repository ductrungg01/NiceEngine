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

        // Tôi cần in ra size của widget này
        //System.out.println(ImGui.getContentRegionAvailX() + " : " + ImGui.getContentRegionAvailY());
        // Tôi cần in ra vị trí góc trái tên của widget này
        //System.out.println(ImGui.getItemRectMinX() + " : " + ImGui.getItemRectMinY());
        // Tôi cần in ra vị trí góc phải dưới của widget này
        //System.out.println(ImGui.getItemRectMaxX() + " : " + ImGui.getItemRectMaxY());

        if (NiceButton("Clear", new ButtonColor())) {
            debugLogs.clear();
        }

        ImGui.beginChild("consoleItem", ImGui.getContentRegionMaxX() - 50, ImGui.getContentRegionMaxY() - 150, true);

        for (int i = 0; i < debugLogs.size(); i++) {
            ImGui.text(debugLogs.get(i));
        }

        ImGui.endChild();
        ImGui.end();
    }
}
