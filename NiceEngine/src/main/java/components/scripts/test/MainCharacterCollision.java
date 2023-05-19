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
        IsBlock isBlock = collidingObject.getComponent(IsBlock.class);

        if (isBlock != null) {
            this.gameObject.getComponent(StateMachine.class).setCurrentState("S1");
        }

        IsGround IsGround = collidingObject.getComponent(IsGround.class);
        if (IsGround != null) {
            this.gameObject.getComponent(MainCharacterMoving.class).isOnGround = true;
        }

        IsTrap isTrap = collidingObject.getComponent(IsTrap.class);
        if (isTrap != null) {
            this.gameObject.getComponent(StateMachine.class).setCurrentState("S2");
            Debug.Log("Collide with trap, change the animation");
        }
    }
}
