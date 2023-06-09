package editor;

import components.Gizmo;
import components.NonPickable;
import components.SpriteRenderer;
import components.StateMachine;
import editor.*;
import system.GameObject;
import system.KeyListener;
import system.MouseListener;
import system.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import renderer.DebugDraw;
import renderer.PickingTexture;
import scenes.Scene;
import util.Settings;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls {
    //region Fields
    static GameObject holdingObject = null;
    private static float debounceTime = 0.3f;
    private static float debounce = debounceTime;

    private static boolean boxSelectSet = false;
    private static Vector2f boxSelectStart = new Vector2f();
    private static Vector2f boxSelectEnd = new Vector2f();
    //endregion

    public void editorUpdate(float dt) {
        if (FileDialog.getInstance().isOpen() || !GameViewWindow.getInstance().getWantCaptureMouse()) return;
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT_CONTROL)) return;

        debounce -= dt;
        PickingTexture pickingTexture = Window.getImguiLayer().getInspectorWindow().getPickingTexture();
        Scene currentScene = Window.getScene();
        if (holdingObject != null) {
            boxSelectSet = false;
            holdingObject.setNoSerialize();
            float x = MouseListener.getWorldX();
            float y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int) Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int) Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                float halfWidth = Settings.GRID_WIDTH / 2.0f;
                float halfHeight = Settings.GRID_HEIGHT / 2.0f;
                if (MouseListener.isDragging() &&
                        !blockInSquare(holdingObject.transform.position.x - halfWidth,
                                holdingObject.transform.position.y - halfHeight)) {
                    place();
                } else if (!MouseListener.isDragging() && debounce < 0) {
                    place();
                    debounce = debounceTime;
                }
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
                holdingObject.destroy();
                holdingObject = null;
            }
        } else if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                Window.getImguiLayer().getInspectorWindow().setActiveGameObject(pickedObj);
                SceneHierarchyWindow.setSelectedGameObject(pickedObj);
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                if (Gizmo.getUsingGizmo()) return;
                Window.getImguiLayer().getInspectorWindow().clearSelected();
                SceneHierarchyWindow.clearSelectedGameObject();
            }
            this.debounce = debounceTime;
        } else if (MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            if (Gizmo.getUsingGizmo()) return;
            if (!boxSelectSet) {
                Window.getImguiLayer().getInspectorWindow().clearSelected();
                boxSelectStart = MouseListener.getScreen();
                boxSelectSet = true;
            }

            boxSelectEnd = MouseListener.getScreen();

            Vector2f boxSelectStartWorld = MouseListener.screenToWorld(boxSelectStart);
            Vector2f boxSelectEndWorld = MouseListener.screenToWorld(boxSelectEnd);

            Vector2f halfSize = (new Vector2f(boxSelectEndWorld).sub(boxSelectStartWorld)).mul(0.5f);

            DebugDraw.addBox2D(
                    new Vector2f(boxSelectStartWorld).add(halfSize),
                    new Vector2f(halfSize).mul(2.0f),
                    0.0f
            );
        } else if (boxSelectSet) {
            boxSelectSet = false;
            int screenStartX = (int) boxSelectStart.x;
            int screenStartY = (int) boxSelectStart.y;
            int screenEndX = (int) boxSelectEnd.x;
            int screenEndY = (int) boxSelectEnd.y;
            boxSelectStart.zero();
            boxSelectEnd.zero();

            if (screenEndX < screenStartX) {
                int tmp = screenStartX;
                screenStartX = screenEndX;
                screenEndX = tmp;
            }
            if (screenEndY < screenStartY) {
                int tmp = screenStartY;
                screenStartY = screenEndY;
                screenEndY = tmp;
            }

            if (MouseListener.isMouseRelease(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
                float[] gameObjectIds = pickingTexture.readPixels(
                        new Vector2i(screenStartX, screenStartY),
                        new Vector2i(screenEndX, screenEndY)
                );
                Set<Integer> uniqueGameObjectIds = new HashSet<>();
                for (float objId : gameObjectIds) {
                    uniqueGameObjectIds.add((int) objId);
                }

                for (Integer gameObjectId : uniqueGameObjectIds) {
                    GameObject pickedObj = Window.getScene().getGameObject(gameObjectId);
                    if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                        Window.getImguiLayer().getInspectorWindow().addActiveGameObject(pickedObj);
                    }
                }
                this.debounce = debounceTime;
            }
        }
    }

    //region Methods
    public void pickupObject(GameObject go) {
        if (holdingObject != null) {
            holdingObject.destroy();
        }
        holdingObject = go;
        holdingObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        holdingObject.addComponent(new NonPickable());
        Window.getScene().addGameObjectToScene(go);
    }

    public void place() {
        if (!Window.getImguiLayer().getGameViewWindow().getWantCaptureMouse()) return;
        GameObject newObj = holdingObject.copy();
        newObj.doSerialization();
        if (newObj.getComponent(StateMachine.class) != null) {
            newObj.getComponent(StateMachine.class).refreshTextures();
        }
        newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickable.class);
        Window.getScene().addGameObjectToScene(newObj);
    }

    private boolean blockInSquare(float x, float y) {
        InspectorWindow inspectorWindow = Window.getImguiLayer().getInspectorWindow();
        Vector2f start = new Vector2f(x, y);
        Vector2f end = new Vector2f(start).add(new Vector2f(Settings.GRID_WIDTH, Settings.GRID_HEIGHT));
        Vector2f startScreenf = MouseListener.worldToScreen(start);
        Vector2f endScreenf = MouseListener.worldToScreen(end);
        Vector2i startScreen = new Vector2i((int) startScreenf.x + 2, (int) startScreenf.y + 2);
        Vector2i endScreen = new Vector2i((int) endScreenf.x - 2, (int) endScreenf.y - 2);
        float[] gameObjectIds = inspectorWindow.getPickingTexture().readPixels(startScreen, endScreen);

        for (int i = 0; i < gameObjectIds.length; i++) {
            if (gameObjectIds[i] > 0) {
                GameObject pickedObj = Window.getScene().getGameObject((int) gameObjectIds[i]);
                if (pickedObj.getComponent(NonPickable.class) == null) {
                    return true;
                }
            }
        }

        return false;
    }
    //endregion
}
