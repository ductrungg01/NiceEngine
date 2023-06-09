package components;

import editor.InspectorWindow;
import system.MouseListener;

public class ScaleGizmo extends Gizmo implements INonAddableComponent {
    private float oldX = Float.NaN;
    private float oldY = Float.NaN;
    //region Constructors

    public ScaleGizmo() {
    }

    public ScaleGizmo(Sprite scaleSprite, InspectorWindow inspectorWindow) {
        super(scaleSprite, inspectorWindow);
        this.xAxisObject.name = "ScaleGizmo - xAxisObject";
        this.yAxisObject.name = "ScaleGizmo - yAxisObject";
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                if (!Float.isNaN(oldX)) {
                    float newX = MouseListener.getWorldX();
                    float addValue = (newX - oldX);

                    activeGameObject.transform.scale.x += addValue;
                    oldX = newX;
                } else oldX = MouseListener.getWorldX();
                oldY = Float.NaN;
            } else if (yAxisActive) {
                if (!Float.isNaN(oldY)) {
                    float newY = MouseListener.getWorldY();
                    float addValue = (newY - oldY);

                    activeGameObject.transform.scale.y += addValue;
                    oldY = newY;
                } else oldY = MouseListener.getWorldY();
                oldX = Float.NaN;
            } else {
                oldX = Float.NaN;
                oldY = Float.NaN;
            }
        }

        super.editorUpdate(dt);
    }
    //endregion
}
