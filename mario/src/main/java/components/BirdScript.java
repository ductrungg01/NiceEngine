package components;

import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;

public class BirdScript extends Component{

    @Override
    public void start(){
        this.gameObject.getComponent(RigidBody2D.class).setGravityScale(0.0f);
    }
}
