package components.scripts.test.mario;

import components.Component;
import components.StateMachine;
import components.scripts.test.MarioMoving;
import editor.Debug;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.RigidBody2D;
import system.GameObject;
import system.Transform;
import system.Window;

public class TopGround extends Component {
    private transient RigidBody2D rb;
    private transient boolean isActive = false;
    private transient Transform transform;
    private transient GameObject player;
    private transient Box2DCollider originalCollider;
    private transient float bound = 0.2f;
    @Override
    public void start(){
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.transform = gameObject.getComponent(Transform.class);
        this.originalCollider = gameObject.getComponent(Box2DCollider.class);
    }

    @Override
    public void update(float dt) {
        float posYGround =  this.transform.position.y + (this.transform.scale.y / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            float posYPlayer = player.transform.position.y + (player.transform.scale.y / 2);
            Debug.Log("PosY Player:" + posYPlayer);
            Debug.Log("PosY TopGround:" + posYGround);
            if (posYPlayer <= posYGround) {
                gameObject.removeComponent(Box2DCollider.class);
                Debug.Log(gameObject.getComponent(Box2DCollider.class) == null);
            } else {
                gameObject.addComponent(new Box2DCollider(originalCollider));
            }
        }
    }
}
