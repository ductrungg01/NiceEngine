package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.Physics2D;
import physics2d.RaycastInfo;
import physics2d.components.RigidBody2D;
import renderer.DebugDraw;
import system.Camera;
import system.GameObject;
import system.Window;

public class TurtleAI extends Component {
    //region Fields
    protected transient boolean hasWing = false;
    protected transient boolean wingEnable = false;
    protected transient RigidBody2D rb;
    protected transient boolean goingRight = false;
    protected transient float lostWingDebounceTime = 0.1f;
    private transient float walkSpeed = 1.5f;
    private transient float spinSpeed = 5f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient boolean isMoving = false;
    private transient StateMachine stateMachine;
    private transient float movingDebounceTime = 0.1f;
    private transient float movingDebounce;
    private transient boolean active = false;
    //endregion

    //region Override methods

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.2f;
        this.isDead = false;
        this.isMoving = true;
        this.movingDebounce = 0;
        this.hasWing = false;
        this.wingEnable = false;
    }

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();

        if (active && this.gameObject.transform.position.x < camera.position.x - 0.5f) {
            this.gameObject.destroy();
            return;
        }

        if (!active && this.gameObject.transform.position.x > camera.position.x - 0.25f && this.gameObject.transform.position.x < camera.position.x + camera.getProjectionSize().x + 0.5f) {
            active = true;
            return;
        }

        if (!active) {
            this.rb.setVelocity(new Vector2f(0, 0));
            return;
        }

        movingDebounce -= dt;

        if (!isDead && !hasWing && isOnEdge()) goingRight = !goingRight;

        if (isMoving) {
            if (goingRight) {
                this.gameObject.transform.scale.x = -0.25f;
            } else {
                this.gameObject.transform.scale.x = 0.25f;
            }

            velocity.x = isDead ? spinSpeed : walkSpeed;
            velocity.x *= -Math.signum(this.gameObject.transform.scale.x);
        } else {
            velocity.x = 0;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(velocity);

        if (this.gameObject.transform.position.y < -1f) {
            this.gameObject.destroy();
        }

        if (hasWing && wingEnable) {
            this.stateMachine.setCurrentState("Wing");
        } else if (!isDead) {
            this.stateMachine.setCurrentState("Default");
        } else {
            if (isMoving) {
                this.stateMachine.setCurrentState("Spin");
            } else {
                this.stateMachine.setCurrentState("Idle");
            }
        }

    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (this.rb.isSensor()) {
            return;
        }

        if (collidingObject.compareTag("Enemy")) {
            contact.setEnabled(false);
            if (isDead && isMoving) knockUpEnemy(collidingObject);
            return;
        }

        if (collidingObject.getComponent(QuestionBlock.class) != null) {
            openQuestionBlock(collidingObject);
        }

        if ((collidingObject.compareTag("Ground") || collidingObject.compareTag("Wall")) && Math.abs(contactNormal.x) > 0.8f) {
            goingRight = !goingRight;
        }

        if (!wingEnable && collidingObject.compareTag("Mario") && !MarioMoving.isDead && MarioMoving.hurtInvincibilityTimeLeft <= 0) {
            if (hasWing && lostWingDebounceTime > 0) return;
            if (!isDead) {
                if (contactNormal.y > 0.5f) {
                    MarioEventHandler.handleEvent(MarioEvent.EnemyGetHit);
                    MarioEventHandler.handleEvent(MarioEvent.MarioBounce);
                    isDead = true;
                    isMoving = false;
                    movingDebounce = movingDebounceTime;
                } else {
                    MarioMoving.getHit();
                }
            } else {
                if (movingDebounce > 0) return;
                movingDebounce = movingDebounceTime;
                if (!isMoving) {
                    MarioEventHandler.handleEvent(MarioEvent.EnemyGetHit);
                    isMoving = true;
                    goingRight = contactNormal.x < 0;
                    if (contactNormal.y > 0.5f) MarioEventHandler.handleEvent(MarioEvent.MarioBounce);
                } else {
                    if (contactNormal.y <= 0.5f) MarioMoving.getHit();
                    else {
                        isMoving = false;
                        MarioEventHandler.handleEvent(MarioEvent.EnemyGetHit);
                        MarioEventHandler.handleEvent(MarioEvent.MarioBounce);
                    }
                }
            }
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (collidingObject.compareTag("Enemy")) {
            contact.setEnabled(false);
            return;
        }
    }
    //endregion

    //region Methods
    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.2f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    boolean isOnEdge() {
        Vector2f raycastBegin = new Vector2f(gameObject.transform.position).add(-this.gameObject.transform.scale.x / 10, 0f);
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, -0.25f);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);
        DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
        return !(info.hit && info.hitObject != null && (info.hitObject.tag.toLowerCase().contains("ground") || info.hitObject.tag.toLowerCase().contains("mario") || info.hitObject.tag.toLowerCase().contains("enemy")));
    }


    void knockUpEnemy(GameObject enemy) {
        enemy.getComponent(KnockUpEffect.class).isKnockUp = true;
        enemy.getComponent(RigidBody2D.class).setIsSensor();
        enemy.transform.scale.y *= -1;
        SoundController.PlaySound(MarioEvent.KickEnemy);
    }

    void openQuestionBlock(GameObject questionBlock) {
        questionBlock.getComponent(QuestionBlock.class).shake = true;
    }
    //endregion
}
