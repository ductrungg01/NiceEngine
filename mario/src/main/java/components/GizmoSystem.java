package components;

import system.KeyListener;
import system.Window;

import static org.lwjgl.glfw.GLFW.*;

public class GizmoSystem extends Component{
    //region Fields
    private Spritesheet gizmos;
    private int usingGizmo = 0;
    //endregion

    //region Contructors
    public GizmoSystem(Spritesheet gizmoSprites){
        this.gizmos =gizmoSprites;
    }
    //endregion

    //region Override methods
    @Override
    public void start(){
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1),
                Window.getImguiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2),
                Window.getImguiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt){
        if (usingGizmo == 0){
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1){
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_E)){
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)){
            usingGizmo = 1;
        }
    }
    //endregion
}
