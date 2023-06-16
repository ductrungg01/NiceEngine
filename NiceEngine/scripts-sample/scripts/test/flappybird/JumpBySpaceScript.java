package components.scripts.test.flappybird;

import components.Component;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import system.KeyListener;
import system.MouseListener;

import static org.lwjgl.glfw.GLFW.*;

public class JumpBySpaceScript extends Component {
    private transient boolean isJump = false;
    private transient float jumpTime = 0.20f;
    private transient float jumpTimeRemain = jumpTime;
    private transient float force = 1.5f;
    private transient float forceRemain = force;

    @Override
    public void update(float dt){
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) && !isJump){
            isJump = true;
            this.gameObject.getComponent(RigidBody2D.class).setBodyType(BodyType.Static);
        }

        if (isJump){
            this.gameObject.transform.position.y += forceRemain * dt;

            jumpTimeRemain -= dt;
            forceRemain -= forceRemain * 0.005;
            if (jumpTimeRemain <= 0){
                isJump = false;
                jumpTimeRemain = jumpTime;
                forceRemain = force;
            }
        } else {
            //this.gameObject.transform.position.y -= 0.01f;
            this.gameObject.getComponent(RigidBody2D.class).setBodyType(BodyType.Dynamic);
        }
    }
}
