package components.DemoPVZ;

import components.Component;
import editor.Debug;
import org.joml.Vector2f;
import system.GameObject;
import system.MouseListener;
import system.Prefabs;
import system.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class CardManager extends Component {
    private transient GameObject pickingPlant = null;

    @Override
    public void update(float dt) {
        if (pickingPlant != null){
            Debug.Clear();
            Debug.Log(this.pickingPlant.name);

            Vector2f mousePos = MouseListener.getWorld();
            pickingPlant.transform.position = new Vector2f(mousePos);

            if (MouseListener.mouseBeginPress(GLFW_MOUSE_BUTTON_RIGHT)){
                this.pickingPlant.destroy();
                this.pickingPlant = null;
            }
        }
    }

    public GameObject getPickingPlant(){
        return this.pickingPlant;
    }

    public void setPickingPlantIsNull(){
        this.pickingPlant = null;
    }

    public void pickupPlant(Card.Type type){
        if (this.pickingPlant != null){
            this.pickingPlant.destroy();
        }

        switch (type){
            case PeeShooter -> {
                this.pickingPlant = Prefabs.createChildFrom("PeeShooter");
            }
            case SunFlower -> {
                this.pickingPlant = Prefabs.createChildFrom("SunFlower");
            }
            case PotatoMine -> {
                this.pickingPlant = Prefabs.createChildFrom("PotatoMine");
            }
        }

        this.pickingPlant.setNoSerialize();
        Window.getScene().addGameObjectToScene(this.pickingPlant);
    }
}
