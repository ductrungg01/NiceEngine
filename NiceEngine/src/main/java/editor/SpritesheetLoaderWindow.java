package editor;

import components.Sprite;
import components.Spritesheet;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import util.AssetPool;
import util.FileUtils;

import java.util.List;

public class SpritesheetLoaderWindow {
    //region Singleton
    private SpritesheetLoaderWindow() {
    }

    private static SpritesheetLoaderWindow instance = null;

    public static SpritesheetLoaderWindow getInstance() {
        if (instance == null) {
            instance = new SpritesheetLoaderWindow();
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
        ImGui.begin("Spritesheet loader");

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

                        ImGui.pushID(sprite.getTexId());
                        if (ImGui.imageButton(sprite.getTexId(), spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
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
