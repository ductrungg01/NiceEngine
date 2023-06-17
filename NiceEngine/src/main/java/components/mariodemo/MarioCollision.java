package components.mariodemo;

import components.Component;
import editor.Debug;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import system.GameObject;

public class MarioCollision extends MarioMoving {
    private float collisionDebounce = 0.1f;
    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;
        if (collidingObject.compareTag("Coin")) {
            collidingObject.destroy();
        }

        if (collidingObject.compareTag("Wall")) {

        }

        if (collidingObject.compareTag("HighGround")) {
            float posYP = this.gameObject.transform.position.y - (this.gameObject.transform.scale.y / 2);
            float posYT = collidingObject.transform.position.y + (collidingObject.transform.scale.y / 2);
            if (contactNormal.y > 0 && posYP < posYT + collisionDebounce) {
                contact.setEnabled(false);
                this.rb.setIsSensor();
                return;
            }
        }
        if (collidingObject.getComponent(Box2DCollider.class) != null && !this.rb.isSensor()) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = -maxVelocity.y;
                this.jumpTime = 0;
                this.startJumpTime = 0;
            }
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("HighGround") || collidingObject.compareTag("Enemy")) {
            float posYP = this.gameObject.transform.position.y - (this.gameObject.transform.scale.y / 2);
            float posYT = collidingObject.transform.position.y + (collidingObject.transform.scale.y / 2);
            Debug.Log("PosYP: " + posYP + " | PosYT: " + posYT);
            if (hitNormal.y > 0 && posYP < posYT + collisionDebounce) {
                contact.setEnabled(false);
                return;
            }
        }
    }

    @Override
    public void endCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("HighGround")) {
            this.rb.setNotSensor();
        }
    }

    @Override
    public void postSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("HighGround")) {
            this.rb.setNotSensor();
        }
    }
}
