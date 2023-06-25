package physics2d.components;

import components.Component;
import imgui.ImGui;
import system.Window;

public class Capsule2DCollider extends Component {
    private CircleCollider topCircle = new CircleCollider();
    private Box2DCollider box2DCollider = new Box2DCollider();
    private CircleCollider bottomCircle = new CircleCollider();
    private transient boolean resetFixtureNextFrame = false;


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

        if (resetFixtureNextFrame){
            resetFixture();
        }
    }

    @Override
    public void update(float dt) {
        if (resetFixtureNextFrame){
            resetFixture();
        }
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

    public void setTopCircle(CircleCollider topCircle) {
        this.topCircle = topCircle;
        resetFixture();
    }

    public void setBox2DCollider(Box2DCollider box2DCollider) {
        this.box2DCollider = box2DCollider;
        resetFixture();
    }

    public void setBottomCircle(CircleCollider bottomCircle) {
        this.bottomCircle = bottomCircle;
        resetFixture();
    }

    public void resetFixture(){
        if (Window.getPhysics().isLocked()){
            resetFixtureNextFrame = true;
            return;
        }

        resetFixtureNextFrame = false;

        if (gameObject != null){
            RigidBody2D rb = gameObject.getComponent(RigidBody2D.class);
            if (rb != null){
                Window.getPhysics().resetCapsule2dCollider(rb, this);
            }
        }
    }
}
