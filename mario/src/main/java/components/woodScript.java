package components;

import org.lwjgl.glfw.GLFW;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import system.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public class woodScript extends Component {
    private float v = 5;
    @Override
    public void update(float dt){
        if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT)){
            this.gameObject.transform.position.x += v*dt;
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_LEFT)){
            this.gameObject.transform.position.x -= v*dt;
        }
    }
}
