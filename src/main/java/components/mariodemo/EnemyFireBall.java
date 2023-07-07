package components.mariodemo;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;

public class EnemyFireBall extends Component {
    protected transient boolean goingRight = false;
    protected float fireballSpeed = 2.5f;
    protected Vector2f velocity = new Vector2f();
    private RigidBody2D rb;

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
    }

    @Override
    public void update(float dt) {
        if (goingRight) {
            velocity.x = fireballSpeed;
        } else {
            velocity.x = -fireballSpeed;
        }
        this.rb.setVelocity(velocity);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (collidingObject.compareTag("Mario")) {
            MarioMoving.getHit();
        }
    }
}
