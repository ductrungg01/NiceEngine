package components.scripts.test;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;

public class Enemy extends Component {

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        MainCharacterMoving mainCharacterMoving = collidingObject.getComponent(MainCharacterMoving.class);

        if (mainCharacterMoving != null) {
            this.gameObject.destroy();
        }
    }
}