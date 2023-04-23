package editor;

import components.MouseControls;
import components.Sprite;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import org.lwjgl.glfw.GLFW;
import scenes.LevelEditorSceneInitializer;
import scenes.Scene;
import system.GameObject;
import system.Prefabs;
import system.Window;
import util.AssetPool;
import util.FileChecker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class AssetsWindow {
    private final String FOLDER_ICON = "assets/images/folder-icon.png";
    private final String LEFT_ARROW_ICON = "assets/images/left-arrow-icon.png";
    private final String RIGHT_ARROW_ICON = "assets/images/right-arrow-icon.png";
    private final String JAVA_ICON = "assets/images/java-icon.png";
    private final String ROOT_FOLDER = "assets";

    private boolean rename = false;
    private String selectedItem = "";
    private String currentOpenFolder = ROOT_FOLDER;
    private ArrayList<String> previousFolder = new ArrayList<>();
    private ArrayList<String> nextFolder = new ArrayList<>();
    private boolean showMsb = false;
    private TypeOfMsb typeOfMsb;
    private String msbText = "";

    private GameObject gameObject = new GameObject("");
    private Scene scene;

    private enum TypeOfMsb {
        ERROR,
        CREATE_FILE,
        CREATE_FILE_SUCCESS,
    }

    public AssetsWindow() {
        gameObject.addComponent(new MouseControls());
    }

    public void handleItemSelect(File item, boolean isFolder) {
        String itemName = item.getName();
        if (scene == null) {
            scene = Window.getScene();
            scene.addGameObjectToScene(gameObject);
        }
        //rename
        if (!rename || !selectedItem.equals(itemName)) {
            if (ImGui.button(itemName)) {
                selectedItem = itemName;
            }
        }
        if (rename && selectedItem.equals(itemName)) {
            String[] newName = JImGui.inputTextNoLabel(itemName);
            if (newName[0].equals("true")) {
                File srcFile = new File(currentOpenFolder + "/" + itemName);
                File desFile = new File(currentOpenFolder + "/" + newName[1]);
                boolean rename = srcFile.renameTo(desFile);
                if (!rename) {
                    showMsb = true;
                    typeOfMsb = TypeOfMsb.CREATE_FILE_SUCCESS;
                    msbText = "Rename failed";
                }
            }

        }

        //item hovered action
        if (ImGui.isItemHovered()) {
            if (ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                System.out.println("hover & right click " + itemName);
                ImGui.openPopup("Item popup");
            } else if (ImGui.isMouseDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                if (isFolder) {
                    previousFolder.add(currentOpenFolder);
                    currentOpenFolder += "/" + itemName;
                    nextFolder.clear();
                } else {
                    try {
                        String command = "explorer.exe \"" + item.getAbsolutePath() + "\"";
                        Runtime.getRuntime().exec(command);
                    } catch (IOException e) {
                        showMsb = true;
                        typeOfMsb = TypeOfMsb.ERROR;
                        if (isFolder) msbText = "Error! Can't open folder";
                        else msbText = "Error! Can't open file";
                    }
                }
            } else if (ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !isFolder && FileChecker.isImageFile(item)) {
                Sprite tmp = new Sprite();
                tmp.setTexture(AssetPool.getTexture(item.getPath()));
                GameObject object = Prefabs.generateSpriteObject(tmp, 0.25f, 0.25f);
                gameObject.getComponent(MouseControls.class).pickupObject(object);
            }
        }


        if (ImGui.beginPopup("Item popup")) {
            ImGui.pushID("Item popup");
            if (ImGui.menuItem("Rename")) {
                rename = true;
                selectedItem = itemName;
            }
            if (ImGui.menuItem("Open in Explorer")) {
                try {
                    String command;
                    if (isFolder) {
                        command = "explorer.exe \"" + item.getAbsolutePath() + "\"";
                    } else {
                        File tmp = new File(currentOpenFolder);
                        command = "explorer.exe \"" + tmp.getAbsolutePath() + "\"";
                    }
                    Runtime.getRuntime().exec(command);
                } catch (IOException e) {
                    showMsb = true;
                    typeOfMsb = TypeOfMsb.ERROR;
                    if (isFolder) msbText = "Error! Can't open folder";
                    else msbText = "Error! Can't open file";
                }
            }

            ImGui.popID();
            ImGui.endPopup();
        }


        //check click, check if click outside rename input, use hoverany
        if (ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            if (!ImGui.isAnyItemHovered()) {
                rename = false;
            }
        }
    }

    public void imgui() {

        //region Assets
        ImGui.begin("Assets");
        Sprite spr = new Sprite();

        //region Breadcrum
        spr.setTexture(AssetPool.getTexture(LEFT_ARROW_ICON));
        if (ImGui.imageButton(spr.getTexId(), 20, 20) && previousFolder.size() > 0) {
            nextFolder.add(currentOpenFolder);
            currentOpenFolder = previousFolder.get(previousFolder.size() - 1);
            previousFolder.remove(previousFolder.size() - 1);

        }
        ImGui.sameLine();
        spr.setTexture(AssetPool.getTexture(RIGHT_ARROW_ICON));
        if (ImGui.imageButton(spr.getTexId(), 20, 20) && nextFolder.size() > 0) {
            previousFolder.add(currentOpenFolder);
            currentOpenFolder = nextFolder.get(nextFolder.size() - 1);
            nextFolder.remove(nextFolder.size() - 1);
        }
        ImGui.sameLine();

        String breadcrum[] = currentOpenFolder.split("/");
        for (int i = 0; i < breadcrum.length; i++) {
            ImGui.sameLine();
            ImGui.pushID(i);
            if (i == 0) {
                if (ImGui.button(breadcrum[i])) {
                    if (!breadcrum[i].equals(currentOpenFolder))
                        previousFolder.add(currentOpenFolder);
                    currentOpenFolder = breadcrum[i];
                    nextFolder.clear();
                }
            } else {
                ImGui.text(">");
                ImGui.sameLine();
                breadcrum[i] = breadcrum[i - 1] + "/" + breadcrum[i];
                if (ImGui.button(breadcrum[i].substring(breadcrum[i].lastIndexOf("/") + 1))) {
                    if (!breadcrum[i].equals(currentOpenFolder))
                        previousFolder.add(currentOpenFolder);
                    currentOpenFolder = breadcrum[i];
                    nextFolder.clear();
                }
            }
            ImGui.popID();
        }

        ImGui.dummy(0, 8);
        //endregion

        //region Load file & folder
        File folder = new File(currentOpenFolder);
        File[] listOfFiles = folder.listFiles();

        //sort file on top
        Arrays.sort(listOfFiles, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if (file1.isDirectory() && file2.isFile()) {
                    return 1;
                } else if (file1.isFile() && file2.isDirectory()) {
                    return -1;
                } else {
                    return file1.compareTo(file2);
                }
            }
        });

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                ImGui.pushID(i);
                if (FileChecker.isImageFile(listOfFiles[i])) {
                    spr.setTexture(AssetPool.getTexture(listOfFiles[i].getPath()));
                    ImGui.image(spr.getTexId(), 28, 28);
                    ImGui.sameLine();
                } else if (FileChecker.checkFileExtension("java", listOfFiles[i])) {
                    spr.setTexture(AssetPool.getTexture(JAVA_ICON));
                    ImGui.image(spr.getTexId(), 28, 28);
                    ImGui.sameLine();
                } else {
                    ImGui.popID();
                    continue;
                }
                handleItemSelect(listOfFiles[i], false);
                ImGui.popID();
                ImGui.dummy(0, 4);

            } else if (listOfFiles[i].isDirectory()) {
                ImGui.pushID(i);
                spr.setTexture(AssetPool.getTexture(FOLDER_ICON));
                ImGui.image(spr.getTexId(), 28, 28);
                ImGui.sameLine();

                handleItemSelect(listOfFiles[i], true);

                ImGui.popID();
                ImGui.dummy(0, 4);
            }
        }
        //endregion

        //region Menu Context
        if (ImGui.isWindowHovered() && !ImGui.isAnyItemHovered() && ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            ImGui.openPopup("Menu Context");
        }

        if (ImGui.beginPopup("Menu Context")) {
            ImGui.pushID("Menu Context");

            if (ImGui.menuItem("New folder")) {
                ImGui.beginChild("child new folder");
                int tmp = 1;
                File theDir = new File(currentOpenFolder + "/New folder");
                while (theDir.exists()) {
                    theDir = new File(currentOpenFolder + "/New folder (" + tmp + ")");
                    tmp++;
                }
                theDir.mkdirs();
                ImGui.endChild();
                rename = true;
                selectedItem = theDir.getName();
            }

            if (ImGui.menuItem("Create file")) {
                showMsb = true;
                typeOfMsb = TypeOfMsb.CREATE_FILE;
                msbText = "[Create file] Enter file name";
            }

            if (ImGui.menuItem("Open folder in Explorer")) {
                try {
                    File tmp = new File(currentOpenFolder);
                    String command = "explorer.exe \"" + tmp.getAbsolutePath() + "\"";
                    Runtime.getRuntime().exec(command);
                } catch (IOException e) {
                    showMsb = true;
                    typeOfMsb = TypeOfMsb.ERROR;
                    msbText = "Error! Can't open folder";
                }
            }
            ImGui.popID();
            ImGui.endPopup();
        }
        //endregion

        //region Message Box
        ImGui.setNextWindowSize(500, 200);
        if (showMsb) {
            ImGui.openPopup("Message Box");
        }
        if (ImGui.beginPopupModal("Message Box")) {
            ImGui.text(msbText);
            ImGui.separator();

            switch (typeOfMsb) {
                case CREATE_FILE:
                    String newFile[] = JImGui.inputTextNoLabel("");
                    ImGui.text("Press enter to confirm");
                    if (ImGui.button("Cancel")) {
                        showMsb = false;
                        ImGui.closeCurrentPopup();
                    }
                    if (newFile[0].equals("true")) {
                        File file = new File(currentOpenFolder + "/" + newFile[1]);
                        try {
                            if (file.createNewFile()) {
                                showMsb = true;
                                typeOfMsb = TypeOfMsb.CREATE_FILE_SUCCESS;
                                msbText = "File created";
                            } else {
                                showMsb = true;
                                typeOfMsb = TypeOfMsb.ERROR;
                                msbText = "Error! File already exist";

                            }
                        } catch (IOException e) {
                            showMsb = true;
                            typeOfMsb = TypeOfMsb.ERROR;
                            msbText = "Error! Can't create file";
                        }
                    }
                    break;
                default:
                    if (ImGui.button("OK")) {
                        showMsb = false;
                        ImGui.closeCurrentPopup();
                    }
                    break;
            }


            ImGui.endPopup();
        }

        ImGui.end();
        //endregion

        //endregion
    }
}