package physics2d.components;

import components.Component;
import editor.NiceImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
import renderer.DebugDraw;

public class Box2DCollider extends Component {
    //region Fields
    private Vector2f offset = new Vector2f();

    private Vector2f halfSize = new Vector2f(0.25f);
    private Vector2f origin = new Vector2f();

    //endregion
    public Box2DCollider() {

    }

    public Box2DCollider(Box2DCollider box2DCollider) {
        this.offset = box2DCollider.offset;
        this.halfSize = box2DCollider.halfSize;
        this.origin = box2DCollider.origin;
    }

    //region Properties
    public Vector2f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2f newOffset) {
        this.offset.set(newOffset);
    }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin() {
        return this.origin;
    }
    //endregion

    //region Override methods
    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    @Override
    public void imgui() {
        NiceImGui.drawVec2Control("Offset", this.offset, "Offset of Box2D " + this.gameObject.hashCode() + this.hashCode());
        NiceImGui.drawVec2Control("Size", this.halfSize, "Size of Box2D " + this.gameObject.hashCode() + this.hashCode());
    }

    //endregion
}
