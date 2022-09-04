package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, worldX, worldY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;
    private int mouseButtonDown = 0;
    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
    }

    public static MouseListener get(){
        if (MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        if (get().mouseButtonDown > 0){
            get().isDragging = true;
        }

        get().xPos = xPos;
        get().yPos = yPos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (action == GLFW_PRESS){
            get().mouseButtonDown++;

            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE){
            get().mouseButtonDown--;

            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }

    public static float getScrollY(){
        return (float)get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public static float getScreenX(){
        return getScreen().x;
    }

    public static float getScreenY(){
        return getScreen().y;
    }

    public static Vector2f getScreen(){
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 1920.0f;
        float currentY = getY() - get().gameViewportPos.y;
        currentY = 1080.0f - ((currentY / get().gameViewportSize.y) * 1080.0f);
        return new Vector2f(currentX, currentY);
    }

    public static float getWorldX(){
        return getWorld().x;
    }

    public static float getWorldY(){
        return getWorld().y;
    }

    public static Vector2f getWorld(){
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 2.0f - 1.0f;
        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(currentX, currentY, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f inverseView = new Matrix4f(camera.getInverseView());
        Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjection());
        tmp.mul(inverseView.mul(inverseProjection));

        return new Vector2f(tmp.x, tmp.y);
    }
    public static void setGameViewportPos(Vector2f gameViewportPos) {
        get().gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize) {
        get().gameViewportSize.set(gameViewportSize);
    }
}
