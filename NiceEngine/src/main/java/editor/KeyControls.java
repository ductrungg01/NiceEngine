package editor;

import components.StateMachine;
import editor.windows.InspectorWindow;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.lwjgl.glfw.GLFW;
import system.GameObject;
import system.KeyListener;
import system.Window;
import util.Settings;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls {
    //region Fields
    private float debounceTime = 0.2f;
    private float debounce = 0;
    //endregion

    public void editorUpdate(float dt) {
        debounce -= dt;

        if ((KeyListener.isKeyPressed(org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL)) && KeyListener.keyBeginPress(GLFW.GLFW_KEY_S)) {
            EventSystem.notify(null, new Event(EventType.SaveLevel));
        }

        if (KeyListener.isKeyRelease(GLFW.GLFW_KEY_L) && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL))) {
            EventSystem.notify(null, new Event(EventType.LoadLevel));
        }

        InspectorWindow inspectorWindow = Window.getImguiLayer().getInspectorWindow();
        GameObject activeGameObject = inspectorWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = inspectorWindow.getActiveGameObjects();

        float multiplier = KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? 0.1f : 1.0f;

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D) &&
                activeGameObject != null) {
            GameObject newObj = activeGameObject.copy();
            Window.getScene().addGameObjectToScene(newObj);
            newObj.transform.position.add(Settings.GRID_WIDTH, 0.0f);
            inspectorWindow.setActiveGameObject(newObj);
            if (newObj.getComponent(StateMachine.class) != null) {
                newObj.getComponent(StateMachine.class).refreshTextures();
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D) &&
                activeGameObjects.size() > 1) {
            List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
            inspectorWindow.clearSelected();
            for (GameObject go : gameObjects) {
                GameObject copy = go.copy();
                Window.getScene().addGameObjectToScene(copy);
                inspectorWindow.addActiveGameObject(copy);
                if (copy.getComponent(StateMachine.class) != null) {
                    copy.getComponent(StateMachine.class).refreshTextures();
                }
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.keyBeginPress(GLFW_KEY_C) && activeGameObject != null) {
            inspectorWindow.setCopyGameObject(activeGameObject);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.keyBeginPress(GLFW_KEY_V) && activeGameObject != null) {
            GameObject go = inspectorWindow.getCopyGameObject();
            if (go != null) {
                Window.getScene().getMouseControls().pickupObject(go.copy());
            }
        } else if (KeyListener.keyBeginPress(GLFW_KEY_DELETE)) {
            for (GameObject go : activeGameObjects) {
                go.destroy();
            }
            inspectorWindow.clearSelected();
        } else if (KeyListener.isKeyPressed(GLFW_KEY_PAGE_DOWN) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.zIndex--;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_PAGE_UP) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.zIndex++;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_UP) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.y += Settings.GRID_HEIGHT * multiplier;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.x -= Settings.GRID_WIDTH * multiplier;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.x += Settings.GRID_WIDTH * multiplier;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.y -= Settings.GRID_HEIGHT * multiplier;
            }
        }
    }
}
