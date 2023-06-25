package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.Window;

public class GoombaAI extends Component {

    //region Fields
    private boolean goingRight = false;
    private transient RigidBody2D rb;
    private float walkSpeed = 1f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f();
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient float timeToKill = 1f;
    private transient StateMachine stateMachine;

    //endregion

    //region Override methods

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
    }

    @Override
    public void update(float dt) {
        if (isDead || this.gameObject.transform.position.y < -0.5f) {
            timeToKill -= dt;
            if (timeToKill <= 0) {
                this.gameObject.destroy();
            } else {
                this.stateMachine.setCurrentState("Die");
            }
            this.rb.setVelocity(new Vector2f());
            return;
        }

        if (goingRight) {
            velocity.x = walkSpeed;
        } else {
            velocity.x = -walkSpeed;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }

        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y),
                -terminalVelocity.y);
        this.rb.setVelocity(velocity);

    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (isDead) {
            return;
        }
        if (obj.compareTag("Wall")) {
            goingRight = !goingRight;
        }

        if (obj.compareTag("InvisibleWall")) {
            contact.setEnabled(false);
        }

        if (obj.compareTag("Mario")) {
            if (contactNormal.y > 0.7f) {
                isDead = true;
                this.rb.setIsSensor();
                MarioEventHandler.handleEvent(MarioEvent.EnemyGetHit);
                MarioEventHandler.handleEvent(MarioEvent.MarioBounce);
            } else {
                contact.setEnabled(false);
                MarioEventHandler.handleEvent(MarioEvent.MarioGetHit);
            }
        }

    }
    //endregion

    //region Methods

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("InvisibleWall")) {
            contact.setEnabled(false);
        }
        if (collidingObject.compareTag("Mario")) {
            contact.setEnabled(false);
        }
    }

    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.14f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    //endregion
}
