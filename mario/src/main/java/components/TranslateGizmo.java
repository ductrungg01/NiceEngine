package components;

import editor.PropertiesWindow;
import system.MouseListener;

public class TranslateGizmo extends Gizmo{
    //region Contructors
    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow){
        super(arrowSprite, propertiesWindow);
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt){
        if (activeGameObject != null){
            if (xAxisActive && !yAxisActive){
                activeGameObject.transform.position.x -= MouseListener.getWorldX();
            } else if (yAxisActive){
                activeGameObject.transform.position.y -= MouseListener.getWorldY();
            }
        }

        super.editorUpdate(dt);
    }
    //endregion
}
