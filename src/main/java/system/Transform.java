package system;

import components.Component;
import components.INonAddableComponent;
import editor.NiceImGui;
import org.joml.Vector2f;

public class Transform extends Component implements INonAddableComponent {
    //region Fields
    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0.0f;
    public int zIndex;
    private transient boolean constrainedProportions = false;
    //endregion

    //region Constructors
    public Transform() {
        init(new Vector2f(), new Vector2f(0.25f, 0.25f));
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f(0.25f, 0.25f));
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }
    //endregion

    //region Methods
    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.zIndex = 0;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }
    //endregion

    //region Override methods
    @Override
    public void imgui() {
        NiceImGui.drawVec2Control("Position", this.position, "Position of transform " + this.gameObject.hashCode());
        constrainedProportions = NiceImGui.drawVec2ControlWithConstrainedProportions("Scale", this.scale, "Scale of transform " + this.gameObject.hashCode(), constrainedProportions);
        this.rotation = NiceImGui.dragFloat("Rotation", this.rotation);
        this.zIndex = NiceImGui.dragInt("Z-Index", this.zIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.scale.equals(this.scale) &&
                this.rotation == t.rotation && t.zIndex == this.zIndex;
    }
    //endregion
}
