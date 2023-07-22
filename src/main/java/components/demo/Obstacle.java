package components.demo;

import components.Component;
import components.DemoChess.ChessPosition;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;

import java.util.Random;

public class Obstacle extends Component {
    private transient Vector2f velo;

    @Override
    public void start() {
        this.gameObject.getComponent(RigidBody2D.class).setGravityScale(0);

        Random rnd = new Random();
        velo = new Vector2f(0, -rnd.nextFloat(0, 1));
    }

    @Override
    public void update(float dt) {
        this.gameObject.getComponent(RigidBody2D.class).setVelocity(velo);

        if (this.gameObject.transform.position.y < -5){
            this.gameObject.destroy();
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("Obstacle")){
            contact.setEnabled(false);
        }
    }
}
