package components;

import editor.InspectorWindow;
import system.MouseListener;

public class TranslateGizmo extends Gizmo implements INonAddableComponent {
    //region Constructors

    public TranslateGizmo() {
    }

    public TranslateGizmo(Sprite arrowSprite, InspectorWindow inspectorWindow) {
        super(arrowSprite, inspectorWindow);
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x = MouseListener.getWorldX() - Gizmo.getxAxisOffsetCalc(this.activeGameObject.transform.scale.x).x;
            } else if (yAxisActive) {
                activeGameObject.transform.position.y = MouseListener.getWorldY() - Gizmo.getyAxisOffsetCalc(this.activeGameObject.transform.scale.y).y;
            }
        }

        super.editorUpdate(dt);
    }
    //endregion
}
