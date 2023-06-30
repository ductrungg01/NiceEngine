package components.mariodemo;

import components.Component;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;

public class KnockUpEffect extends Component {
    protected transient boolean isKnockUp = false;
    private transient float knockUpTime = 0.4f;
    private transient float knockUpVelocityY = 4f;
    private RigidBody2D rb;

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
    }

    @Override
    public void update(float dt) {
        if (isKnockUp) {
            if (knockUpTime > 0) {
                knockUpTime -= dt;
                this.rb.setIsSensor();
                this.rb.setVelocity(new Vector2f(0, knockUpVelocityY));
            } else {
                this.rb.setVelocity(new Vector2f(0, -knockUpVelocityY));
                if (this.gameObject.transform.position.y < -1f) this.gameObject.destroy();
            }
        }
    }
}
