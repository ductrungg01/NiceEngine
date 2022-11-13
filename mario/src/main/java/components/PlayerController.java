package components;

import jade.KeyListener;
import jade.Window;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;
import physics2d.RaycastInfo;
import physics2d.components.RigidBody2D;
import renderer.DebugDraw;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component{
    private enum PlayerState {
        Small,
        Big,
        Fire,
        Invincible
    }

    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    private PlayerState playerState = PlayerState.Small;
    public transient boolean onGround = false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient RigidBody2D rb;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient int enemyBounce = 0;

    @Override
    public void start(){
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt){
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)){
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)){
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0){
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0){
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0){
                this.stateMachine.trigger("stopRunning");
            }
        }

        checkOnGround();

        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), - this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), - this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }

    public void checkOnGround(){
        Vector2f raycastBegin = new Vector2f(this.gameObject.transform.position);
        float innerPlayerWidth = this.playerWidth * 0.6f;
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        float yVal = playerState == PlayerState.Small ? -0.14f : -0.24f;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        onGround = (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);

        DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
        DebugDraw.addLine2D(raycast2Begin, raycast2End, new Vector3f(1, 0, 0));
    }
}
