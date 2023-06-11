package editor;

import components.Spritesheet;
import editor.uihelper.ButtonColor;
import editor.uihelper.ColorHelp;
import imgui.ImGui;
import imgui.ImVec2;
import components.Sprite;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import renderer.Texture;
import system.GameObject;
import system.Window;
import util.AssetPool;
import util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;

public class NiceImGui {

    //region Fields
    private static float defaultColumnWidth = 150.0f;
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

    public static Object ReferenceButton(String label, ReferenceType referenceType, Object oldValue, String idPush) {
        return ReferenceButton(label, referenceType, oldValue, defaultColumnWidth, idPush);
    }

    public static Object ReferenceButton(String label, ReferenceType referenceType, Object oldValue, float labelWidth, String idPush) {
        ImGui.pushID(idPush);
        ImGui.columns(2);

        ImGui.setColumnWidth(0, labelWidth);
        ImGui.setColumnWidth(1, 1000);

        ImGui.text(label);

        ImGui.nextColumn();

        float referenceButtonWidth = 250.0f;
        float referenceButtonHeight = 30.0f;

        // Create reference button
        String refState = (oldValue == null ? "(Missing) " : "");
        String refName = "";
        switch (referenceType) {
            case GAMEOBJECT -> {
                GameObject go = (GameObject) oldValue;
                if (go != null)
                    refName = go.name;
                else
                    refName = "Game object";
                break;
            }
            case SPRITE -> {
                Sprite spr = (Sprite) oldValue;
                if (spr != null && oldValue != null && spr.getTexture() != null) {
                    String texturePath = spr.getTexture().getFilePath();
                    File imageFile = new File(texturePath);

                    refName = FileUtils.getShorterName(imageFile.getName());
                } else
                    refName = "Sprite";
                break;
            }
            case SOUND -> {
                File soundFile = (File) oldValue;
                if (soundFile != null)
                    refName = FileUtils.getShorterName(soundFile.getName());
                else
                    refName = "Sound";
                break;
            }
            case JAVA -> {
                File javaFile = (File) oldValue;
                if (javaFile != null)
                    refName = javaFile.getName();
                else
                    refName = "Java Script";
                break;
            }
        }
        if (ImGui.button(refState + "<" + refName + ">", referenceButtonWidth, referenceButtonHeight)) {

        }
        ImGui.sameLine();

        if (drawOpenFileDialogButton(referenceButtonHeight)) {
            FileDialog.getInstance().open(idPush, referenceType);
        }

        oldValue = FileDialog.getInstance().getSelectedObject(idPush, oldValue);

        ImGui.sameLine();
        if (drawDeleteReferenceButton(referenceButtonHeight)) {
            oldValue = null;

            if (referenceType == ReferenceType.SPRITE) {
                oldValue = FileUtils.getDefaultSprite();
            }
        }

        ImGui.columns(1);

        ImGui.popID();

        return oldValue;
    }

    private static boolean drawOpenFileDialogButton(float openFileDialogButtonSize) {
        boolean isClick = false;

        ImDrawList drawList = ImGui.getWindowDrawList();

        // Create open file dialog button
        if (drawButton("",
                new ButtonColor(COLOR_Blue, COLOR_DarkBlue, COLOR_DarkBlue),
                new Vector2f(openFileDialogButtonSize, openFileDialogButtonSize))) {
            isClick = true;
        }

        ImGui.sameLine();

        float iconButtonSize = 7.0f;
        float iconButtonPosX = ImGui.getCursorScreenPosX() - openFileDialogButtonSize / 2f - 7f;
        float iconButtonPosY = ImGui.getCursorScreenPosY() + openFileDialogButtonSize / 2f;
        drawList.addCircleFilled(iconButtonPosX, iconButtonPosY, iconButtonSize, ImColor.intToColor(255, 255, 255, 255));
        drawList.addCircle(iconButtonPosX, iconButtonPosY, iconButtonSize * 1.5f, ImColor.intToColor(255, 255, 255, 255));

        return isClick;
    }

