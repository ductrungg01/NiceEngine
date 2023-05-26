package editor;

import components.Sprite;
import components.Spritesheet;
import editor.uihelper.ButtonColor;
import editor.uihelper.ColorHelp;
import editor.uihelper.NiceImGui;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import system.Window;
import util.AssetPool;
import util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;
import static editor.uihelper.NiceShortCall.COLOR_Blue;

public class FileDialog {
    //region Singleton
    private FileDialog() {
    }

    private static FileDialog instance = null;

    public static FileDialog getInstance() {
        if (instance == null) {
            instance = new FileDialog();
        }

        return instance;
    }
    //endregion

    private Object selectedObject;
    private boolean isOpen;
    private String idWaiting = "";
    private ReferenceType referenceType = null;
    private List<Object> itemList = new ArrayList<>();
    private List<Spritesheet> spritesheetList = new ArrayList<>();

    public void open(String idWaiting, ReferenceType typeRequest) {
        selectedObject = null;
        isOpen = true;
        this.idWaiting = idWaiting;
        this.referenceType = typeRequest;
        getItemList();
    }

    private void getItemList() {
        if (this.referenceType == null) {
            itemList.addAll(FileUtils.getAllFiles());
        } else {
            itemList.addAll(FileUtils.getFilesWithReferenceConfig(new ReferenceConfig(referenceType)));
        }
    }

    public void render() {
        if (isOpen)
            ImGui.openPopup("FileDialog");

        if (ImGui.beginPopupModal("FileDialog")) {
            if (ImGui.beginTabBar("FileDialogTabBar")) {
                showSpriteSheet();

                if (ImGui.beginTabItem("SPRITE")) {
                    ImGui.text("Select an image below!");

                    //region Content Tab1
                    ImGui.beginChild("fileDialog", ImGui.getContentRegionMaxX(), ImGui.getContentRegionMaxY() * 0.8f, true);
                    final float iconWidth = 150f;
                    final float iconHeight = 150f;
                    final float iconSize = iconHeight;
                    final float spacingX = 20.0f;
                    final float spacingY = 50.0f;
                    float availableWidth = ImGui.getContentRegionAvailX();
                    int itemsPerRow = (int) (availableWidth / (iconWidth + spacingX));

                    int itemIndex = 0;
                    for (Object item : itemList) {
                        // Calculate position for this item
                        float posX = (itemIndex % itemsPerRow) * (iconWidth + spacingX);
                        float posY = (itemIndex / itemsPerRow) * (iconHeight + spacingY);

                        // Set item position and size
                        ImGui.setItemAllowOverlap();
                        ImGui.setCursorPos(posX, posY);

                        int itemState = drawAnItem(item, iconSize);

                        if (itemState == 3) {
                            setSelectedObject(item);
                            close();
                            ImGui.closeCurrentPopup();
                            break;
                        }

                        itemIndex++;
                    }

                    ImGui.endChild();
                    //endregion

                    ImGui.endTabItem();
                }

                ImGui.endTabBar();
            }

            if (NiceImGui.drawButton("Cancel", new ButtonColor(COLOR_DarkRed, COLOR_Red, COLOR_Red))) {
                close();
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }

    private void showSpriteSheet() {
        if (this.spritesheetList.isEmpty()) {
            this.spritesheetList = AssetPool.getAllSpritesheets();
        }

        if (ImGui.beginTabItem("SPRITESHEET")) {
            ImGui.text("Select an spritesheet tab and choose an sprite below!");
            if (ImGui.beginTabBar("FileDialogSpritesheetTabBar")) {
                for (Spritesheet spritesheet : spritesheetList) {
                    String spritesheetName = FileUtils.getFileName(spritesheet.getTexture().getFilePath());
                    if (ImGui.beginTabItem(spritesheetName)) {
                        ImGui.endTabItem();
                    }
                }

                ImGui.endTabBar();
            }
            ImGui.endTabItem();
        }
    }

    // 0: is show, and nothing
    // 1: hover
    // 2: click
    // 3: double click
    public int drawAnItem(Object item, float iconSize) {
        int returnValue = 0;

        String id = item.toString();
        Sprite icon = new Sprite();
        String shortItemName = "";
        String fullItemName = "";

        if (item instanceof File) {
            File file = (File) item;
            id = file.getPath();
            icon = FileUtils.getIconByFile(file);
            shortItemName = FileUtils.getShorterName(file.getName());
            fullItemName = file.getName();
        } else if (item instanceof GameObject) {
            GameObject go = (GameObject) item;
            id = go.name;
            icon = FileUtils.getGameObjectIcon();
            shortItemName = FileUtils.getShorterName(go.name);
            fullItemName = go.name;
        } else if (item instanceof Sprite) {
            Sprite spr = (Sprite) item;
            id = spr.getTexture().getFilePath();
            shortItemName = FileUtils.getShorterName(spr.getTexture().getFilePath());
            fullItemName = spr.getTexture().getFilePath();
        }

        ImGui.pushID(id);

        float posX = ImGui.getCursorPosX();
        float posY = ImGui.getCursorPosY();

        Vector4f hoveredColor = ColorHelp.ColorChangeAlpha(COLOR_LightBlue, 0.3f);
        Vector4f activeColor = COLOR_Blue;

        //region draw the icon
        ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0.0f);  // No color
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, hoveredColor.x, hoveredColor.y, hoveredColor.z, hoveredColor.w);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, activeColor.x, activeColor.y, activeColor.z, activeColor.w);
        ImGui.imageButton(icon.getTexId(), iconSize, iconSize);
        ImGui.popStyleColor(3);
        //endregion

