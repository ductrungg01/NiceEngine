package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;
import system.Camera;
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
    private transient boolean active;

    //endregion

    //region Override methods

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        this.active = false;
    }

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();

        if (active && this.gameObject.transform.position.x < camera.position.x - 0.5f) {
            this.gameObject.destroy();
            return;
        }

        if (!active && this.gameObject.transform.position.x > camera.position.x - 0.25f && this.gameObject.transform.position.x < camera.position.x + camera.getProjectionSize().x + 0.5f) {
            active = true;
            return;
        }

        if (!active) {
            this.rb.setVelocity(new Vector2f(0, 0));
            return;
        }

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
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (isDead || this.rb.isSensor()) {
            this.rb.setIsSensor();
            return;
        }
        if ((collidingObject.compareTag("Wall") || collidingObject.compareTag("Ground")) && Math.abs(hitNormal.x) > 0.8f) {
            goingRight = !goingRight;
        }

        if (collidingObject.compareTag("InvisibleWall") || collidingObject.compareTag("Enemy")) {
            contact.setEnabled(false);
        }

        if (collidingObject.compareTag("Mario")) {
            if (hitNormal.y > 0.6f) {
                isDead = true;
                this.rb.setIsSensor();
                MarioEventHandler.handleEvent(MarioEvent.EnemyGetHit);
                MarioEventHandler.handleEvent(MarioEvent.MarioBounce);
                MarioEventHandler.addPoint(this.gameObject.transform.position, 100);
            } else if (!isDead) {
                contact.setEnabled(false);
                MarioEventHandler.handleEvent(MarioEvent.MarioGetHit);
            }
        }

    }
    //endregion

    //region Methods

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("InvisibleWall") || collidingObject.compareTag("Enemy")) {
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