    private static boolean drawDeleteReferenceButton(float btnSize) {
        if (drawButton("-", new ButtonColor(COLOR_Red, COLOR_DarkRed, COLOR_DarkRed)
                , new Vector2f(btnSize, btnSize))) {
            return true;
        }

        return false;
    }

    public static boolean deleteReferenceButton(float btnSize) {
        if (drawButton("-", new ButtonColor(COLOR_Red, COLOR_DarkRed, COLOR_DarkRed)
                , new Vector2f(btnSize, btnSize))) {
            return true;
        }
        return false;
    }

    public static boolean openFileDialogButton(float openFileDialogButtonSize) {
        boolean isClick = false;

        if (NiceImGui.drawButton("",
                new ButtonColor(COLOR_Blue, COLOR_DarkBlue, COLOR_DarkBlue),
                new Vector2f(openFileDialogButtonSize, openFileDialogButtonSize))) {
            isClick = true;
            Debug.Log("open file dialog button is clicked!");
        }

        ImGui.sameLine();

        ImDrawList drawList = ImGui.getWindowDrawList();
        float iconButtonSize = 7.0f;
        float iconButtonPosX = ImGui.getCursorScreenPosX() - openFileDialogButtonSize / 2f - 7f;
        float iconButtonPosY = ImGui.getCursorScreenPosY() + openFileDialogButtonSize / 2f;
        drawList.addCircleFilled(iconButtonPosX, iconButtonPosY, iconButtonSize, ImColor.intToColor(255, 255, 255, 255));
        drawList.addCircle(iconButtonPosX, iconButtonPosY, iconButtonSize * 1.5f, ImColor.intToColor(255, 255, 255, 255));

        return isClick;
    }


    /**
     * Very good for debugging
     **/
    public static void createDot(float x, float y, float size) {
        ImDrawList drawList = ImGui.getWindowDrawList();
        drawList.addCircleFilled(x, y, size, ImColor.intToColor(255, 0, 0, 255));
    }

    public static float getLengthOfText(String text) {
        // Tính độ dài của label
        ImVec2 textSize = new ImVec2(0, 0);
        ImGui.calcTextSize(textSize, text);
        float labelWidth = textSize.x + ImGui.getStyle().getFramePaddingX() * 2.0f;

        return labelWidth + ImGui.getStyle().getFramePaddingX() * 2.0f;
    }

    public static float getHeightOfALine() {
        return ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
    }

    public static boolean drawButton(String label, ButtonColor btnColor) {

        Vector2f buttonSize = getSizeOfButton(label);

        return drawButton(label, btnColor, buttonSize);
    }

    public static Vector2f getSizeOfButton(String label) {
        float width = getLengthOfText(label);
        float height = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;

        return new Vector2f(width, height);
    }

    public static boolean drawButtonWithLeftText(String label, ButtonColor btnColor, Vector2f btnSize) {
        float posX = ImGui.getCursorPosX() + 8f;
        float posY = ImGui.getCursorPosY();

        boolean ans = drawButton("", btnColor, btnSize);

        float newPosX = ImGui.getCursorPosX();
        float newPosY = ImGui.getCursorPosY();

        ImGui.setCursorPos(posX, posY);
        ImGui.text(label);
        ImGui.setCursorPos(newPosX, newPosY);

        return ans;
    }

    public static boolean drawButton(String label, ButtonColor btnColor, Vector2f btnSize) {
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
            isClick = true;
        }
        ImGui.popStyleColor(3);
        ImGui.popID();