        // write the file name
        // set the cursor pos is below of icon
        final float offsetOfIconAndName = 5f;
        ImGui.setCursorPos(posX + 5f, posY + iconSize + offsetOfIconAndName);
        if (ImGui.isItemHovered()) {
            if (ImGui.isMouseDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                ImGui.pushStyleColor(ImGuiCol.Text, activeColor.x, activeColor.y, activeColor.z, activeColor.w);
                ImGui.text(shortItemName);
                ImGui.popStyleColor(1);

                returnValue = 3;
            } else if (ImGui.isItemClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                ImGui.pushStyleColor(ImGuiCol.Text, activeColor.x, activeColor.y, activeColor.z, activeColor.w);
                ImGui.text(shortItemName);
                ImGui.popStyleColor(1);
                returnValue = 2;
            } else {
                ImGui.pushStyleColor(ImGuiCol.Text, hoveredColor.x, hoveredColor.y, hoveredColor.z, activeColor.w);
                ImGui.text(shortItemName);
                ImGui.popStyleColor(1);
                returnValue = 1;
            }
        } else {
            ImGui.text(shortItemName);
        }

        // End item
        ImGui.popID();

        return returnValue;
    }

    private void setSelectedObject(Object item) {
        if (this.referenceType == null) {
            this.selectedObject = item;
        } else {
            switch (this.referenceType) {
                case SPRITE -> {
                    File itemFile = (File) item;
                    Sprite spr = new Sprite(itemFile.getPath());
                    this.selectedObject = spr;
                }
                case SOUND, JAVA -> {
                    File itemFile = (File) item;
                    this.selectedObject = itemFile;
                }
                case GAMEOBJECT -> {
                    this.selectedObject = (GameObject) item;
                }
            }
        }
    }

    public void close() {
        this.isOpen = false;
        this.referenceType = null;
        this.itemList.clear();
        this.spritesheetList.clear();
    }

    public Object getSelectedObject(String idWaiting, Object oldValue) {
        if (!this.idWaiting.equals(idWaiting) || isOpen || this.selectedObject == null) {
            return oldValue;
        }

        Object o = this.selectedObject;
        this.idWaiting = "";
        selectedObject = null;

        return o;
    }
}
