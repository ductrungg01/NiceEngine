package components.scripts.test.mario;

import components.Component;
import system.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import util.AssetPool;

public class Flower extends Component {
    //region Fields
    private transient RigidBody2D rb;
    //endregion

    //region Override methods
    /**
     * Start is called before the first frame update
     */
    @Override
    public void start(){
        this.rb = gameObject.getComponent(RigidBody2D.class);
        AssetPool.getSound("assets/sounds/powerup_appears.ogg").play();
        this.rb.setIsSensor();
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal){
        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null){
            playerController.powerup();
            this.gameObject.destroy();
        }
    }
    //endregion
}
