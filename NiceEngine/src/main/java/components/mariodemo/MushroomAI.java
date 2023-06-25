package components.mariodemo;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.Window;

public class MushroomAI extends Component {
    private transient RigidBody2D rb;
    private transient Vector2f startPosition;
    private transient boolean goRight = false;
    private transient boolean goUp = true;

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.rb.setMass(0);
        this.rb.setGravityScale(0);
        this.rb.setFixedRotation(true);
        this.rb.setIsSensor();
        this.rb.setContinuousCollision(true);
        this.gameObject.tag = "Mushroom";
        this.gameObject.transform.zIndex = 6;
        this.startPosition = new Vector2f(this.gameObject.transform.position);
        GameObject player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            if (player.transform.position.x < this.startPosition.x + this.gameObject.transform.scale.x) goRight = true;
        }
    }

    @Override
    public void update(float dt) {
        if (goUp && this.gameObject.transform.position.y < this.startPosition.y + this.gameObject.transform.scale.y) {
            this.rb.setVelocity(new Vector2f(0, 0.75f));
        } else {
            goUp = false;
            this.rb.setVelocity(new Vector2f(goRight ? 1.75f : -1.75f, -2.5f));
            this.rb.setNotSensor();
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("Wall") || (collidingObject.compareTag("Ground") && Math.abs(hitNormal.x) > 0.75f)) {
            goRight = !goRight;
        }

        if (!collidingObject.compareTag("Ground") && !collidingObject.compareTag("HighGround")) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (!collidingObject.compareTag("Ground") && !collidingObject.compareTag("HighGround")) {
            contact.setEnabled(false);
        }
    }
}
