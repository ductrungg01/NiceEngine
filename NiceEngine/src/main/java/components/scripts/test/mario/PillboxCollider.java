package components.scripts.test.mario;

import components.Component;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import system.Window;
import org.joml.Vector2f;

public class PillboxCollider extends Component {
    //region Fields
    private transient CircleCollider topCircle = new CircleCollider();
    private transient CircleCollider bottomCircle = new CircleCollider();
    private transient Box2DCollider box = new Box2DCollider();
    private transient boolean resetFixtureNextFrame = false;

    public float width = 0.1f;
    public float height = 0.2f;
    public Vector2f offset = new Vector2f();
    //endregion

    public PillboxCollider() {
    }

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {

    }

    @Override
    public void editorUpdate(float dt) {
        if (this.topCircle.gameObject == null || this.bottomCircle.gameObject == null || this.box.gameObject == null) {
            this.topCircle.gameObject = this.gameObject;
            this.bottomCircle.gameObject = this.gameObject;
            this.box.gameObject = this.gameObject;
            recalculateColliders();
        }

        topCircle.editorUpdate(dt);
        bottomCircle.editorUpdate(dt);
        box.editorUpdate(dt);

        if (resetFixtureNextFrame) {
            resetFixture();
        }
    }

    /**
     * // Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {
        if (resetFixtureNextFrame) {
            resetFixture();
        }
    }
    //endregion

    //region Methods
    public void setWidth(float newValue) {
        this.width = newValue;
        recalculateColliders();
        resetFixture();
    }

    public void setHeight(float newValue) {
        this.height = newValue;
        recalculateColliders();
        resetFixture();
    }

    public void resetFixture() {
        if (Window.getPhysics().isLocked()) {
            resetFixtureNextFrame = true;
            return;
        }

        resetFixtureNextFrame = false;

        if (gameObject != null) {
            RigidBody2D rb = gameObject.getComponent(RigidBody2D.class);
            if (rb != null) {
                Window.getPhysics().resetPillboxCollider(rb, this);
            }
        }
    }

    public void recalculateColliders() {
        float circleRadius = width / 4.0f;
        float boxHeight = height - 2 * circleRadius;
        topCircle.setRadius(circleRadius);
        bottomCircle.setRadius(circleRadius);
        topCircle.setOffset(new Vector2f(offset).add(0, boxHeight / 4.0f));
        bottomCircle.setOffset(new Vector2f(offset).sub(0, boxHeight / 4.0f));
        box.setHalfSize(new Vector2f(width / 2.0f, boxHeight / 2.0f));
        box.setOffset(offset);
    }
    //endregion

    //region Properties
    public CircleCollider getTopCircle() {
        return topCircle;
    }

    public CircleCollider getBottomCircle() {
        return bottomCircle;
    }

    public Box2DCollider getBox() {
        return box;
    }
    //endregion

}
