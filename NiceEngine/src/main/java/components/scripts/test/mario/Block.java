package components.scripts.test.mario;

import components.Component;
import system.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import util.AssetPool;

public abstract class Block extends Component {
    //region Fields
    private transient boolean bopGoingUp = true;
    private transient boolean doBopAnimation = false;
    private transient Vector2f bopStart;
    private transient Vector2f topBopLocation;
    private transient boolean active = true;

    public float bopSpeed = 0.4f;
    //endregion

    //region Properties
    public void setInactive() {
        this.active = false;
    }
    //endregion

    //region Override methods
    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        this.bopStart = new Vector2f(this.gameObject.transform.position);
        this.topBopLocation = new Vector2f(this.gameObject.transform.position).add(0.0f, 0.02f);
    }

    /**
     * // Update is called once per frame
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {
        if (doBopAnimation) {
            if (bopGoingUp) {
                if (this.gameObject.transform.position.y < topBopLocation.y) {
                    this.gameObject.transform.position.y += bopSpeed * dt;
                } else {
                    bopGoingUp = false;
                }
            } else {
                if (this.gameObject.transform.position.y > bopStart.y) {
                    this.gameObject.transform.position.y -= bopSpeed * dt;
                } else {
                    this.gameObject.transform.position.y = this.bopStart.y;
                    bopGoingUp = true;
                    doBopAnimation = false;
                }
            }
        }
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = obj.getComponent(PlayerController.class);
        // If the player hit the bottom of this block
        if (active && playerController != null && contactNormal.y < -0.8f) {
            doBopAnimation = true;
            AssetPool.getSound("assets/sounds/bump.ogg").play();
            playerHit(playerController);
        }
    }
    //endregion

    //region Methods
    abstract void playerHit(PlayerController playerController);
    //endregion
}