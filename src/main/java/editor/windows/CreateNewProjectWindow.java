package editor.windows;

import editor.NiceImGui;
import editor.uihelper.ButtonColor;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import system.Window;
import util.ProjectUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;

public class CreateNewProjectWindow {
    private static boolean isOpen = false;
    private static boolean showOpenProjectWindow = false;
    private static String projectName = "";

    public static void open(boolean showOpenProjectWindowAfterCancel) {
        isOpen = true;
        projectName = "";
        showOpenProjectWindow = showOpenProjectWindowAfterCancel;
    }

    public static void imgui() {
        if (!isOpen) return;

        ImGui.openPopup("Create new project");

        float popupWidth = Window.getWidth() * 0.3f;
        float popupHeight = Window.getHeight() * 0.15f;
        ImGui.setNextWindowSize(popupWidth, popupHeight);

        float popupPosX = (float) Window.getWidth() / 2 - popupWidth / 2;
        float popupPosY = (float) Window.getHeight() / 2 - popupHeight / 2;
        ImGui.setNextWindowPos(popupPosX, popupPosY, ImGuiCond.Always);

        if (ImGui.beginPopupModal("Create new project", ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize)) {
            projectName = NiceImGui.inputText("", projectName, "Enter project name", 0, "NewProjectName");

            if (NiceImGui.drawButton("Create", new ButtonColor(COLOR_DarkBlue, COLOR_Blue, COLOR_DarkBlue), new Vector2f(100, 30))) {
                if (!projectName.isEmpty()) {
                    if (createNewProject(projectName)) {
                        isOpen = false;
                        Window.get().changeCurrentProject(projectName, true, true);
                    }
                }
            }

            ImGui.sameLine();

            if (NiceImGui.drawButton("Cancel", new ButtonColor(COLOR_DarkRed, COLOR_Red, COLOR_DarkRed), new Vector2f(100, 30))) {
                isOpen = false;
                if (showOpenProjectWindow) {
                    OpenProjectWindow.open(true);
                }
            }

            ImGui.endPopup();
        }
    }

    private static boolean createNewProject(String projectName) {
        if (!isValidName(projectName)) {
            return false;
        }

        File folder = new File("data\\" + projectName);

        if (!folder.exists()) {
            boolean success = folder.mkdir();
            if (success) {
                JOptionPane.showMessageDialog(null, "Create project '" + projectName + "' successful",
                        "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                createFile(projectName);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error when create project!\nCheck if the project name has any special characters? ",
                        "CREATE PROJECT FAIL", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "'" + projectName + "' is existed!",
                    "INVALID NAME", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void createFile(String projectName) {
        try {
            FileWriter writer = new FileWriter("data\\" + projectName + "\\" + "level.txt");
            writer.write("[]");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter writer = new FileWriter("data\\" + projectName + "\\" + "prefabs.txt");
            writer.write("[]");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter writer = new FileWriter("data\\" + projectName + "\\" + "spritesheet.txt");
            writer.write("");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isValidName(String name) {
        if (name.startsWith(" ") || name.endsWith(" ")) {
            JOptionPane.showMessageDialog(null, "Project name cannot contain leading and trailing spaces",
                    "INVALID NAME", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<String> projects = new ArrayList<>();
        File directory = new File("data");
        File[] filesList = directory.listFiles();
        if (filesList != null) {
            for (File file : filesList) {
                if (file.isDirectory()) {
                    projects.add(file.getName());
                }
            }
        }
        if (projects.contains(ProjectUtils.CURRENT_PROJECT)) {
            int currProjectIndex = projects.indexOf(ProjectUtils.CURRENT_PROJECT);
            projects.set(currProjectIndex, projects.get(0));
            projects.set(0, ProjectUtils.CURRENT_PROJECT);
        }

        if (projects.contains(name)) {
            JOptionPane.showMessageDialog(null, "'" + projectName + "' is existed!",
                    "INVALID NAME", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean isOpen() {
        return isOpen;
    }
}
