package components.scripts.test;

import components.Component;
import components.Rigidbody;
import components.Sprite;
import components.SpriteRenderer;
import editor.Debug;
import org.joml.Vector2f;
import system.*;

import static org.lwjgl.glfw.GLFW.*;

public class TestComponent extends Component {
    public GameObject TargetGo = null;
    public Sprite sprite = null;
    private GameObject spr_go = null;
    private transient float moveSpeed = 3f;

    @Override
    public void start() {
        if (sprite != null) {
            spr_go = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);

            spr_go.transform.position = new Vector2f(1, 1).add(this.gameObject.transform.position);

            Window.getScene().addGameObjectToScene(spr_go);
        }
    }

    @Override
    public void update(float dt) {


        if (TargetGo != null) {
            if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
                TargetGo.getComponent(TargetDebugging.class).GoUp(moveSpeed * dt);
            } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
                TargetGo.getComponent(TargetDebugging.class).GoDown(moveSpeed * dt);
            }
        }
    }

    @Override
    public void editorUpdate(float dt) {
        if (TargetGo != null) {
            //Debug.Log("UID in Editor (TestComponent): " + TargetGo.getUid());
            if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
                TargetGo.getComponent(TargetDebugging.class).GoUp(moveSpeed * dt);
            } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
                TargetGo.getComponent(TargetDebugging.class).GoDown(moveSpeed * dt);
            }
        }
    }
}
