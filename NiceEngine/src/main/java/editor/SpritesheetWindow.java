package editor;

import components.Sprite;
import components.Spritesheet;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import system.Prefabs;
import system.Window;
import util.AssetPool;
import util.FileUtils;
import util.Settings;

import javax.swing.*;
import java.util.List;

public class SpritesheetWindow {
    //region Singleton
    private SpritesheetWindow() {
    }

    private static SpritesheetWindow instance = null;

    public static SpritesheetWindow getInstance() {
        if (instance == null) {
            instance = new SpritesheetWindow();
        }

        return instance;
    }
    //endregion

    static float BUTTON_SIZE_BOOST = 1;
    static float BUTTON_SIZE_BOOST_DEFAULT_VALUE = 1;
    static float DEFAULT_SPRITESHEET_BUTTON_SIZE = 32;

    private static Sprite SPRITE_WAITING = null;
    public static String spritesheet_has_just_add = "";

    //region Methods
    public void imgui() {
        ImGui.setNextWindowSizeConstraints(Settings.MIN_WIDTH_GROUP_WIDGET, Settings.MIN_HEIGHT_GROUP_WIDGET, Window.getWidth(), Window.getHeight());

        ImGui.begin("Spritesheet");

        settings();

        ImGui.text("Select a spritesheet tab and see sprite list below!");

        List<Spritesheet> spritesheets = AssetPool.getAllSpritesheets();
        for (int i = 0; i < spritesheets.size(); i++) {
            String sprsheetName = FileUtils.getFileName(spritesheets.get(i).getTexture().getFilePath());
            if (ImGui.beginTabBar("SpritesheetLoaderTabBar")) {
                ImBoolean pOpen = new ImBoolean(false);
                if (!spritesheet_has_just_add.equals("")) {
                    if (sprsheetName.equals(spritesheet_has_just_add)) {
                        spritesheet_has_just_add = "";
                        pOpen = new ImBoolean(true);
                    }
                } else {
                    pOpen = new ImBoolean(true);
                }

                boolean previousOpen = pOpen.get();

                if (ImGui.beginTabItem(sprsheetName, pOpen)) {
                    ImVec2 windowPos = new ImVec2();
                    ImGui.getWindowPos(windowPos);
                    ImVec2 windowSize = new ImVec2();
                    ImGui.getWindowSize(windowSize);
                    ImVec2 itemSpacing = new ImVec2();
                    ImGui.getStyle().getItemSpacing(itemSpacing);
                    float windowX2 = windowPos.x + windowSize.x;
                    int sprsheetLength = spritesheets.get(i).size();

                    for (int j = 0; j < sprsheetLength; j++) {
                        Sprite sprite = spritesheets.get(i).getSprite(j);
                        float offset = Math.min(DEFAULT_SPRITESHEET_BUTTON_SIZE / sprite.getWidth(), DEFAULT_SPRITESHEET_BUTTON_SIZE / sprite.getHeight());
                        float spriteWidth = sprite.getWidth() * offset * BUTTON_SIZE_BOOST;
                        float spriteHeight = sprite.getHeight() * offset * BUTTON_SIZE_BOOST;
                        Vector2f[] texCoords = sprite.getTexCoords();

                        ImGui.pushID(sprite.getTexId() + sprsheetName + j);
                        if (ImGui.imageButton(sprite.getTexId(), spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
                            GameObject newGo = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f,
                                    sprsheetName + "(" + j + ")");
                            Window.getScene().getMouseControls().pickupObject(newGo);
                        }
                        if (ImGui.isItemHovered()) {
                            ImGui.beginTooltip();
                            ImGui.text("Spritesheet: " + sprsheetName + "\nIndex: " + j);
                            ImGui.endTooltip();
                        }
                        ImGui.popID();

                        ImVec2 lastButtonPos = new ImVec2();
                        ImGui.getItemRectMax(lastButtonPos);
                        float lastButtonX2 = lastButtonPos.x;
                        float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                        if (j + 1 < sprsheetLength && nextButtonX2 <= windowX2) {
                            ImGui.sameLine();
                        }
                    }

                    if (ImGui.isWindowHovered() && ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                        ImGui.openPopup("Right Menu Context of " + sprsheetName);
                    }
                    if (ImGui.beginPopup("Right Menu Context of " + sprsheetName)) {
                        if (ImGui.menuItem("Remove this spritesheet")) {
                            String resourceName = spritesheets.get(i).getTexture().getFilePath();
                            AssetPool.removeSpritesheet(resourceName);
                        }
                        ImGui.endPopup();
                    }

                    ImGui.endTabItem();
                }

                if (previousOpen && !pOpen.get()) {
                    int response = JOptionPane.showConfirmDialog(null,
                            "Remove spritesheet '" + sprsheetName + "'?",
                            "REMOVE SPRITESHEET",
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        Debug.Log(sprsheetName + " has just removed!");
                        AssetPool.removeSpritesheet(spritesheets.get(i).getTexture().getFilePath());
                    }
                }

                ImGui.endTabBar();
            }
        }
        ImGui.end();
    }

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

        BUTTON_SIZE_BOOST = NiceImGui.dragfloat("Button size boost: ", BUTTON_SIZE_BOOST, 0.01f, 100000, "SpritesheetLoaderTabBar BUTTON_SIZE_BOOST");
        ImGui.sameLine();
        if (ImGui.button("Reset")) {
            BUTTON_SIZE_BOOST = BUTTON_SIZE_BOOST_DEFAULT_VALUE;
        }

        ImGui.endChild();
        //endregion

        ImGui.separator();
    }
    //endregion
}
