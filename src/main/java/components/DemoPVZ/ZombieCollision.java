package components.DemoPVZ;

import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;

public class ZombieCollision extends Component {
    private transient StateMachine stateMachine;

    @Override
    public void start() {
        stateMachine = this.gameObject.getComponent(StateMachine.class);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("Plant")) {
            stateMachine.setCurrentState("Eat");
            this.gameObject.getComponent(ZombieMoving.class).isEating(true);
            collidingObject.getComponent(Plant.class).setAsBeingEaten(true);
        }
    }
}
