package components.scripts.test;

import components.Component;
import editor.Debug;
import org.joml.Vector2f;
import system.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public class TargetDebugging extends Component {
    @Override
    public void update(float dt) {
        ///Debug.Log("UID in Update: " + gameObject.getUid());
    }

    @Override
    public void editorUpdate(float dt) {
        ///Debug.Log("UID in Editor (TargetDebugging): " + gameObject.getUid());

    }

    public void TestMethod_TalkSomething() {
        Debug.Log("Hi bro, I am TestMethod");
    }

    public void GoUp(float length) {
        gameObject.transform.position.y += length;
        Debug.Log("I'm SPR-Target, I'm moving Up");
    }

    public void GoDown(float length) {
        gameObject.transform.position.y -= length;
        Debug.Log("I'm SPR-Target, I'm moving Down");
    }
}
