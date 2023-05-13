package components;

import editor.InspectorWindow;
import system.MouseListener;

public class TranslateGizmo extends Gizmo {
    //region Contructors
    public TranslateGizmo(Sprite arrowSprite, InspectorWindow inspectorWindow) {
        super(arrowSprite, inspectorWindow);
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldX();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldY();
            }
        }

        super.editorUpdate(dt);
    }
    //endregion
}
