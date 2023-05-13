package system;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    //region Fields
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];
    private boolean keyRelease[] = new boolean[350];
    //endregion

    //region Contructors
    private KeyListener() {

    }
    //endregion

    //region Methods
    public static void endframe() {
        Arrays.fill(get().keyBeginPress, false);
        Arrays.fill(get().keyRelease, false);
    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            get().keyBeginPress[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            get().keyBeginPress[key] = false;
            get().keyRelease[key] = true;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < get().keyPressed.length) {
            return get().keyPressed[keyCode];
        } else {
            return false;
        }
    }

    public static boolean keyBeginPress(int keyCode) {
        return get().keyBeginPress[keyCode];
    }

    public static boolean isKeyRelease(int keyCode) {
        if (keyCode < get().keyPressed.length) {
            return get().keyRelease[keyCode];
        } else {
            return false;
        }
    }
    //endregion
}
