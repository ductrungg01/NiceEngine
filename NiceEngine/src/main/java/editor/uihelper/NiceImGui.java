package editor.uihelper;

import components.Sprite;
import editor.Debug;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import system.Window;
import util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NiceImGui {

    //region Fields
    private static float defaultColumnWidth = 220.0f;
    //endregion

    //region Methods
    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, 0.0f, defaultColumnWidth);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, defaultColumnWidth);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.8f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue,
                                       float columnWidthForLabel, float columnWidthForValues,
                                       float minValue, float maxValue) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidthForLabel);
        ImGui.setColumnWidth(1, columnWidthForValues);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.8f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.01f, minValue, maxValue);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.01f, minValue, maxValue);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static boolean openFileDialog = false;
    public static boolean fileDialogIsShowed = false;

    public static void ReferenceButton(String label, ReferenceConfig referenceConfig) {
        ImGui.pushID(label);
        ImGui.columns(2);

        ImGui.setColumnWidth(0, 200);
        ImGui.setColumnWidth(1, 700);

        ImGui.text(label);

        ImGui.nextColumn();

        float referenceButtonWidth = 300.0f;
        float referenceButtonHeight = 30.0f;

        // Create reference button
        if (ImGui.button("<(Missing) Box2D Collider>", referenceButtonWidth, referenceButtonHeight)) {

        }
        ImGui.sameLine();

        drawReferenceButton(referenceButtonHeight);
        showFileDialogForReference(referenceConfig);

        ImGui.columns(1);

        ImGui.popID();
    }

    private static void drawReferenceButton(float openFileDialogButtonSize) {
        ImDrawList drawList = ImGui.getWindowDrawList();

        // Create open file dialog button
        if (ImGui.button("", openFileDialogButtonSize, openFileDialogButtonSize)) {
            openFileDialog = true;
            Debug.Log("open file dialog button is clicked!");
        }

        ImGui.sameLine();

        float iconButtonSize = 7.0f;
        float iconButtonPosX = ImGui.getCursorScreenPosX() - openFileDialogButtonSize / 2f - 7f;
        float iconButtonPosY = ImGui.getCursorScreenPosY() + openFileDialogButtonSize / 2f;
        drawList.addCircleFilled(iconButtonPosX, iconButtonPosY, iconButtonSize, ImColor.intToColor(255, 255, 255, 255));
        drawList.addCircle(iconButtonPosX, iconButtonPosY, iconButtonSize * 1.5f, ImColor.intToColor(255, 255, 255, 255));
    }

    private static void showFileDialogForReference(ReferenceConfig referenceConfig) {
        if (openFileDialog && !fileDialogIsShowed) {
            ImGui.openPopup("File Dialog");
            fileDialogIsShowed = true;
        }

        if (ImGui.beginPopupModal("File Dialog")) {
            ImGui.beginChild("fileDialog", ImGui.getContentRegionMaxX() - 50, ImGui.getContentRegionMaxY() - 100, true);
            final float iconWidth = 100f;
            final float iconHeight = 100f;
            final float iconSize = iconHeight;
            final float spacingX = 25.0f;
            final float spacingY = 50.0f;
            float availableWidth = ImGui.getContentRegionAvailX();
            int itemsPerRow = (int) (availableWidth / (iconWidth + spacingX));

            int itemIndex = 0;

            List<Object> itemList = new ArrayList<>();
            itemList.addAll(FileUtils.getFilesWithReferenceConfig(referenceConfig));
            itemList.addAll(Window.getScene().getGameObjects());

            for (Object item : itemList) {
                // Calculate position for this item
                float posX = (itemIndex % itemsPerRow) * (iconWidth + spacingX);
                float posY = (itemIndex / itemsPerRow) * (iconHeight + spacingY);

                // Set item position and size
                ImGui.setItemAllowOverlap();
                ImGui.setCursorPos(posX, posY);

                drawItemInFileDialog(item, iconSize);

                itemIndex++;
            }

            ImGui.endChild();

            if (ImGui.button("Cancel")) {
                openFileDialog = false;
                fileDialogIsShowed = false;
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }

    }

    public static void drawItemInFileDialog(Object item, float iconSize) {
        String id = item.toString();
        Sprite icon = new Sprite();
        String itemName = "";

        if (item instanceof File) {
            File file = (File) item;
            id = file.getAbsolutePath();
            icon = FileUtils.getIconByFile(file);
            itemName = FileUtils.getShorterName(file.getName());
        } else if (item instanceof GameObject) {
            GameObject go = (GameObject) item;
            id = go.name;
            icon = FileUtils.getGameObjectIcon();
            itemName = FileUtils.getShorterName(go.name);
        }

        ImGui.pushID(id);

        float posX = ImGui.getCursorPosX();
        float posY = ImGui.getCursorPosY();

        // draw the icon
        ImGui.image(icon.getTexId(), iconSize, iconSize);

        // write the file name
        // set the cursor pos is below of icon
        final float offsetOfIconAndName = 5f;
        ImGui.setCursorPos(posX + 5f, posY + iconSize + offsetOfIconAndName);
        ImGui.text(itemName);

        // End item
        ImGui.popID();
    }

//    public static void drawFileInFileDialog(File file, float iconSize) {
//        ImGui.pushID(file.getAbsolutePath());
//
//        float posX = ImGui.getCursorPosX();
//        float posY = ImGui.getCursorPosY();
//
//        // Display icon and filename
//        // get icon first
//        Sprite icon = FileUtils.getIconByFile(file);
//
//        // draw the icon
//        ImGui.image(icon.getTexId(), iconSize, iconSize);
//
//        // write the file name
//        // set the cursor pos is below of icon
//        final float offsetOfIconAndName = 5f;
//        ImGui.setCursorPos(posX + 5f, posY + iconSize + offsetOfIconAndName);
//        ImGui.text(FileUtils.getShorterName(file.getName()));
//
//        // End item
//        ImGui.popID();
//    }
//
//    public static void drawGameObjectInFileDialog(GameObject gameObject, float iconSize) {
//        ImGui.pushID(gameObject.name);
//
//        float posX = ImGui.getCursorPosX();
//        float posY = ImGui.getCursorPosY();
//
//        // Display icon and filename
//        // get icon first
//        Sprite icon = FileUtils.getGameObjectIcon();
//
//        // draw the icon
//        ImGui.image(icon.getTexId(), iconSize, iconSize);
//
//        // write the file name
//        // set the cursor pos is below of icon
//        final float offsetOfIconAndName = 5f;
//        ImGui.setCursorPos(posX + 5f, posY + iconSize + offsetOfIconAndName);
//        ImGui.text(FileUtils.getShorterName(gameObject.name));
//
//        // End item
//        ImGui.popID();
//    }

    /**
     * Very good for debugging
     **/
    public static void CreateDot(float x, float y, float size) {
        ImDrawList drawList = ImGui.getWindowDrawList();
        drawList.addCircleFilled(x, y, size, ImColor.intToColor(255, 0, 0, 255));
    }

    public static boolean NiceButton(String label, ButtonColor btnColor) {
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;

        // Tính độ dài của label
        ImVec2 textSize = new ImVec2(0, 0);
        ImGui.calcTextSize(textSize, label);
        float labelWidth = textSize.x;

        // Tính toán kích thước của button
        Vector2f buttonSize = new Vector2f(labelWidth + ImGui.getStyle().getFramePaddingX() * 2.0f + 10, lineHeight * 1.5f);

        return NiceButton(label, btnColor, buttonSize);
    }

    public static boolean NiceButton(String label, ButtonColor btnColor, Vector2f btnSize) {
        boolean isClick = false;

        ImGui.pushID(label);

        Vector2f mousePos = new Vector2f(ImGui.getIO().getMousePosX(), ImGui.getIO().getMousePosY());
        Vector2f buttonPos = new Vector2f(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());

        if (mousePos.x >= buttonPos.x && mousePos.x <= buttonPos.x + btnSize.x
                && mousePos.y >= buttonPos.y && mousePos.y <= buttonPos.y + btnSize.y) {
            if (ImGui.getMouseCursor() != ImGuiMouseCursor.Hand) {
                ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            }
        }

        ImGui.getIO().setMouseDrawCursor(true);

        float widthEach = (ImGui.calcItemWidth() - btnSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, btnColor.buttonColor.x, btnColor.buttonColor.y, btnColor.buttonColor.z, btnColor.buttonColor.w);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, btnColor.hoveredColor.x, btnColor.hoveredColor.y, btnColor.hoveredColor.z, btnColor.hoveredColor.w);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, btnColor.activeColor.x, btnColor.activeColor.y, btnColor.activeColor.z, btnColor.activeColor.w);
        if (ImGui.button(label, btnSize.x, btnSize.y)) {
            // handle click button
            isClick = true;
        }
        ImGui.popStyleColor(3);
        ImGui.popID();

        return isClick;
    }

    public static float dragfloat(String label, float value) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static float dragfloat(String label, float value, float minValue, float maxValue) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        float v = valArr[0];

        v = Math.min(v, maxValue);
        v = Math.max(v, minValue);

        return v;
    }

    public static int dragInt(String label, int value) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {value};
        ImGui.dragInt("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static boolean colorPicker4(String label, Vector4f color) {
        boolean res = false;

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] imColor = {color.x, color.y, color.w, color.z};
        if (ImGui.colorEdit4("##colorPicker", imColor)) {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static String inputText(String label, String text) {
        boolean res = false;

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImString outString = new ImString(text, 256);
        if (ImGui.inputText("##" + label, outString)) {
            ImGui.columns(1);
            ImGui.popID();

            return outString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }

    public static String[] inputTextNoLabel(String text) {
        boolean isPressEnter = false;

        ImGui.pushID(text);
        ImGui.columns(1);
        ImGui.nextColumn();
        ImString outString = new ImString(text, 256);
        if (!ImGui.isAnyItemActive() && !ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT))
            ImGui.setKeyboardFocusHere(0);
        if (ImGui.inputText("##" + text, outString)) {
            ImGui.columns(1);

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
                System.out.println("press enter");
                isPressEnter = true;
            }
            ImGui.popID();
            if (isPressEnter) return new String[]{"true", outString.get()};
            else
                return new String[]{"false", outString.get()};
        }
        if (ImGui.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
            System.out.println("press enter");
            isPressEnter = true;
        }
        ImGui.popID();
        if (isPressEnter) return new String[]{"true", text};
        else
            return new String[]{"false", text};
    }

    public static String inputArrayText(String label, String[] text) {
        boolean res = false;

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        String value = "";

        for (int i = 0; i < text.length - 1; i++) {
            value += text[i] + ", ";
        }

        if (text.length > 0)
            value += text[text.length - 1];


        ImString outString = new ImString(value, 256);

        if (ImGui.inputText("##" + label, outString)) {
            ImGui.columns(1);
            ImGui.popID();

            return outString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return value;
    }
    //endregion
}
