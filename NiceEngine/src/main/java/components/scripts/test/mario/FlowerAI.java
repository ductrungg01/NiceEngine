package components.scripts.test.mario;

import components.Component;
import components.scripts.test.MarioMoving;
import editor.Debug;
import physics2d.components.Box2DCollider;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.Transform;
import system.Window;

public class FlowerAI extends Component {
    private transient RigidBody2D rb;
    transient boolean active = false;
    transient boolean isUp = false;
    transient Transform transform;
    transient GameObject player;
    transient float UnactiveRange = 1f;
    transient float downRange = 0.5f;
    transient float diffY = 0;
    transient float delayTime = 1.2f;
    transient float delay = delayTime;




    @Override
    public void start() {
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.transform = gameObject.getComponent(Transform.class);
    }

    @Override
    public void update(float dt) {
        float posX =  this.transform.position.x + (this.transform.scale.x / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            float posXPlayer = player.transform.position.x + (player.transform.scale.x / 2);
            if (Math.abs(posXPlayer - posX) < UnactiveRange) {
                this.active = false;
            } else {
                this.active = true;
            }
        }
        delay -= dt;
        if (active) {
            if (delay > 0) return;
            if (isUp) {
                if (diffY > 0) {
                    gameObject.getComponent(Transform.class).position.y += 0.02f;
                    diffY -= 0.02f;
                } else {
                    isUp = false;
                    delay = delayTime;
                }
            } else {
                if (diffY < downRange) {
                    gameObject.getComponent(Transform.class).position.y -= 0.02f;
                    diffY += 0.02f;
                } else {
                    isUp = true;
                    delay = delayTime;
                }
            }

        } else {
            if (diffY < downRange) {
                gameObject.getComponent(Transform.class).position.y -= 0.02f;
                diffY += 0.02f;
            } else isUp = true;
        }
    }
}
