package components.scripts.test;

import components.Component;
import components.StateMachine;
import editor.Debug;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;

public class MainCharacterCollision extends Component {

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("Ground")) {
            this.gameObject.getComponent(MainCharacterMoving.class).isOnGround = true;
        }

        if (collidingObject.compareTag("Coin")) {
            collidingObject.destroy();
        }
    }
}