        return isClick;
    }

    public static void prefabShowingInInspectorsButton(GameObject go) {
        ImGui.columns(3);
        ImGui.setColumnWidth(0, 100);
        ImGui.setColumnWidth(1, 250);
        ImGui.text("Prefab: ");
        ImGui.nextColumn();

        String pushId = "GoToPrefabButton" + go.name + go.hashCode();
        ImGui.pushID(pushId);

        GameObject prefab = GameObject.getPrefabById(go.parentId);
        if (prefab == null) {
            ImGui.button("Error: Cannot find the prefab!");
        } else {
            Vector4f buttonColor = new Vector4f(14 / 255f, 14 / 255f, 28 / 255f, 1);
            if (drawButton("Go to: '" + prefab.name + "'",
                    new ButtonColor(buttonColor, COLOR_Blue, COLOR_DarkBlue),
                    new Vector2f(ImGui.getContentRegionAvailX(), 30f))) {
            }
        }

        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text("Go to prefab");
            ImGui.endTooltip();
        }
        ImGui.popID();

        ImGui.nextColumn();

        pushId = "Override the prefab" + go.name + go.hashCode();
        ImGui.pushID(pushId);
        if (prefab == null) {
            ImGui.button("Error: Cannot find the prefab!");
        } else {
            Vector4f buttonColor = new Vector4f(14 / 255f, 14 / 255f, 28 / 255f, 1);
            if (drawButton("Override prefab!",
                    new ButtonColor(buttonColor, COLOR_Blue, COLOR_DarkBlue),
                    new Vector2f(ImGui.getContentRegionAvailX(), 30f))) {
                go.overrideThePrefab();
            }
        }

        ImGui.popID();

        ImGui.columns(1);
    }

    public static float dragfloat(String label, float value, String imguiId) {
        final float DEFAULT_SPEED = 0.1f;
        return dragfloat(label, value, DEFAULT_SPEED, imguiId);
    }

    public static float dragfloat(String label, float value, float vSpeed, String imguiID) {
        ImGui.pushID(imguiID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##dragFloat", valArr, vSpeed);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static float dragfloat(String label, float value, float minValue, float maxValue, String imguiID) {
        final float DEFAULT_SPEED = 0.1f;

        return dragfloat(label, value, minValue, maxValue, DEFAULT_SPEED, imguiID);
    }

    public static float dragfloat(String label, float value, float minValue, float maxValue, float vSpeed, String imguiID) {
        ImGui.pushID(imguiID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##dragFloat", valArr, vSpeed);

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

        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4("##colorPicker", imColor)) {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static String inputText(String label, String text, String idPush) {
        return inputTextWithHint(label, "", text, idPush);
    }

    public static String inputTextWithHint(String label, String hint, String text, String idPush) {
        ImGui.pushID(idPush);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImString outString = new ImString(text, 256);
        if (ImGui.inputTextWithHint("##" + label, hint, outString)) {
            ImGui.columns(1);
            ImGui.popID();

            return outString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }

    public static String inputTextWithNoLabel(String text, String idPush) {
        return inputTextWithHintAndNoLabel("", text, idPush);
    }

    public static String inputTextWithHintAndNoLabel(String hint, String text, String idPush) {
        ImGui.pushID(idPush);

        ImString outString = new ImString(text, 256);
        if (ImGui.inputTextWithHint("##", hint, outString)) {
            ImGui.popID();

            return outString.get();
        }

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

    public static boolean checkbox(String label, boolean isChecked) {
        ImGui.pushID(label + "Checkbox");

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImBoolean imguiIsChecked = new ImBoolean(isChecked);
        ImGui.checkbox("", imguiIsChecked);
        boolean returnValue = imguiIsChecked.get();

        ImGui.columns(1);

        ImGui.popID();

        return returnValue;
    }

    public static String comboBox(String label, String selectingValue, int imguiComboFlag, List<String> items, String imguiID) {
        ImGui.pushID(imguiID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        String returnvalue = selectingValue;

        if (ImGui.beginCombo("", selectingValue, imguiComboFlag)) {
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);

                boolean isSelected = (item.equals(selectingValue));
                if (ImGui.selectable(item, isSelected)) {
                    returnvalue = item;
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }

        ImGui.columns(1);
        ImGui.popID();

        return returnvalue;
    }

    //endregion
}
