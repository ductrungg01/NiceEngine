package editor;

import editor.uihelper.ButtonColor;
import editor.windows.FileDialog;
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
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import util.FileUtils;

import java.io.File;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;
import static java.lang.Math.min;

public class NiceImGui {

    //region Fields
    private static final float defaultLabelColumnWidth = 150.0f;
    //endregion

    //region Methods

    //region Calc / Settings / Configurations
    private static float calcMinLabelColWith(String label) {
        float minLength = getLengthOfText(label);

        return Float.max(minLength, defaultLabelColumnWidth);
    }

    public static Vector2f getSizeOfButton(String label) {
        float width = getLengthOfText(label);
        float height = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;

        return new Vector2f(width, height);
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

    private static void columnConfiguration(int numsOfColumns, float[] columnsWidth, String label) {
        if (numsOfColumns <= 0) return;

        float[] widths = new float[numsOfColumns];
        if (label.isEmpty()) {
            widths[0] = 0;
        } else {
            if (columnsWidth[0] != 0)
                widths[0] = columnsWidth[0];
            else
                widths[0] = calcMinLabelColWith(label);
        }

        for (int i = 1; i < numsOfColumns; i++) {
            widths[i] = columnsWidth[i];
        }

        ImGui.columns(numsOfColumns);
        for (int i = 0; i < numsOfColumns - 1; i++) {
            ImGui.setColumnWidth(i, widths[i]);
        }
        if (widths[numsOfColumns - 1] != 0) {
            ImGui.setColumnWidth(numsOfColumns - 1, widths[numsOfColumns - 1]);
        }
    }
    //endregion

    //region Draw dot
    /**
     * Very good for debugging
     **/
    static final Vector4f DEFAULT_DOT_COLOR = COLOR_Red;
    static final float DEFAULT_DOT_SIZE = 3f;

    public static void createDot() {
        createDot(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY(), DEFAULT_DOT_SIZE, DEFAULT_DOT_COLOR);
    }

    public static void createDot(float x, float y) {
        createDot(x, y, DEFAULT_DOT_SIZE, DEFAULT_DOT_COLOR);
    }

    public static void createDot(float x, float y, float size) {
        createDot(x, y, size, DEFAULT_DOT_COLOR);
    }

    public static void createDot(float x, float y, float size, Vector3f color) {
        createDot(x, y, size, new Vector4f(color, 1f));
    }

    public static void createDot(float x, float y, float size, Vector4f color) {
        ImDrawList drawList = ImGui.getWindowDrawList();
        drawList.addCircleFilled(x, y, size, ImColor.floatToColor(color.x, color.y, color.z, color.w));
    }
    //endregion

    //region Vec2
    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, 0.0f, calcMinLabelColWith(label));
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, calcMinLabelColWith(label));
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
    //endregion

    //region Reference
    final static float DEFAULT_REFERENCE_BUTTON_WIDTH = 250f;

    public static Object ReferenceButton(String label, ReferenceType referenceType, Object oldValue, String idPush) {
        return ReferenceButton(label, referenceType, oldValue, new float[2], idPush);
    }

    public static Object ReferenceButton(String label, ReferenceType referenceType, Object oldValue, float[] columnWidths, String idPush) {
        ImGui.pushID(idPush);

        columnConfiguration(2, columnWidths, label);
        ImGui.text(label);

        ImGui.nextColumn();

        float offset = ImGui.getStyle().getFramePaddingX() * 2;
        float referenceButtonHeight = 30.0f;
        float referenceButtonWidth = (columnWidths[1] != 0 ? columnWidths[1] - (referenceButtonHeight + offset) * 2 - offset : DEFAULT_REFERENCE_BUTTON_WIDTH);

        //region Reference Button
        String refState = (oldValue == null ? "(Missing) " : "");
        String refName = "";
        String toolTipRef = "";
        switch (referenceType) {
            case GAMEOBJECT -> {
                GameObject go = (GameObject) oldValue;
                if (go != null)
                    refName = go.name;
                else
                    refName = "Game object";
                toolTipRef = refName;
            }
            case SPRITE -> {
                Sprite spr = (Sprite) oldValue;
                if (spr != null && oldValue != null && spr.getTexture() != null) {
                    String texturePath = spr.getTexture().getFilePath();
                    File imageFile = new File(texturePath);

                    refName = FileUtils.getShorterName(imageFile.getName());
                    toolTipRef = spr.getTexture().getFilePath();
                } else
                    refName = "Sprite";
            }
            case SOUND -> {
                File soundFile = (File) oldValue;
                if (soundFile != null) {
                    refName = FileUtils.getShorterName(soundFile.getName());
                    toolTipRef = soundFile.getName();
                } else
                    refName = "Sound";
                break;
            }
            case JAVA -> {
                File javaFile = (File) oldValue;
                if (javaFile != null)
                    refName = javaFile.getName();
                else
                    refName = "Java Script";
                toolTipRef = refName;
            }
        }

        ImGui.pushID("ReferenceButton " + idPush);

        if (ImGui.button(refState + "<" + refName + ">", referenceButtonWidth, referenceButtonHeight)) {

        }

        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text(toolTipRef);
            ImGui.endTooltip();
        }

        ImGui.popID();

        //endregion

        ImGui.sameLine();

        //region Open FileDialog Button
        ImGui.pushID("OpenFileDialogButton" + idPush);
        if (drawOpenFileDialogButton(referenceButtonHeight)) {
            FileDialog.getInstance().open(idPush, referenceType);
        }

        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text("Open file dialog");
            ImGui.endTooltip();
        }

        oldValue = FileDialog.getInstance().getSelectedObject(idPush, oldValue);
        ImGui.popID();
        //endregion

        ImGui.sameLine();

        //region Remove Button
        ImGui.pushID("Remove reference of " + idPush);
        if (drawDeleteReferenceButton(referenceButtonHeight)) {
            oldValue = null;

            if (referenceType == ReferenceType.SPRITE) {
                oldValue = FileUtils.getDefaultSprite();
            }
        }
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text("Remove reference value?");
            ImGui.endTooltip();
        }
        ImGui.popID();
        //endregion

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
    //endregion

    //region Button
    public static boolean drawButton(String label, ButtonColor btnColor) {

        Vector2f buttonSize = getSizeOfButton(label);

        return drawButton(label, btnColor, buttonSize);
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
    //endregion

    //region Float
    static final float DEFAULT_FLOAT_DRAG_SPEED = 0.1f;

    public static float dragFloat(String label, float value) {
        return dragFloat(label, value, Float.MIN_VALUE, Float.MAX_VALUE, DEFAULT_FLOAT_DRAG_SPEED, new float[2], label);
    }

    public static float dragFloat(String label, float value, float minValue, float maxValue, String imguiID) {
        return dragFloat(label, value, minValue, maxValue, DEFAULT_FLOAT_DRAG_SPEED, new float[2], imguiID);
    }

    public static float dragFloat(String label, float value, float minValue, float maxValue, float vSpeed, float labelColumnWidth, String imguiId) {
        float[] columnWidth = new float[2];
        columnWidth[0] = labelColumnWidth;
        return dragFloat(label, value, minValue, maxValue, vSpeed, columnWidth, imguiId);
    }

    public static float dragFloat(String label, float value, float minValue, float maxValue, float vSpeed, float[] columnWidth, String imguiId) {
        ImGui.pushID(imguiId);

        columnConfiguration(2, columnWidth, label);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};

        if (columnWidth[1] != 0) {
            ImGui.pushItemWidth(columnWidth[1]);
            ImGui.dragFloat("##dragFloat", valArr, vSpeed, minValue, maxValue);
            ImGui.popItemWidth();
        } else {
            ImGui.dragFloat("##dragFloat", valArr, vSpeed, minValue, maxValue);
        }

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }
    //endregion

    //region Color
    public static boolean colorPicker4(String label, Vector4f color) {
        boolean res = false;

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, calcMinLabelColWith(label));
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
    //endregion

    //region Int
    public static int dragInt(String label, int value) {
        return dragInt(label, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int dragInt(String label, int value, int minValue, int maxValue) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, calcMinLabelColWith(label));
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {value};

        ImGui.dragInt("##dragFloat", valArr, 0.1f, minValue, maxValue);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }
    //endregion

    //region InputText
    public static String inputText(String label, String text, String imguiId) {
        return inputText(label, text, "", new float[2], imguiId);
    }

    public static String inputText(String label, String text, String hint, float labelColumnWidth, String imguiId) {
        float[] colW = new float[2];
        colW[0] = labelColumnWidth;
        return inputText(label, text, hint, colW, imguiId);
    }

    public static String inputText(String label, String text, String hint, float[] columnWidths, String imguiId) {
        ImGui.pushID(imguiId);

        columnConfiguration(2, columnWidths, label);

        ImGui.text(label);

        ImGui.nextColumn();

        ImString outString = new ImString(text, 256);

        if (columnWidths[1] != 0) {
            ImGui.pushItemWidth(columnWidths[1]);
            if (ImGui.inputTextWithHint("##" + label, hint, outString)) {
                ImGui.columns(1);
                ImGui.popID();

                return outString.get();
            }
            ImGui.popItemWidth();
        } else {
            if (ImGui.inputTextWithHint("##" + label, hint, outString)) {
                ImGui.columns(1);
                ImGui.popID();

                return outString.get();
            }
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
        ImGui.setColumnWidth(0, calcMinLabelColWith(label));
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

    //region Checkbox

    public static boolean checkbox(String label, boolean isChecked) {
        return checkbox(label, isChecked, new float[2]);
    }

    public static boolean checkbox(String label, boolean isChecked, float labelColumnWidth) {
        float[] colW = new float[2];
        colW[0] = labelColumnWidth;
        return checkbox(label, isChecked, colW);
    }

    public static boolean checkbox(String label, boolean isChecked, float[] columnWidths) {
        ImGui.pushID(label + "Checkbox");

        columnConfiguration(2, columnWidths, label);

        ImGui.text(label);

        ImGui.nextColumn();

        ImBoolean imguiIsChecked = new ImBoolean(isChecked);
        ImGui.checkbox("", imguiIsChecked);
        boolean returnValue = imguiIsChecked.get();

        ImGui.columns(1);

        ImGui.popID();

        return returnValue;
    }
    //endregion

    //region ComboBox
    public static String comboBox(String label, String selectingValue, int imguiComboFlag, List<String> items, String imguiID) {
        ImGui.pushID(imguiID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, calcMinLabelColWith(label));
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

    //region Others
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
    //endregion

    //region Image
    public static void showImage(Sprite spr, Vector2f size) {
        showImage(spr, size, false, "", false, new Vector2f(), false);
    }

    public static void showImage(Sprite spr, Vector2f size, boolean showAtBottom, String tooltipStr, boolean showTooltipImg, Vector2f tooltipImgSize, boolean changeBgWhenHover) {
        float offset = min(size.x / spr.getWidth(), size.y / spr.getHeight());
        Vector2f[] texCoords = spr.getTexCoords();

        ImGui.pushID(spr.getTexId());

        boolean hoverFakeBtn = false;
        Vector2f oldCursorPos = new Vector2f(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());
        if (showTooltipImg) {
            Vector4f color = new Vector4f(0, 0, 0, 0);
            NiceImGui.drawButton("", new ButtonColor(color, color, color), size);
            if (ImGui.isItemHovered()) {
                hoverFakeBtn = true;
                if (changeBgWhenHover) {
                    ImGui.setCursorScreenPos(oldCursorPos.x, oldCursorPos.y);
                    ImVec4 btnHoveredCol = new ImVec4();
                    ImGui.getStyle().getColor(ImGuiCol.ButtonHovered, btnHoveredCol);
                    color = new Vector4f(btnHoveredCol.x, btnHoveredCol.y, btnHoveredCol.z, btnHoveredCol.w);
                    NiceImGui.drawButton("", new ButtonColor(color, color, color), size);
                }
            }
            ImGui.setCursorScreenPos(oldCursorPos.x, oldCursorPos.y);
        }

        if (showAtBottom) {
            float heightOffset = size.y - spr.getHeight() * offset;
            ImGui.setCursorScreenPos(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY() + heightOffset);
        }

        ImGui.image(spr.getTexId(), spr.getWidth() * offset, spr.getHeight() * offset,
                texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y);

        if ((ImGui.isItemHovered() || hoverFakeBtn) && (!tooltipStr.isEmpty() || showTooltipImg)) {
            ImGui.beginTooltip();
            if (!tooltipStr.isEmpty()) {
                ImGui.text(tooltipStr);
            }
            if (showTooltipImg) {
                showImage(spr, tooltipImgSize);
            }
            ImGui.endTooltip();

        }

        ImGui.popID();
    }
    //endregion

    //region Image button
    public static boolean imageButton(Sprite spr, Vector2f size, String tooltip) {
        boolean isClicked = false;

        Vector2f[] texCoords = spr.getTexCoords();

        if (ImGui.imageButton(spr.getTexId(), size.x, size.y, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y)) {
            isClicked = true;
        }

        if (ImGui.isItemHovered() && !tooltip.isEmpty()) {
            ImGui.beginTooltip();
            ImGui.text(tooltip);
            ImGui.endTooltip();
        }

        return isClicked;
    }
    //endregion

    //endregion
}
