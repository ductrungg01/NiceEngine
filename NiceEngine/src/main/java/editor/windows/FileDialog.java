package editor.windows;

import components.Sprite;
import editor.NiceImGui;
import editor.ReferenceType;
import system.Spritesheet;
import editor.uihelper.ButtonColor;
import editor.uihelper.ColorHelp;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import org.joml.Vector4f;
import system.GameObject;
import system.Window;
import util.AssetPool;
import util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;
import static editor.uihelper.NiceShortCall.COLOR_Blue;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

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
    static float BUTTON_SIZE_BOOST = 1;
    static float BUTTON_SIZE_BOOST_DEFAULT_VALUE = 1;
    public static String spritesheet_has_just_used = "";
    public boolean showSpritesheetAlso = true;

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
            itemList.addAll(FileUtils.getFilesWithReferenceType(referenceType));
        }
    }

    public void render() {
        if (isOpen) {
            ImGui.openPopup("FileDialog");
            ImGui.setNextWindowSizeConstraints(Window.getWidth() * 0.75f, Window.getHeight() * 0.75f, Window.getWidth(), Window.getHeight());
        }
        if (ImGui.beginPopupModal("FileDialog")) {
            if (ImGui.beginTabBar("FileDialogTabBar")) {

                if (this.showSpritesheetAlso)
                    showSpriteSheet();

                if (ImGui.beginTabItem("SPRITE")) {
                    ImGui.text("Select an image below!");

                    //region Content
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

        Sprite spriteChosen = null;

        final float DEFAULT_SPRITE_BUTTON_SIZE = 32;

        if (ImGui.beginTabItem("SPRITESHEET")) {
            settings();

            ImGui.text("Select an spritesheet tab and choose an sprite below!");
            if (ImGui.beginTabBar("FileDialogSpritesheetTabBar")) {
                for (Spritesheet spritesheet : spritesheetList) {
                    String spritesheetName = FileUtils.getFileName(spritesheet.getTexture().getFilePath());

                    ImBoolean pOpen = new ImBoolean(true);
                    if (!spritesheet_has_just_used.equals("")) {
                        if (!spritesheetName.equals(spritesheet_has_just_used)) {
                            pOpen = new ImBoolean(false);
                        }
                    }

                    if (ImGui.beginTabItem(spritesheetName, pOpen)) {
                        if (!spritesheetName.equals(spritesheet_has_just_used)) {
                            spritesheet_has_just_used = "";
                        }

                        ImGui.beginChild("##Select sprite from spritesheet on FileDialog with spritesheet " + spritesheetName, ImGui.getContentRegionMaxX(), ImGui.getContentRegionMaxY() * 0.75f, true);

                        ImVec2 windowPos = new ImVec2();
                        ImGui.getWindowPos(windowPos);
                        ImVec2 windowSize = new ImVec2();
                        ImGui.getWindowSize(windowSize);
                        ImVec2 itemSpacing = new ImVec2();
                        ImGui.getStyle().getItemSpacing(itemSpacing);
                        float windowX2 = windowPos.x + windowSize.x;

                        for (int i = 0; i < spritesheet.size(); i++) {
                            Sprite spr = spritesheet.getSprite(i);
                            Vector2f[] texCoords = spr.getTexCoords();
                            float offset = Math.min(DEFAULT_SPRITE_BUTTON_SIZE / spr.getWidth(), DEFAULT_SPRITE_BUTTON_SIZE / spr.getHeight());
                            float spriteWidth = spr.getWidth() * offset * BUTTON_SIZE_BOOST;
                            float spriteHeight = spr.getHeight() * offset * BUTTON_SIZE_BOOST;
                            ImGui.pushID(spr.getTexId());
                            // If select this sprite
                            if (ImGui.imageButton(spr.getTexId(), spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
                            }

                            if (ImGui.isItemHovered()) {
                                if (ImGui.isMouseDoubleClicked(GLFW_MOUSE_BUTTON_LEFT)) {
                                    spriteChosen = spr;
                                }
                            }
                            ImGui.popID();

                            ImVec2 lastButtonPos = new ImVec2();
                            ImGui.getItemRectMax(lastButtonPos);
                            float lastButtonX2 = lastButtonPos.x;
                            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                            if (i + 1 < spritesheet.size() && nextButtonX2 <= windowX2) {
                                ImGui.sameLine();
                            }

                            if (spriteChosen != null) break;

                        }
                        ImGui.endChild();
                        ImGui.endTabItem();
                    }
                }

                ImGui.endTabBar();
            }
            ImGui.endTabItem();
        }

        if (spriteChosen != null) {
            setSelectedObject(spriteChosen);
            close();
            ImGui.closeCurrentPopup();
        }
    }

    private static Sprite SPRITE_WAITING = null;

    private void settings() {
        //region ADD NEW SPRITESHEET
        final String ID_WAITING_FILE_DIALOG = "Add new spritesheet imgui id";
        if (ImGui.button("Add new spritesheet")) {
            FileDialog.getInstance().showSpritesheetAlso = false;
            FileDialog.getInstance().open(ID_WAITING_FILE_DIALOG, ReferenceType.SPRITE);
        }

        SPRITE_WAITING = (Sprite) FileDialog.getInstance().getSelectedObject(ID_WAITING_FILE_DIALOG, SPRITE_WAITING);

        if (SPRITE_WAITING != null && !AddingSpritesheetWindow.getInstance().isOpened()) {
            AddingSpritesheetWindow.getInstance().open(SPRITE_WAITING);
            SPRITE_WAITING = null;
        }
        //endregion

        //region BUTTON SIZE BOOST
        final float SETTINGS_WIDTH = 500f;

        ImGui.beginChild("ButtonSizeBoostOfSpritesheetLoaderWindow", SETTINGS_WIDTH, 30f);

        BUTTON_SIZE_BOOST = NiceImGui.dragFloat("Button size boost: ", BUTTON_SIZE_BOOST, 0.01f, 100000, "FileDialogSettingBoostButtonSizeForSpriteInSpritesheet");
        ImGui.sameLine();
        if (ImGui.button("Reset")) {
            BUTTON_SIZE_BOOST = BUTTON_SIZE_BOOST_DEFAULT_VALUE;
        }

        ImGui.endChild();
        //endregion

        ImGui.separator();
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
            icon = FileUtils.getIcon(FileUtils.ICON_NAME.GAME_OBJECT);
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
        NiceImGui.showImage(icon, new Vector2f(iconSize, iconSize), true, "Double-click to select this sprite!\n" + fullItemName + "\n" + ((File) item).getPath(), true, new Vector2f(300, 300));
        ImGui.popStyleColor(3);
        //endregion

        // write the file name
        // set the cursor pos is below of icon
        final float offsetOfIconAndName = 5f;
        ImGui.setCursorPos(posX + 5f, posY + iconSize + offsetOfIconAndName);
        if (ImGui.isItemHovered()) {
            if (ImGui.isMouseDoubleClicked(GLFW_MOUSE_BUTTON_LEFT)) {
                ImGui.pushStyleColor(ImGuiCol.Text, activeColor.x, activeColor.y, activeColor.z, activeColor.w);
                ImGui.text(shortItemName);
                ImGui.popStyleColor(1);

                returnValue = 3;
            } else if (ImGui.isItemClicked(GLFW_MOUSE_BUTTON_LEFT)) {
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
                    if (item instanceof File) {
                        File itemFile = (File) item;
                        Sprite spr = new Sprite(itemFile.getPath());
                        this.selectedObject = spr;
                    } else if (item instanceof Sprite) {
                        this.selectedObject = item;
                    }

                    GameViewWindow.getInstance().debounceTimeToCapture = 0.1f;
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
        this.showSpritesheetAlso = true;
    }

    public boolean isOpen() {
        return this.isOpen;
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
