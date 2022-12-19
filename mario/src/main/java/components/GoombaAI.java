package components;

import system.Camera;
import system.GameObject;
import system.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;
import util.AssetPool;

public class GoombaAI extends Component {

    //region Fields
    private transient boolean goingRight = false;
    private transient RigidBody2D rb;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f();
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient float timeToKill = 0.5f;
    private transient StateMachine stateMachine;
    //endregion

    //region Override methods
    @Override
    public void start(){
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
    }

    @Override
    public void update(float dt){
        Camera camera = Window.getScene().camera();
        if (this.gameObject.transform.position.x >
                camera.position.x + camera.getProjectionSize().x * camera.getZoom()){
            return;
        }

        if (isDead){
            timeToKill -= dt;
            if (timeToKill <= 0){
                this.gameObject.destroy();
            }
            this.rb.setVelocity(new Vector2f());
            return;
        }

        if (goingRight){
            velocity.x = walkSpeed;
        } else {
            velocity.x = -walkSpeed;
        }

        checkOnGround();
        if (onGround){
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
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal){
        if (isDead){
            return;
        }

        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null){
            if (!playerController.isDead() && !playerController.isHurtInvincible()
                    && contactNormal.y > 0.58f){
                playerController.enemyBounce();
                stomp();
            } else if (!playerController.isDead() && !playerController.isInvincible()){
                playerController.die();
            }
        } else if (Math.abs(contactNormal.y) < 0.1f){
            goingRight = contactNormal.x < 0;
        }

        if (obj.getComponent(Fireball.class) != null){
            stomp();
            obj.getComponent(Fireball.class).disappear();
        }
    }
    //endregion

    //region Methods
    public void stomp(){
        stomp(true);
    }

    public void stomp(boolean playSound){
        this.isDead = true;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.stateMachine.trigger("SquashMe");
        this.rb.setIsSensor();
        if (playSound){
            AssetPool.getSound("assets/sounds/bump.ogg").play();
        }
    }

    public void checkOnGround(){
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.14f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }
    //endregion
}
