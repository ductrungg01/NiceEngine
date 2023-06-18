package components.DemoChess;

import components.Component;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import system.MouseListener;

import static editor.uihelper.NiceShortCall.COLOR_Green;

public class ChangeColorWhenHover extends Component {

    private transient Vector4f originalColor;
    private transient SpriteRenderer spr;

    @Override
    public void start() {
        this.spr = this.gameObject.getComponent(SpriteRenderer.class);
        this.originalColor = this.spr.getColor();
    }

    @Override
    public void update(float dt) {

        Vector2f pos = this.gameObject.transform.position;
        Vector2f size = this.gameObject.transform.scale;

        Vector2f start = new Vector2f(pos.x - size.x / 2f, pos.y - size.y / 2f);
        Vector2f end = new Vector2f(pos.x + size.x / 2f, pos.y + size.y / 2f);

        Vector2f mousePos = MouseListener.getWorld();

        if (mousePos.x >= start.x && mousePos.x <= end.x && mousePos.y >= start.y && mousePos.y <= end.y) {
            this.spr.setColor(COLOR_Green);
        } else {
            this.spr.setColor(originalColor);
        }
    }
}
