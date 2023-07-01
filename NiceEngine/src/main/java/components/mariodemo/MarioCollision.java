package components.mariodemo;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;
import system.GameObject;

public class MarioCollision extends Component {
    private float collisionDebounce = 0.12f;
    private MarioMoving marioMoving;
    private RigidBody2D rb;

    @Override
    public void start() {
        marioMoving = this.gameObject.getComponent(MarioMoving.class);
        rb = this.gameObject.getComponent(RigidBody2D.class);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (this.marioMoving.isDead) {
            this.rb.setIsSensor();
            return;
        }
        if (collidingObject.compareTag("Coin")) {
            if (!collidingObject.isDead()) {
                MarioEventHandler.handleEvent(MarioEvent.GetCoin);
                collidingObject.destroy();
            }
        }

        if (collidingObject.compareTag("Mushroom") && !collidingObject.isDead()) {
            if (!collidingObject.isDead()) {
                collidingObject.destroy();
                MarioEventHandler.addPoint(collidingObject.transform.position, 1000);
                MarioEventHandler.handleEvent(MarioEvent.LevelUp);
            }
        }

        if (collidingObject.compareTag("InvisibleWall") && MarioMoving.endScene) {
            MarioEventHandler.handleEvent(MarioEvent.GameOver);
        }

        if (collidingObject.compareTag("HighGround")) {
            float posYP = this.gameObject.transform.position.y - (this.gameObject.transform.scale.y / 2);
            float posYT = collidingObject.transform.position.y + (collidingObject.transform.scale.y / 2);
            if (contactNormal.y > 0 || posYP + collisionDebounce < posYT) {
                contact.setEnabled(false);
            }
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("HighGround") || collidingObject.compareTag("Enemy")) {
            float posYP = this.gameObject.transform.position.y - (this.gameObject.transform.scale.y / 2);
            float posYT = collidingObject.transform.position.y + (collidingObject.transform.scale.y / 2);
            if (hitNormal.y > 0 || posYP + collisionDebounce < posYT) {
                contact.setEnabled(false);
            }
        }
    }
}
