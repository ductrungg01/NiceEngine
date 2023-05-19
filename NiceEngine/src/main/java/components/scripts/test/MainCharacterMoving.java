package components.scripts.test;

import components.Component;
import editor.Debug;
import system.GameObject;
import system.KeyListener;
import system.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Math;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.PillboxCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class MainCharacterMoving extends Component {
    public float walkSpeed = 1.9f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient int jumpTime = 0;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    private RigidBody2D rb;

    public boolean isOnGround = false;

    @Override
    public void start() {
        rb = this.gameObject.getComponent(RigidBody2D.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt) {
        if ((KeyListener.isKeyPressed(GLFW_KEY_SPACE)) && (jumpTime > 0 || isOnGround || groundDebounce > 0)) {
            if ((isOnGround || groundDebounce > 0) && jumpTime == 0) {
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0f;
            isOnGround = false;
        } else if (!isOnGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        } else {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.acceleration.x = walkSpeed;

            if (this.velocity.x < 0) {
                this.velocity.x += slowDownForce;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x > 0) {
                this.velocity.x -= slowDownForce;
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }
}
