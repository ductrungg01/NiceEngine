package components.scripts.test.flappybird;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;

public class BirdScript extends Component {

    @Override
    public void start(){
        //this.gameObject.getComponent(RigidBody2D.class).setVelocity(new Vector2f(0, -9.81f));
    }
}
