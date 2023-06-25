package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.joml.Vector2f;
import physics2d.components.RigidBody2D;

public class BounceCoin extends Component {
    private transient StateMachine stateMachine;
    private transient RigidBody2D rb;
    private transient float upTime;
    private transient float velocityY;
    private transient Vector2f startPosition;

    @Override
    public void start() {
        this.stateMachine = this.gameObject.getComponent(StateMachine.class);
        this.stateMachine.setCurrentState("FromBlock");
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.upTime = 0.15f;
        this.velocityY = 0.1f;
        this.startPosition = new Vector2f(this.gameObject.transform.position);
    }

    @Override
    public void update(float dt) {
        if (upTime > 0) {
            upTime -= dt;
            this.gameObject.transform.position.y += velocityY;
        } else {
            this.gameObject.transform.position.y -= velocityY;
            if (this.gameObject.transform.position.y <= startPosition.y) this.gameObject.destroy();
        }
    }
}
