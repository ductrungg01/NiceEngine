package components;

import physics2d.components.RigidBody2D;
import system.KeyListener;
import system.MouseListener;

import static org.lwjgl.glfw.GLFW.*;

public class JumpBySpaceScript extends Component{

    private transient boolean isJump = false;
    private transient float jumpTime = 0.15f;
    private transient float jumpTimeRemain = jumpTime;
    private transient float force = 2.5f;
    private transient float forceRemain = force;

    @Override
    public void update(float dt){
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) && !isJump){
            isJump = true;
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
            this.gameObject.transform.position.y -= 0.01f;
        }
    }
}
