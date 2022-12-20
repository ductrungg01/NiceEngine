package components;

import system.GameObject;
import system.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;

public class Fireball extends Component{
    //region Fields
    public transient boolean goingRight = false;
    private transient RigidBody2D rb;
    private transient float fireballSpeed = 1.7f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround = false;
    private transient float lifeTime = 4.0f;

    private static int fireballCount = 0;
    public static boolean canSpawn(){
        return fireballCount < 4;
    }
    //endregion

    //region Override methods
    /**
     * Start is called before the first frame update
     */
    @Override
    public void start(){
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        fireballCount++;
    }

    /**
     * // Update is called once per frame
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt){
        if (goingRight){
            velocity.x = fireballSpeed;
        } else {
            velocity.x = -fireballSpeed;
        }

        checkOnGround();
        if (onGround){
            this.acceleration.y = 1.5f;
            this.velocity.y = 2.5f;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }

        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y),
                -terminalVelocity.y);
        this.rb.setVelocity(velocity);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal){
        if (Math.abs(contactNormal.x ) > 0.8f){
            this.goingRight = contactNormal.x < 0;
        }
    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal){
        if (obj.getComponent(PlayerController.class) != null
                || obj.getComponent(Fireball.class) != null){
            contact.setEnabled(false);
        }
    }
    //endregion

    //region Methods
    public void checkOnGround(){
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.09f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    public void disappear(){
        fireballCount--;
        this.gameObject.destroy();
    }
    //endregion
}
