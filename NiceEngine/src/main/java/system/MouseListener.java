package system;

import editor.AssetsWindow;
import editor.Debug;
import editor.MessageBox;
import imgui.ImVec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import util.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    //region Fields
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, worldX, worldY, lastX, lastY, lastWorldX, lastWorldY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean mouseBeginPress[] = new boolean[3];
    private boolean mouseRelease[] = new boolean[3];
    private boolean isDragging;
    private int mouseButtonDown = 0;
    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();
    //endregion

    //region Constructors
    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;

    }
    //endregion

    //region Methods
    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public static boolean mouseBeginPress(int button) {
        if (button < get().mouseBeginPress.length) {
            return get().mouseBeginPress[button];
        } else {
            return false;
        }
    }

    public static boolean isMouseRelease(int button) {
        if (button < get().mouseRelease.length) {
            return get().mouseRelease[button];
        } else {
            return false;
        }
    }

    public static Vector2f screenToWorld(Vector2f screenCoords) {
        Vector2f normalizeScreenCoords = new Vector2f(
                screenCoords.x / Window.getWidth(),
                screenCoords.y / Window.getHeight()
        );

        normalizeScreenCoords.mul(2.0f).sub(new Vector2f(1.0f, 1.0f));
        Camera camera = Window.getScene().camera();
        Vector4f tmp = new Vector4f(normalizeScreenCoords.x, normalizeScreenCoords.y, 0, 1);

        Matrix4f inverseView = new Matrix4f(camera.getInverseView());
        Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjection());
        tmp.mul(inverseView.mul(inverseProjection));

        return new Vector2f(tmp.x, tmp.y);
    }

    public static Vector2f worldToScreen(Vector2f worldCoords) {
        Camera camera = Window.getScene().camera();

        Vector4f ndcSpacePos = new Vector4f(worldCoords.x, worldCoords.y, 0, 1);
        Matrix4f view = new Matrix4f(camera.getViewMatrix());
        Matrix4f projection = new Matrix4f(camera.getProjectionMatrix());
        ndcSpacePos.mul(projection.mul(view));
        Vector2f windowSpace = new Vector2f(ndcSpacePos.x, ndcSpacePos.y).mul(1.0f / ndcSpacePos.w);
        windowSpace.add(new Vector2f(1.0f, 1.0f)).mul(0.5f);
        windowSpace.mul(new Vector2f(Window.getWidth(), Window.getHeight()));

        return windowSpace;
    }

    public static void endFrame() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        Arrays.fill(get().mouseBeginPress, false);
        Arrays.fill(get().mouseRelease, false);
    }

    public static void clear() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().xPos = 0.0;
        get().yPos = 0.0;
        get().lastX = 0.0;
        get().lastY = 0.0;
        get().mouseButtonDown = 0;
        get().isDragging = false;
        Arrays.fill(get().mouseButtonPressed, false);
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        if (!Window.getImguiLayer().getGameViewWindow().getWantCaptureMouse()) {
            clear();
        }

        if (get().mouseButtonDown > 0) {
            get().isDragging = true;
        }

        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().lastWorldX = get().worldX;
        get().lastWorldY = get().worldY;
        get().xPos = xPos;
        get().yPos = yPos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().mouseButtonDown++;

            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
                get().mouseBeginPress[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (get().mouseButtonDown > 0)
                get().mouseButtonDown--;

            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().mouseBeginPress[button] = false;
                get().mouseRelease[button] = true;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }
    //endregion

    public static boolean isMouseInRange(double pos, double start, double end) {
        return pos >= start && pos <= end;
    }

    public static void mouseDropCallback(long window, int count, long names) {

        //get size widget
        ImVec2 pos = AssetsWindow.getWidgetPos();
        ImVec2 size = AssetsWindow.getWidgetSize();

        //get mouse pos
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        glfwGetCursorPos(window, xPos, yPos);

        if (isMouseInRange(xPos[0], pos.x, pos.x + size.x) && isMouseInRange(yPos[0], pos.y, pos.y + size.y)) {
            Debug.Log("Chuot trong size widget");
            PointerBuffer paths = MemoryUtil.memPointerBuffer(names, count);
            for (int i = 0; i < count; i++) {
                String filePath = MemoryUtil.memUTF8(paths.get(i));

                //copy file
                File srcFile = new File(filePath);
                File desFile = new File(AssetsWindow.getCurrentOpenFolder() + "/" + srcFile.getName());

                FileUtils.copyFile(srcFile, desFile);
            }
        } else {
            Debug.Log("Chuot ngoai size widget");
        }
    }

    //region Properties
    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }


    public static float getScreenX() {
        return getScreen().x;
    }

    public static float getScreenY() {
        return getScreen().y;
    }

    public static Vector2f getScreen() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 1920.0f;
        float currentY = getY() - get().gameViewportPos.y;
        currentY = 1080.0f - ((currentY / get().gameViewportSize.y) * 1080.0f);
        return new Vector2f(currentX, currentY);
    }

    public static float getWorldX() {
        return getWorld().x;
    }

    public static float getWorldY() {
        return getWorld().y;
    }

    public static Vector2f getWorld() {
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

    //endregion
}
