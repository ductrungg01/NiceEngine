package components.DemoPVZ;

import components.Component;
import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import system.GameObject;
import system.MouseListener;
import system.Prefabs;
import system.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Spot extends Component {

    private transient CardManager cardManager;
    private transient GameObject previewGo;

    @Override
    public void start() {
        cardManager = Window.getScene().findGameObjectWith(CardManager.class).getComponent(CardManager.class);
    }

    @Override
    public void update(float dt) {
        if (previewGo != null) {
            previewGo.destroy();
        }

        if (cardManager.getPickingPlant() != null){
            previewGo = Prefabs.generateSpriteObject(cardManager.getPickingPlant().getComponent(SpriteRenderer.class).getSprite(), "previewGo");
            previewGo.setNoSerialize();
            previewGo.transform.position = new Vector2f(this.gameObject.transform.position).add(0, 0.074f);
            previewGo.transform.zIndex = 5;
            Window.getScene().addGameObjectToScene(previewGo);
        } else {
            previewGo = null;
        }

        Vector2f pos = this.gameObject.transform.position;
        Vector2f size = this.gameObject.transform.scale;

        Vector2f start = new Vector2f(pos.x - size.x / 2f, pos.y - size.y / 2f);
        Vector2f end = new Vector2f(pos.x + size.x / 2f, pos.y + size.y / 2f);

        Vector2f mousePos = MouseListener.getWorld();

        if (previewGo != null) {
            Vector4f color = previewGo.getComponent(SpriteRenderer.class).getColor();
            if (mousePos.x >= start.x && mousePos.x <= end.x && mousePos.y >= start.y && mousePos.y <= end.y) {
                color = new Vector4f(1, 1, 1, 0.2f);

                if (MouseListener.mouseBeginPress(GLFW_MOUSE_BUTTON_LEFT)) {
                    placePlant();
                }
            } else {
                color = new Vector4f(0, 0, 0, 0);
            }
            previewGo.getComponent(SpriteRenderer.class).setColor(color);
        }
    }

    private void placePlant(){
        GameObject plant = cardManager.getPickingPlant();
        cardManager.setPickingPlantIsNull();

        plant.transform.position = new Vector2f(this.gameObject.transform.position).add(0, 0.074f);
    }
}
