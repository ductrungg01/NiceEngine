package components.scripts.test;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;

public class MarioCollision extends Component {

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("Ground")) {
            this.gameObject.getComponent(MarioMoving.class).isOnGround = true;
        }

        if (collidingObject.compareTag("Coin")) {
            collidingObject.destroy();
        }

        if (collidingObject.compareTag("Wall")) {
            
        }
    }
}
