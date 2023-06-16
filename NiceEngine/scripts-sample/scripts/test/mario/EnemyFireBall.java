package components.scripts.test.mario;

import components.scripts.test.MarioMoving;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;

public class EnemyFireBall extends Fireball{
    @Override
    public void update(float dt) {
        if (goingRight){
            velocity.x = fireballSpeed;
        } else {
            velocity.x = -fireballSpeed;
        }

        velocity.y = diffY;
        this.rb.setVelocity(velocity);
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (collidingObject.compareTag("Mario")) {
            collidingObject.getComponent(MarioMoving.class).die();
            this.gameObject.destroy();
        }
    }
}
