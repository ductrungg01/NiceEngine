package editor.windows;

import components.Sprite;
import editor.NiceImGui;
import system.Spritesheet;
import editor.uihelper.ButtonColor;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import system.Window;
import util.AssetPool;
import util.FileUtils;
import util.Settings;

import static editor.uihelper.NiceShortCall.*;

public class AddingSpritesheetWindow {
    //region Singleton
    private AddingSpritesheetWindow() {
    }

    private static AddingSpritesheetWindow instance = null;

    public static AddingSpritesheetWindow getInstance() {
        if (instance == null) {
            instance = new AddingSpritesheetWindow();
        }

        return instance;
    }
    //endregion

    private boolean isOpen = false;
    private Sprite sprite = null;

    // SPRITESHEET SETTINGS
    public int sprWidth = 1;
    public int sprHeight = 1;
    public int numSprites = 1;
    public int sprSpacingX = 0;
    public int sprSpacingY = 0;

    public boolean isAdd = true;

    public void open(Sprite sprite) {
        this.isOpen = true;
        this.sprite = sprite;

        this.sprWidth = 1;
        this.sprHeight = 1;
        this.numSprites = 1;
        this.sprSpacingX = 0;
        this.sprSpacingY = 0;
        this.isAdd = true;
    }

    public void spritesheetPreview() {
        String popupId = (this.isAdd ? "Adding new spritesheet" : "Edit spritesheet");

        if (this.isOpen) {
            ImGui.openPopup(popupId);

            ImGui.setNextWindowSizeConstraints(Window.getWidth() * 0.75f, Window.getHeight() * 0.75f, Window.getWidth(), Window.getHeight());
        }

        if (ImGui.beginPopupModal(popupId, new ImBoolean(this.isOpen))) {
            ImGui.columns(2);
            final float SETTING_COLUMN_WIDTH = 300f;
            ImGui.setColumnWidth(0, SETTING_COLUMN_WIDTH);

            //region SPRITESHEET PREVIEW SETTINGS
            ImGui.text("SPRITESHEET SETTING PREVIEW");
            ImGui.beginChild("##SpritesheetSettingPreview", SETTING_COLUMN_WIDTH, 300);
            this.sprWidth = NiceImGui.dragInt("Sprite width: ", sprWidth, 0, this.sprite.getTexture().getWidth());
            this.sprHeight = NiceImGui.dragInt("Sprite Height", sprHeight, 0, this.sprite.getTexture().getHeight());
            this.numSprites = NiceImGui.dragInt("Nums of sprites: ", numSprites, 0, Integer.MAX_VALUE);
            this.sprSpacingX = NiceImGui.dragInt("Spacing X: ", sprSpacingX, 0, this.sprite.getTexture().getWidth());
            this.sprSpacingY = NiceImGui.dragInt("Spacing Y: ", sprSpacingY, 0, this.sprite.getTexture().getHeight());

            if (NiceImGui.drawButton((this.isAdd ? "ADD NEW SPRITESHEET" : "SAVE"),
                    new ButtonColor(COLOR_DarkBlue, COLOR_Blue, COLOR_Blue),
                    new Vector2f(SETTING_COLUMN_WIDTH, 50f))) {
                if (this.isAdd)
                    addSpritesheetToAssetPool();
                else
                    updateSpritesheetToAssetPool();
                close();
            }

            if (NiceImGui.drawButton("CANCEL",
                    new ButtonColor(COLOR_DarkRed, COLOR_Red, COLOR_Red),
                    new Vector2f(SETTING_COLUMN_WIDTH, 30f))) {
                close();
            }
            ImGui.endChild();
            //endregion

            ImGui.nextColumn();

            //region SPRITESHEET PREVIEW
            float imageSizeX = this.sprite.getTexture().getWidth();
            float imageSizeY = this.sprite.getTexture().getHeight();

            final float SPRITESHEET_PREVIEW_MINIMUM_SIZE_X = 500f;
            final float SPRITESHEET_PREVIEW_MINIMUM_SIZE_Y = 500f;

            float offset = Math.min(SPRITESHEET_PREVIEW_MINIMUM_SIZE_X / imageSizeX,
                    SPRITESHEET_PREVIEW_MINIMUM_SIZE_Y / imageSizeY);
            float sizeToShowImageX = this.sprite.getTexture().getWidth() * offset;
            float sizeToShowImageY = this.sprite.getTexture().getHeight() * offset;

            float cursorPosX = ImGui.getCursorScreenPosX();
            float cursorPosY = ImGui.getCursorScreenPosY();

            ImGui.image(this.sprite.getTexId(), sizeToShowImageX, sizeToShowImageY);

            drawLineToEasyPreview(new Vector2f(cursorPosX, cursorPosY), new Vector2f(sizeToShowImageX, sizeToShowImageY));
            //endregion

            ImGui.columns(1);
            ImGui.endPopup();
        }
    }

