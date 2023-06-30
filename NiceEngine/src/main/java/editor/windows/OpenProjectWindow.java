package editor.windows;

import editor.NiceImGui;
import editor.uihelper.ButtonColor;
import imgui.ImGui;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import system.MouseListener;
import system.Window;
import util.FileUtils;
import util.ProjectUtils;
import util.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;

public class OpenProjectWindow {
    private static boolean isOpen = false;
    private static List<String> projects = new ArrayList<>();
    private static int projectIconTexId = -1;
    private static boolean allowToCancel = false;

    public static void open(boolean allowToCancel){
        isOpen = true;
        getListProject();
        projectIconTexId = FileUtils.getIcon(FileUtils.ICON_NAME.PROJECT).getTexId();
        OpenProjectWindow.allowToCancel = allowToCancel;
    }

    public static void imgui(){
        if (!isOpen) return;

        ImGui.openPopup("Open project");
        ImGui.setNextWindowSize(Window.getWidth() * 0.3f, Window.getHeight() * 0.7f);

        if (ImGui.beginPopupModal("Open project")){
            ImGui.text("Select a project below or");
            ImGui.sameLine();
            NiceImGui.drawButton("Create new project", new ButtonColor(COLOR_DarkGreen, COLOR_Green, COLOR_DarkGreen), new Vector2f(200, 25));

            Vector4f textColor = Settings.NAME_COLOR;
            ImGui.textColored(textColor.x, textColor.y, textColor.z, textColor.w, "Project list");

            ImGui.beginChild("ProjectList", ImGui.getContentRegionMaxX() * 0.99f, ImGui.getContentRegionMaxY() * 0.8f, true);
            for (String p : projects){
                ImGui.image(projectIconTexId, 25f, 25f);
                ImGui.sameLine();
                ImGui.button(p);

                if (ImGui.isItemHovered()){
                    ImGui.beginTooltip();
                    ImGui.textColored(textColor.x, textColor.y, textColor.z, textColor.w, "Double-click to open this project!");
                    ImGui.endTooltip();

                    if (ImGui.isMouseDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)){
                        ProjectUtils.CURRENT_PROJECT = p;
                        isOpen = false;
                        EventSystem.notify(null, new Event(EventType.LoadLevel));
                    }
                }
            }
            ImGui.endChild();
            if (allowToCancel) {
                if (NiceImGui.drawButton("Cancel", new ButtonColor(COLOR_DarkRed, COLOR_Red, COLOR_DarkRed), new Vector2f(100, 30))) {
                    isOpen = false;
                }
            }

            ImGui.endPopup();
        }
    }
    
    private static void getListProject(){
        projects.clear();
        File directory = new File("data");
        File[] filesList = directory.listFiles();
        if (filesList != null) {
            for (File file : filesList) {
                if (file.isDirectory()){
                    projects.add(file.getName());
                }
            }
        }
    }
}
