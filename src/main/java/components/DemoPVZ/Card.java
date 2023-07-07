package components.DemoPVZ;

import components.Component;
import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;
import system.GameObject;
import system.MouseListener;
import system.Window;

import static editor.uihelper.NiceShortCall.COLOR_Green;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Card extends Component {
    public enum Type {
        PeeShooter,
        SunFlower,
        PotatoMine
    }

    private Sprite peeShooterCardSpr;
    private Sprite sunFlowerCardSpr;
    private Sprite potatoMineCardSpr;

    public Type cardType;
    private transient GameObject cardManager;

    @Override
    public void start() {
        cardManager = Window.getScene().findGameObjectWith(CardManager.class);
        SpriteRenderer spr = this.gameObject.getComponent(SpriteRenderer.class);
        switch (cardType){
            case SunFlower -> {
                spr.setSprite(sunFlowerCardSpr);
            }
            case PeeShooter -> {
                spr.setSprite(peeShooterCardSpr);
            }
            case PotatoMine -> {
                spr.setSprite(potatoMineCardSpr);
            }
        }
    }

    @Override
    public void update(float dt) {
        if (isClick()){
            cardManager.getComponent(CardManager.class).pickupPlant(this.cardType);
        }
    }

    private boolean isClick(){
        Vector2f pos = this.gameObject.transform.position;
        Vector2f size = this.gameObject.transform.scale;

        Vector2f start = new Vector2f(pos.x - size.x / 2f, pos.y - size.y / 2f);
        Vector2f end = new Vector2f(pos.x + size.x / 2f, pos.y + size.y / 2f);

        Vector2f mousePos = MouseListener.getWorld();

        if (mousePos.x >= start.x && mousePos.x <= end.x && mousePos.y >= start.y && mousePos.y <= end.y) {
            if (MouseListener.mouseBeginPress(GLFW_MOUSE_BUTTON_LEFT)){
                return true;
            }
        }
        return false;
    }
}
