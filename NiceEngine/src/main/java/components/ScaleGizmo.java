package components;

import editor.InspectorWindow;
import system.MouseListener;

public class ScaleGizmo extends Gizmo {
    //region Contructors
    public ScaleGizmo(Sprite scaleSprite, InspectorWindow inspectorWindow) {
        super(scaleSprite, inspectorWindow);
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldX();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldY();
            }
        }

        super.editorUpdate(dt);
    }
    //endregion
}
