package components.mariodemo;

import components.Component;
import editor.Debug;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.KeyListener;

public class DemoScript extends Component {
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
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            rb.addForce(new Vector2f(0, -moveSpeed * dt));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            rb.addForce(new Vector2f( -moveSpeed * dt, 0));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            rb.addForce(new Vector2f( moveSpeed * dt,0 ));
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        Debug.Log("beginCollision " + collidingObject.tag);
    }

    @Override
    public void endCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        Debug.Log("endCollision " + collidingObject.tag);
    }

}
