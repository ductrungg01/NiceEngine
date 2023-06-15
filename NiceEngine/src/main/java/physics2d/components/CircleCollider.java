package physics2d.components;

import components.Component;
import editor.NiceImGui;
import org.joml.Vector2f;
import renderer.DebugDraw;

public class CircleCollider extends Component {
    //region Fields
    private Vector2f offset = new Vector2f();
    private float radius = 1.0f;

    //endregion

    //region Properties
    public Vector2f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2f newOffset) {
        this.offset.set(newOffset);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addCircle(center, this.radius);
    }

    @Override
    public void imgui() {
        NiceImGui.drawVec2Control("Offset", this.offset, "Offset of Circle2dCollider" + this.hashCode() + this.gameObject.hashCode());
        this.radius = NiceImGui.dragFloat("Radius", this.radius, 0, Float.MAX_VALUE, "Radius of Circle2DCollider" + this.hashCode() + this.gameObject.hashCode());
    }

    //endregion
}
