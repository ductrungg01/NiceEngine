package editor;

import components.NonPickable;
import components.SpriteRenderer;
import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
import org.joml.Vector4f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import renderer.PickingTexture;
import scenes.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    List<GameObject> activeGameObjects;
    private List<Vector4f> activeGameObjectOriginalColor;
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.activeGameObjects = new ArrayList<>();
        this.pickingTexture = pickingTexture;
        this.activeGameObjectOriginalColor = new ArrayList<>();
    }

    public void imgui(){
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null){
            activeGameObject = activeGameObjects.get(0);

            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")){
                if (ImGui.menuItem("Add Rigidbody")){
                    if (activeGameObject.getComponent(RigidBody2D.class) == null){
                        activeGameObject.addComponent(new RigidBody2D());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")){
                    if (activeGameObject.getComponent(Box2DCollider.class) == null &&
                            activeGameObject.getComponent(CircleCollider.class) == null){
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider")){
                    if (activeGameObject.getComponent(CircleCollider.class) == null &&
                            activeGameObject.getComponent(Box2DCollider.class) == null){
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject(){
        return activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) : null;
    }

    public void clearSelected(){
        if (activeGameObjectOriginalColor.size() > 0){
            int i = 0;
            for (GameObject go : activeGameObjects){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr != null){
                    spr.setColor(activeGameObjectOriginalColor.get(i));
                }
                i++;
            }
        }

        this.activeGameObjects.clear();
        this.activeGameObjectOriginalColor.clear();
    }

    public List<GameObject> getActiveGameObjects(){ return this.activeGameObjects;}
    public void setActiveGameObject(GameObject go){
        if (go != null){
            clearSelected();
            this.activeGameObjects.add(go);
        }
    }
    public void addActiveGameObject(GameObject go){
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null){
            this.activeGameObjectOriginalColor.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        } else {
            this.activeGameObjectOriginalColor.add(new Vector4f());
        }
        this.activeGameObjects.add(go);
    }

    public PickingTexture getPickingTexture(){
        return this.pickingTexture;
    }
}
