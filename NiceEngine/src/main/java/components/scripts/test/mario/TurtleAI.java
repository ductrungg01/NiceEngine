package components.scripts.test.mario;

import components.Component;
import components.StateMachine;
import components.scripts.test.MarioMoving;
import components.scripts.test.mario.Fireball;
import components.scripts.test.mario.GoombaAI;
import components.scripts.test.mario.PlayerController;
import editor.Debug;
import physics2d.components.CircleCollider;
import renderer.DebugDraw;
import system.Camera;
import system.GameObject;
import system.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;
import util.AssetPool;

public class TurtleAI extends Component {
    //region Fields
    private transient boolean goingRight = false;
    private transient RigidBody2D rb;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient boolean isMoving = false;
    private transient StateMachine stateMachine;
    private float movingDebounce = 0.32f;
    //endregion

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.2f;
    }

    /**
     * // Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {
        movingDebounce -= dt;
        Camera camera = Window.getScene().camera();
        if (this.gameObject.transform.position.x > camera.position.x + camera.getProjectionSize().x * camera.getZoom()) {
            return;
        }

        if (!isDead || isMoving) {
            if (goingRight) {
                this.gameObject.transform.scale.x = -0.25f;
                velocity.x = walkSpeed;
            } else {
                this.gameObject.transform.scale.x = 0.25f;
                velocity.x = -walkSpeed;
            }
        } else {
            velocity.x = 0;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(velocity);

        if (this.gameObject.transform.position.x
                < Window.getScene().camera().position.x - 0.5f
            //|| this.gameObject.transform.position.y < 0.0f
        ) {
            this.gameObject.destroy();
        }

        if (isDead) {
            if (isMoving) {
                this.stateMachine.setCurrentState("TurtleShellSpinMoving");
            } else {
                this.stateMachine.setCurrentState("TurtleShellSpin");
            }
        }
        Debug.Log("isDead:" + isDead + "  |  " + "isMoving: " + isMoving + "  | State: " + this.stateMachine.getCurrentStateTitle());
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        MarioMoving marioMoving = obj.getComponent(MarioMoving.class);
        if (marioMoving != null) {
            if (!isDead && !marioMoving.isDead() && !marioMoving.isHurtInvincible() && contactNormal.y > 0.58f) {
                marioMoving.enemyBounce();
                stomp();
                walkSpeed *= 4.0f;
            } else if (movingDebounce < 0 && !marioMoving.isDead() && !marioMoving.isInvincible() &&
                    (isMoving || !isDead) && contactNormal.y < 0.58f) {
                marioMoving.die();
            } else if (!marioMoving.isDead() && !marioMoving.isHurtInvincible()) {
                if (isDead && contactNormal.y > 0.58f) {
                    marioMoving.enemyBounce();
                    isMoving = !isMoving;
                    goingRight = contactNormal.x < 0;
                } else if (isDead && !isMoving) {
                    isMoving = true;
                    goingRight = contactNormal.x < 0;
                    movingDebounce = 0.32f;
                }
            }
        } else if (Math.abs(contactNormal.y) < 0.1f && !obj.isDead()) {
            goingRight = contactNormal.x < 0;
            AssetPool.getSound("assets/sounds/bump.ogg").play();
        }


        if (obj.getComponent(Fireball.class) != null) {
            stomp();
            obj.getComponent(Fireball.class).disappear();
        }
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        GoombaAI goomba = obj.getComponent(GoombaAI.class);
        if (isDead && isMoving && goomba != null) {
            goomba.stomp(false);
            contact.setEnabled(false);
            AssetPool.getSound("assets/sounds/kick.ogg").play();
        }
    }
    //endregion

    //region Methods
    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.2f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    public void stomp() {
        this.isDead = true;
        this.isMoving = false;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.stateMachine.setCurrentState("TurtleShellSpin");
        this.gameObject.transform.scale.y = 0.25f;
        this.gameObject.getComponent(CircleCollider.class).setRadius(0.125f);
        AssetPool.getSound("assets/sounds/bump.ogg").play();
    }
    //endregion
}
