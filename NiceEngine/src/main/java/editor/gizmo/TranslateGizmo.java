package editor.gizmo;

import components.INonAddableComponent;
import components.Sprite;
import editor.windows.InspectorWindow;
import system.MouseListener;

public class TranslateGizmo extends Gizmo implements INonAddableComponent {
    //region Constructors

    public TranslateGizmo() {
    }

    public TranslateGizmo(Sprite arrowSprite, InspectorWindow inspectorWindow) {
        super(arrowSprite, inspectorWindow);
        this.xAxisObject.name = "TranslateGizmo - xAxisObject";
        this.yAxisObject.name = "TranslateGizmo - yAxisObject";
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
