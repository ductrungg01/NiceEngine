package physics2d.components;

import components.Component;
import org.joml.Vector2f;

public class CircleCollider extends Component {
    private Vector2f offset = new Vector2f();
    public Vector2f getOffset(){
        return this.offset;
    }
    public void setOffset(Vector2f newOffset) { this.offset.set(newOffset);}
    private float radius = 1.0f;
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
