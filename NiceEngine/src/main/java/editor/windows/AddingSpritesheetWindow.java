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
import util.AssetPool;
import util.FileUtils;

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
    private int sprWidth = 1;
    private int sprHeight = 1;
    private int numSprites = 1;
    private int sprSpacingX = 0;
    private int sprSpacingY = 0;

    public void open(Sprite sprite) {
        this.isOpen = true;
        this.sprite = sprite;

        this.sprWidth = 1;
        this.sprHeight = 1;
        this.numSprites = 1;
        this.sprSpacingX = 0;
        this.sprSpacingY = 0;
    }

    public void spritesheetPreview() {
        if (this.isOpen) {
            ImGui.openPopup("Adding new spritesheet");
        }

        if (ImGui.beginPopupModal("Adding new spritesheet", new ImBoolean(this.isOpen))) {
            ImGui.columns(2);
            final float SETTING_COLUMN_WIDTH = 300f;
            ImGui.setColumnWidth(0, SETTING_COLUMN_WIDTH);

            //region SPRITESHEET PREVIEW SETTINGS
            ImGui.text("SPRITESHEET SETTING PREVIEW");
            ImGui.beginChild("##SpritesheetSettingPreview", SETTING_COLUMN_WIDTH, 300);
            this.sprWidth = NiceImGui.dragInt("Sprite width: ", sprWidth);
            this.sprHeight = NiceImGui.dragInt("Sprite Height", sprHeight);
            this.numSprites = NiceImGui.dragInt("Number of sprites: ", numSprites);
            this.sprSpacingX = NiceImGui.dragInt("Spacing X: ", sprSpacingX);
            this.sprSpacingY = NiceImGui.dragInt("Spacing Y: ", sprSpacingY);
            if (NiceImGui.drawButton("ADD THIS SPRITESHEET",
                    new ButtonColor(COLOR_DarkBlue, COLOR_Blue, COLOR_Blue),
                    new Vector2f(SETTING_COLUMN_WIDTH, 50f))) {
                addSpritesheetToAssetPool();
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

            ImGui.image(this.sprite.getTexId(), sizeToShowImageX, sizeToShowImageY);

            drawLineToEasyPreview(new Vector2f(sizeToShowImageX, sizeToShowImageY));
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

    private void close() {
        this.isOpen = false;
    }

    public boolean isOpened() {
        return isOpen;
    }

    private void drawLineToEasyPreview(Vector2f sizeToShowImage) {
        ImDrawList drawList = ImGui.getWindowDrawList();
        float cursorPosX = ImGui.getCursorScreenPosX();
        float cursorPosY = ImGui.getCursorScreenPosY();

        Vector2f imgTLPos = new Vector2f(cursorPosX, cursorPosY - sizeToShowImage.y);
        Vector2f imgBRPos = new Vector2f(cursorPosX + sizeToShowImage.x, cursorPosY);

        float offsetX = (imgBRPos.x - imgTLPos.x);
        float offsetY = (imgBRPos.y - imgTLPos.y);

        int color = ImColor.intToColor(255, 255, 255, 100);
        float lineSize = 0.3f;

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
            drawList.addLine(topLeftPos.x, topLeftPos.y, bottomRightPos.x, topLeftPos.y, color, lineSize);
            drawList.addLine(bottomRightPos.x, topLeftPos.y, bottomRightPos.x, bottomRightPos.y, color, lineSize);
            drawList.addLine(bottomRightPos.x, bottomRightPos.y, topLeftPos.x, bottomRightPos.y, color, lineSize);
            drawList.addLine(topLeftPos.x, bottomRightPos.y, topLeftPos.x, topLeftPos.y, color, lineSize);

            currentX += sprWidth + sprSpacingX;
            if (currentX >= IMAGE_SIZE_X) {
                // next line
                currentX = 0;
                currentY += sprHeight + sprSpacingY;
            }
        }
    }
}
