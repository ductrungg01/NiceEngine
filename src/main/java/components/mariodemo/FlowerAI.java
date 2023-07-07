package components.mariodemo;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.Transform;
import system.Window;

public class FlowerAI extends Component {
    transient boolean active;
    transient boolean isUp;
    transient Transform transform;
    transient GameObject player;
    transient float upRange = 0.5f;
    transient float upStep = upRange / 20;
    transient float diffY = 0;
    transient float delay;
    private float delayTime = 2f;
    private float inactiveRange = 1f;
    private float activeRange = 2.5f;
    private transient RigidBody2D rb;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.transform = gameObject.getComponent(Transform.class);
        this.delay = delayTime;
        this.active = false;
        this.isUp = true;
    }

    @Override
    public void update(float dt) {
        this.transform.zIndex = -1;
        float posX = this.transform.position.x + (this.transform.scale.x / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            float posXPlayer = player.transform.position.x + (player.transform.scale.x / 2);
            if ((Math.abs(posXPlayer - posX) < inactiveRange || Math.abs(posXPlayer - posX) > activeRange) && isUp && diffY <= 0 && active) {
                active = false;
                delay = delayTime;
            } else {
                active = true;
            }
        }

        delay -= dt;
        if (delay > 0) return;
        if (active) {
            if (isUp) {
                if (diffY < upRange) {
                    gameObject.getComponent(Transform.class).position.y += upStep;
                    diffY += upStep;
                } else {
                    isUp = false;
                    delay = delayTime;
                }
            } else {
                if (diffY > 0) {
                    gameObject.getComponent(Transform.class).position.y -= upStep;
                    diffY -= upStep;
                } else {
                    isUp = true;
                    delay = delayTime;
                }
            }

        } else {
            if (diffY > 0) {
                gameObject.getComponent(Transform.class).position.y -= upStep;
                diffY -= upStep;
            } else isUp = true;
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (collidingObject.compareTag("Mario")) {
            contact.setEnabled(false);
            MarioEventHandler.handleEvent(MarioEvent.MarioGetHit);
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("Mario")) {
            contact.setEnabled(false);
        }
    }
}
