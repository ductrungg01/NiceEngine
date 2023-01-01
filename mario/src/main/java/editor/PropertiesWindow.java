package editor;

import components.*;
import imgui.ImGui;
import system.GameObject;
import org.joml.Vector4f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import renderer.PickingTexture;

import java.util.ArrayList;
import java.util.List;

public class PropertiesWindow {
    //region Fields
    List<GameObject> activeGameObjects;
    private List<Vector4f> activeGameObjectOriginalColor;
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    boolean firstTime = true;
    //endregion

    //region Contructors
    public PropertiesWindow(PickingTexture pickingTexture){
        this.activeGameObjects = new ArrayList<>();
        this.pickingTexture = pickingTexture;
        this.activeGameObjectOriginalColor = new ArrayList<>();
    }
    //endregion

    //region Methods
    public void imgui(){
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null){
            activeGameObject = activeGameObjects.get(0);

            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")){
//                Reflections reflections = new Reflections("components");
//                Set<Class<? extends Component>> classes = reflections.getSubTypesOf(Component.class);
//                for (Class<? extends Component> aClass : classes) {
//                    if (ImGui.menuItem("Add " + aClass.getName().substring(10))){
//                        if (activeGameObject.getComponent(aClass) == null){
//                            activeGameObject.addComponent(aClass.cast(Component.class));
//                        }
//                    }
//                }


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

                if (ImGui.menuItem("Add MoveToLeftScript")){
                    if (activeGameObject.getComponent(MoveToLeftScripts.class) == null) {
                        activeGameObject.addComponent(new MoveToLeftScripts());
                    }
                }

                if (ImGui.menuItem("Add JumpBySpace Script")){
                    if (activeGameObject.getComponent(JumpBySpaceScript.class) == null){
                        activeGameObject.addComponent(new JumpBySpaceScript());
                    }
                }

                if (ImGui.menuItem("Add BirdScript")){
                    if (activeGameObject.getComponent(BirdScript.class) == null){
                        activeGameObject.addComponent(new BirdScript());
                    }
                }

                if(ImGui.menuItem("Add Wood Script")){
                    if(activeGameObject.getComponent(woodScript.class) == null){
                        activeGameObject.addComponent(new woodScript());
                    }
                }

                if(ImGui.menuItem("Add wrapper Screen Script")){
                    if(activeGameObject.getComponent(wrapperScreen.class) == null){
                        activeGameObject.addComponent(new wrapperScreen());
                    }
                }

                if(ImGui.menuItem("Add Bird enemy Script")){
                    if(activeGameObject.getComponent(birdEnemyScript.class) == null){
                        activeGameObject.addComponent(new birdEnemyScript());
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
    //endregion
}
