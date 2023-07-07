package editor.uihelper;

import org.joml.Vector4f;

public class ColorHelp {
    /**
     * Changes the alpha component of the oldColor to the newAlpha value.
     *
     * @param oldColor The old color to change the alpha component.
     * @param newAlpha The new alpha value, in the range [0, 1].
     * @return The new color with the changed alpha component.
     */

    public static Vector4f ColorChangeAlpha(Vector4f oldColor, float newAlpha) {
        // Ensure that the new alpha value is within the range [0, 1].
        if (newAlpha < 0 || newAlpha > 1) {
            assert false : "The alpha para in the ColorChangeAlpha must in range [0,1]";
        }

        return new Vector4f(oldColor.x, oldColor.y, oldColor.z, newAlpha);
    }
}
