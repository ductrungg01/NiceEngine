package components.mariodemo;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.Transform;
import system.Window;

public class FlowerAI extends Component {
    transient boolean active = false;
    transient boolean isUp = false;
    transient Transform transform;
    transient GameObject player;
    transient float downRange = 0.5f;
    transient float diffY = 0;
    private float delayTime = 1.5f;
    transient float delay = delayTime;
    private float InactiveRange = 0.75f;
    private transient RigidBody2D rb;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.transform = gameObject.getComponent(Transform.class);
    }

    @Override
    public void update(float dt) {
        float posX = this.transform.position.x + (this.transform.scale.x / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            float posXPlayer = player.transform.position.x + (player.transform.scale.x / 2);
            if (Math.abs(posXPlayer - posX) < InactiveRange && isUp) {
                this.active = false;
                delay = delayTime;
            } else {
                this.active = true;
            }
        }
        if (active) {
            delay -= dt;
            if (delay > 0) return;
            if (isUp) {
                if (diffY > 0) {
                    gameObject.getComponent(Transform.class).position.y += 0.02f;
                    diffY -= 0.02f;
                } else {
                    isUp = false;
                    delay = delayTime;
                }
            } else {
                if (diffY < downRange) {
                    gameObject.getComponent(Transform.class).position.y -= 0.02f;
                    diffY += 0.02f;
                } else {
                    isUp = true;
                    delay = delayTime;
                }
            }

        } else {
            if (diffY < downRange) {
                gameObject.getComponent(Transform.class).position.y -= 0.02f;
                diffY += 0.02f;
            } else isUp = true;
        }
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (obj.compareTag("Mario")) {
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
