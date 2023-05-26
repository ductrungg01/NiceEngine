package editor;

import components.Sprite;
import components.Spritesheet;
import editor.uihelper.NiceImGui;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
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

    static float BUTTON_SIZE_BOOST = 2;
    static float BUTTON_SIZE_BOOST_DEFAULT_VALUE = 2;

    //region Methods
    public void imgui() {
        ImGui.begin("Spritesheet loader");
        List<Spritesheet> spritesheets = AssetPool.getAllSpritesheets();
        for (int i = 0; i < spritesheets.size(); i++) {
            String sprsheetName = FileUtils.getFileName(spritesheets.get(i).getTexture().getFilePath());
            if (ImGui.beginTabBar("SpritesheetLoaderTabBar")) {
                if (ImGui.beginTabItem(sprsheetName)) {
                    ImVec2 windowPos = new ImVec2();
                    ImGui.getWindowPos(windowPos);
                    ImVec2 windowSize = new ImVec2();
                    ImGui.getWindowSize(windowSize);
                    ImVec2 itemSpacing = new ImVec2();
                    ImGui.getStyle().getItemSpacing(itemSpacing);
                    float windowX2 = windowPos.x + windowSize.x;
                    int sprsheetLength = spritesheets.get(i).size();


                    BUTTON_SIZE_BOOST = NiceImGui.dragfloat("Button size boost: ", BUTTON_SIZE_BOOST, "SpritesheetLoaderTabBar" + i + "btnsizeboost");
                    ImGui.sameLine();
                    if (ImGui.button("Reset")) {
                        BUTTON_SIZE_BOOST = BUTTON_SIZE_BOOST_DEFAULT_VALUE;
                    }

                    for (int j = 0; j < sprsheetLength; j++) {
                        Sprite sprite = spritesheets.get(i).getSprite(j);
                        float spriteWidth = sprite.getWidth() * BUTTON_SIZE_BOOST;
                        float spriteHeight = sprite.getHeight() * BUTTON_SIZE_BOOST;
                        Vector2f[] texCoords = sprite.getTexCoords();

                        ImGui.pushID(sprite.getTexId());
                        if (ImGui.imageButton(sprite.getTexId(), spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
                        }
                        ImGui.popID();

                        ImVec2 lastButtonPos = new ImVec2();
                        ImGui.getItemRectMax(lastButtonPos);
                        float lastButtonX2 = lastButtonPos.x;
                        float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                        if (i + 1 < sprsheetLength && nextButtonX2 < windowX2) {
                            ImGui.sameLine();
                        }
                    }
                    ImGui.endTabItem();
                }
                ImGui.endTabBar();
            }
        }
        ImGui.end();
    }
    //endregion
}
