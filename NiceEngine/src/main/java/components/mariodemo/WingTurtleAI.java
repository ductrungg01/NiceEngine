package components.mariodemo;

import components.Component;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.RaycastInfo;
import renderer.DebugDraw;
import system.GameObject;
import system.Window;

public class WingTurtleAI extends Component {
    private transient TurtleAI turtleAI;
    private transient float jumpDelayTime;
    private transient float jumpTime;
    private float jumpTimeDebounce = 0.05f;
    private float maxJumpTime = 0.35f;
    private Vector2f maxVelocity = new Vector2f(0.25f, 3.5f);

    @Override
    public void start() {
        this.turtleAI = this.gameObject.getComponent(TurtleAI.class);
        this.turtleAI.hasWing = true;
        this.turtleAI.wingEnable = true;
        jumpDelayTime = 0;
        jumpTime = 0;
    }

    @Override
    public void update(float dt) {
        if (!this.turtleAI.wingEnable) {
            if (this.turtleAI.lostWingDebounceTime > 0) {
                this.turtleAI.lostWingDebounceTime -= dt;
            }
            return;
        }

        if (jumpTime > 0) {
            jumpTime -= dt;
            if (this.turtleAI.goingRight)
                this.turtleAI.rb.setVelocity(new Vector2f(maxVelocity.x, maxVelocity.y));
            else
                this.turtleAI.rb.setVelocity(new Vector2f(-maxVelocity.x, maxVelocity.y));
        } else if (checkOnGround()) {
            if (jumpDelayTime > 0) {
                jumpDelayTime -= dt;
                return;
            }
            jumpTime = maxJumpTime;
            jumpDelayTime = jumpTimeDebounce;
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (this.turtleAI.rb.isSensor()) {
            return;
        }
        if (collidingObject.compareTag("HighGround") && hitNormal.y > 0.75f)
            contact.setEnabled(false);

        if (!this.turtleAI.wingEnable) return;
        if (collidingObject.compareTag("Mario") && !MarioMoving.isDead && MarioMoving.hurtInvincibilityTimeLeft <= 0) {
            if (hitNormal.y > 0.5f) {
                MarioEventHandler.handleEvent(MarioEvent.EnemyGetHit);
                MarioEventHandler.handleEvent(MarioEvent.MarioBounce);
                this.turtleAI.wingEnable = false;
            } else {
                MarioMoving.getHit();
            }
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("HighGround") && hitNormal.y > 0.75f)
            contact.setEnabled(false);
    }

    boolean checkOnGround() {
        Vector2f raycastBegin = new Vector2f(gameObject.transform.position);
        raycastBegin.sub(this.gameObject.transform.scale.x / 2.0f, 0.0f);
        Vector2f raycastEnd = new Vector2f(raycastBegin).sub(0.0f, this.gameObject.transform.scale.y / 2);
        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(this.gameObject.transform.scale.x, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(this.gameObject.transform.scale.x, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
        DebugDraw.addLine2D(raycast2Begin, raycast2End, new Vector3f(1, 0, 0));

        return (info.hit && info.hitObject != null && info.hitObject.tag.toLowerCase().contains("ground")) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.tag.toLowerCase().contains("ground"));
    }
}
