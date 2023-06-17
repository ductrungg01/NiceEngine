package components.mariodemo;

import components.Component;
import components.SpriteRenderer;
import components.StateMachine;
import editor.Debug;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.KeyListener;

public class MarioMoving extends Component {
    protected transient RigidBody2D rb;
    protected transient SpriteRenderer spr;
    protected transient StateMachine stateMachine;

    protected Vector2f maxVelocity = new Vector2f(1.5f, 2.2f);
    protected Vector2f acceleration = new Vector2f(5f, 0);
    protected float SLOWDOWN_FORCE = 5f;
    protected Vector2f velocity;
    protected float directionChangeDebounce = 0.2f;
    protected float velocityDebounce = 0.2f;
    protected float jumpTimeDebounce = 0.45f;
    protected float jumpTimeBuffer = 0.32f;
    protected float maxJumpTime = 0.8f;
    protected float jumpTime = 0;
    protected float startJumpTime = 0;
    protected float directionChangeTime = 0;
    protected boolean disableJump = false;
    protected boolean isOnGround = false;
    protected boolean isDead = false;

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.spr = this.gameObject.getComponent(SpriteRenderer.class);
        stateMachine = this.gameObject.getComponent(StateMachine.class);
        this.velocity = this.rb.getVelocity();
    }
    @Override
    public void update(float dt) {
        // handle velocity X
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            if (velocity.x < 0) {
                directionChangeTime = directionChangeDebounce;
                velocity.x = 0;
            } else {
                if (directionChangeTime <= 0) {
                    velocity.x += dt * acceleration.x;
                    velocity.x = Math.min(velocity.x, maxVelocity.x);
                }
            }
        } else if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            if (velocity.x > 0) {
                directionChangeTime = directionChangeDebounce;
                velocity.x = 0;
            } else {
                if (directionChangeTime <= 0) {
                    velocity.x -= dt * acceleration.x;
                    velocity.x = Math.max(velocity.x, -maxVelocity.x);
                }
            }
        } else {
            if (Math.abs(velocity.x) > velocityDebounce) {
                velocity.x -= dt * (velocity.x > 0 ? SLOWDOWN_FORCE : -SLOWDOWN_FORCE) ;
                velocity.x = Math.max( 0, Math.abs(velocity.x)) * Math.signum(velocity.x);
                velocity.x = Math.abs(velocity.x) > velocityDebounce ? velocity.x : 0;
            } else velocity.x = 0;
        }
        if (directionChangeTime > 0) {
            directionChangeTime -= dt;
        }

        //handle velocity Y
        isOnGround = Physics2D.checkOnGround(this.gameObject, this.gameObject.transform.scale.x, -0.15f);
        if (isOnGround) velocity.y = 0;
        if (KeyListener.keyBeginPress(GLFW.GLFW_KEY_SPACE) && !disableJump && isOnGround) {
            startJumpTime = maxJumpTime;
            jumpTime = jumpTimeDebounce;
        }
        if (jumpTime > 0 && startJumpTime > 0) {
            jumpTime -= dt;
            startJumpTime -= dt;
            velocity.y = maxVelocity.y;
        } else {
            if (!disableJump && startJumpTime > 0) {
                jumpTime = jumpTimeBuffer;
                disableJump = true;
            } else {
                if (!isOnGround) {
                    velocity.y = -maxVelocity.y ;
                }
            }
        }
        if (KeyListener.isKeyRelease(GLFW.GLFW_KEY_SPACE)) {
            disableJump = true;
            startJumpTime = jumpTime;
        }
        if (isOnGround) {
            disableJump = false;
            jumpTime = 0;
            startJumpTime = 0;
        }

        this.rb.setVelocity(velocity);
        this.rb.setAngularVelocity(0);
        setState();

//        Debug.Log(("velX: " + velocity.x));
//        Debug.Log(("velY: " + velocity.y));
//        Debug.Log(("accel: " + acceleration.y));
//        Debug.Log(("jumpTime: " + jumpTime));
//        Debug.Log(("OnGround: " + isOnGround));
//        Debug.Log(("disableJump: " + disableJump));
//        Debug.Log(("startJumpTime: " + startJumpTime));
    }
    void setState() {
        if (velocity.x != 0) {
            this.gameObject.transform.scale.x *= Math.signum(this.gameObject.transform.scale.x * velocity.x);
        }

        if (!isOnGround) {
            stateMachine.setCurrentState("Jump");
            return;
        }

        if (Math.abs(velocity.x) > velocityDebounce ) {
            stateMachine.setCurrentState("Run");
        } else {
            if (directionChangeTime > 0) {
                stateMachine.setCurrentState("ChangeDirection");
            } else {
                stateMachine.setCurrentState("Idle");
            }
        }
    }
}
