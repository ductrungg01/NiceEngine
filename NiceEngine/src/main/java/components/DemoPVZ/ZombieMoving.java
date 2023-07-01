package components.DemoPVZ;

import components.Component;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;

public class ZombieMoving extends Component {
    private float speed = 0;

    private transient RigidBody2D rb;

    private transient boolean isEating = false;

    @Override
    public void start() {
        rb = this.gameObject.getComponent(RigidBody2D.class);
    }

    @Override
    public void update(float dt) {
        if (isEating){
            rb.setVelocity(new Vector2f());
        } else {
            rb.setVelocity(new Vector2f(-speed, 0));
        }
    }

    public void isEating(boolean isEating){
        this.isEating = isEating;
    }
}
