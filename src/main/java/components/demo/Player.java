package components.demo;

import components.Component;
import editor.Debug;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import physics2d.components.RigidBody2D;
import scenes.GamePlayingSceneInitializer;
import system.GameObject;
import system.KeyListener;
import system.Window;
import util.AssetPool;

public class Player extends Component {
    private transient RigidBody2D rb;
    private float moveSpeed = 5f;

    @Override
    public void start() {
        rb = this.gameObject.getComponent(RigidBody2D.class);
        rb.setGravityScale(0);
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            rb.addForce(new Vector2f(0, moveSpeed * dt));
            Debug.Log("Press UP key");
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            rb.addForce(new Vector2f(0, -moveSpeed * dt));
            Debug.Log("Press DOWN key");
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            rb.addForce(new Vector2f( -moveSpeed * dt, 0));
            Debug.Log("Press LEFT key");
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            rb.addForce(new Vector2f( moveSpeed * dt,0 ));
            Debug.Log("Press RIGHT key");

        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        Debug.Log("beginCollision " + collidingObject.tag);

        if (collidingObject.compareTag("Obstacle")){
            Debug.Log("GAME OVER");
            AssetPool.getSound("assets/sounds/stomp.ogg").play();
            Window.changeScene(new GamePlayingSceneInitializer());
        }

        if (collidingObject.compareTag("target")){
            Debug.Log("WIN");
            AssetPool.getSound("assets/sounds/1-up.ogg").play();
            Window.changeScene(new GamePlayingSceneInitializer());
        }
    }

    @Override
    public void endCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        Debug.Log("endCollision " + collidingObject.tag);
    }

}