    private void addSpritesheetToAssetPool() {
        String spritesheetName = FileUtils.getFileName(this.sprite.getTexture().getFilePath());

        AssetPool.addSpritesheet(spritesheetName,
                new Spritesheet(this.sprite.getTexture(), this.sprWidth, this.sprHeight, this.numSprites, this.sprSpacingX, this.sprSpacingY));

        SpritesheetWindow.spritesheet_has_just_add = spritesheetName;
    }

    private void updateSpritesheetToAssetPool() {
        String spritesheetName = FileUtils.getFileName(this.sprite.getTexture().getFilePath());

        AssetPool.updateSpritesheet(spritesheetName,
                new Spritesheet(this.sprite.getTexture(), this.sprWidth, this.sprHeight, this.numSprites, this.sprSpacingX, this.sprSpacingY));

        SpritesheetWindow.spritesheet_has_just_add = spritesheetName;
    }

    private void close() {
        this.isOpen = false;
    }

    public boolean isOpened() {
        return isOpen;
    }

    private void drawLineToEasyPreview(Vector2f imgTLPos, Vector2f imgSize) {
        float cursorPosX = ImGui.getCursorScreenPosX();
        float cursorPosY = ImGui.getCursorScreenPosY();

        Vector2f imgBRPos = new Vector2f(cursorPosX + imgSize.x, cursorPosY);

        float offsetX = (imgBRPos.x - imgTLPos.x);
        float offsetY = (imgBRPos.y - imgTLPos.y);


        final float IMAGE_SIZE_X = this.sprite.getTexture().getWidth();
        final float IMAGE_SIZE_Y = this.sprite.getTexture().getHeight();
        float currentX = 0;
        float currentY = 0;

        for (int i = 0; i < this.numSprites; i++) {
            float topY = (currentY) / IMAGE_SIZE_Y;
            float rightX = (currentX + sprWidth) / IMAGE_SIZE_X;
            float leftX = currentX / IMAGE_SIZE_X;
            float bottomY = (currentY + sprHeight) / IMAGE_SIZE_Y;

            Vector2f topLeftPos = new Vector2f(leftX * offsetX + imgTLPos.x, topY * offsetY + imgTLPos.y);
            Vector2f bottomRightPos = new Vector2f(rightX * offsetX + imgTLPos.x, bottomY * offsetY + imgTLPos.y);

            // draw
            draw4lines(topLeftPos, bottomRightPos);

            currentX += sprWidth + sprSpacingX;
            if (currentX >= IMAGE_SIZE_X) {
                // next line
                currentX = 0;
                currentY += sprHeight + sprSpacingY;
            }
        }
    }

    private void draw4lines(Vector2f topLeftPos, Vector2f bottomRightPos) {
        ImDrawList drawList = ImGui.getWindowDrawList();

        int color = ImColor.intToColor(255, 255, 255, 100);
        float lineSize = 0.3f;

        drawList.addLine(topLeftPos.x, topLeftPos.y, bottomRightPos.x, topLeftPos.y, color, lineSize);
        drawList.addLine(bottomRightPos.x, topLeftPos.y, bottomRightPos.x, bottomRightPos.y, color, lineSize);
        drawList.addLine(bottomRightPos.x, bottomRightPos.y, topLeftPos.x, bottomRightPos.y, color, lineSize);
        drawList.addLine(topLeftPos.x, bottomRightPos.y, topLeftPos.x, topLeftPos.y, color, lineSize);
    }
}
