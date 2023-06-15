package physics2d.components;

import components.Component;
import imgui.ImGui;

public class Capsule2DCollider extends Component {
    private CircleCollider topCircle = new CircleCollider();
    private Box2DCollider box2DCollider = new Box2DCollider();
    private CircleCollider bottomCircle = new CircleCollider();

    @Override
    public void start() {
        this.topCircle.gameObject = this.gameObject;
        this.bottomCircle.gameObject = this.gameObject;
        this.box2DCollider.gameObject = this.gameObject;
    }

    @Override
    public void editorUpdate(float dt) {
        bottomCircle.editorUpdate(dt);
        box2DCollider.editorUpdate(dt);
        topCircle.editorUpdate(dt);
    }

    @Override
    public void imgui() {
        ImGui.text("Top circle:");
        this.topCircle.imgui();

        ImGui.text("Box:");
        this.box2DCollider.imgui();

        ImGui.text("Bottom circle:");
        this.bottomCircle.imgui();
    }

    public Box2DCollider getBox() {
        return this.box2DCollider;
    }

    public CircleCollider getTopCircle() {
        return this.topCircle;
    }

    public CircleCollider getBottomCircle() {
        return this.bottomCircle;
    }
}
