package components.DemoPVZ;

import components.Component;
import components.SpriteRenderer;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.components.RigidBody2D;
import system.GameObject;

public class Plant extends Component {
    private transient SpriteRenderer spr;
    private transient boolean isBeingEaten = false;
    private transient float blinkTimeRemain = 0;
    private final transient float BLINK_TIME = 0.4f;
    private transient boolean isBlink = false;

    @Override
    public void start() {
        spr = this.gameObject.getComponent(SpriteRenderer.class);
    }

    @Override
    public void update(float dt) {
        if (isBeingEaten) {
            blinkTimeRemain -= dt;
            if (blinkTimeRemain < 0){
                blinkTimeRemain = BLINK_TIME;
                if (isBlink) {
                    spr.setColor(new Vector4f(1, 1, 1, 0.8f));
                } else {
                    spr.setColor(new Vector4f(1, 1, 1, 1));
                }

                isBlink = !isBlink;
            }
        } else {
            spr.setColor(new Vector4f(1, 1, 1, 1));
        }
    }

    public void setAsBeingEaten(boolean isBeingEaten){
        this.isBeingEaten = isBeingEaten;
    }
}
