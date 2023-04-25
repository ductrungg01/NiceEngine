package editor.uihelper;

import org.joml.Vector4f;

import static editor.uihelper.NiceShortCall.COLOR_Blue;

public class ButtonColor {
    public Vector4f buttonColor;
    public Vector4f hoveredColor;
    public Vector4f activeColor;

    private Vector4f defaultColor = COLOR_Blue;

    public ButtonColor(Vector4f buttonColor, Vector4f hoveredColor, Vector4f activeColor) {
        this.buttonColor = buttonColor;
        this.hoveredColor = hoveredColor;
        this.activeColor = activeColor;
    }

    public ButtonColor() {
        this.buttonColor = defaultColor;
        this.hoveredColor = defaultColor;
        this.activeColor = defaultColor;
    }
}
