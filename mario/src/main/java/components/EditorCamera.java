package components;

import jade.Camera;
import jade.KeyListener;
import jade.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends  Component{

    private float dragDebounce = 0.032f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;
    private float lerpTime = 0.0f;
    private float dragSensitivity = 30.0f;
    private float scrollSensivity = 0.1f;

    public EditorCamera(Camera levelEditorCamera){
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt){
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && dragDebounce > 0){
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);

            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            dragDebounce = 0.1f;
        }

        if (MouseListener.getScrollY() != 0.0f){
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensivity),
                    1 / levelEditorCamera.getZoom());

            addValue *= -Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
            reset = true;
        }

        if (reset){
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;
            if (Math.abs(levelEditorCamera.position.x) <= 5.0f &&
                    Math.abs(levelEditorCamera.position.y) <= 5.0f){
                this.lerpTime = 0;
                levelEditorCamera.position.set(0f, 0f);
                this.levelEditorCamera.setZoom(1.0f);
                reset = false;
            }
        }
    }
}
