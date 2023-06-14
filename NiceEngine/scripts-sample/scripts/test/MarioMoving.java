package components.scripts.test;

import components.Component;
import components.SpriteRenderer;
import components.StateMachine;
import components.scripts.test.mario.Ground;
import components.scripts.test.mario.PillboxCollider;
import editor.Debug;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.Physics2D;
import physics2d.components.Box2DCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import scenes.LevelEditorSceneInitializer;
import scenes.LevelSceneInitializer;
import system.GameObject;
import system.KeyListener;
import system.Window;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class MarioMoving extends Component {
    enum MarioState {
        NORMAL,
        BIG,
        FIRE
    }

    public float walkSpeed = 2.5f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient int jumpTime = 0;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;

    private transient RigidBody2D rb;
    public transient boolean isOnGround = false;
    private transient StateMachine stateMachine;
    private transient float marioWidth = 0.25f;
    private transient int enemyBounce = 0;
    private transient MarioState marioCurrentState = MarioState.NORMAL;
    private transient boolean playWinAnimation = false;
    private transient float timeToCastle = 4.5f;
    private transient float walkTime = 2.2f;

    private transient boolean isDead = false;
    private transient float deadMaxHeight = 0;
    private transient float deadMinHeight = 0;
    private transient boolean deadGoingUp = true;
    private transient float hurtInvincibilityTimeLeft = 0;
    private transient float hurtInvincibilityTime = 1.4f;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float blinkTime = 0.0f;
    private transient SpriteRenderer spr;

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.spr = gameObject.getComponent(SpriteRenderer.class);
        this.rb.setGravityScale(0.0f);
        stateMachine = this.gameObject.getComponent(StateMachine.class);
        changeState(marioCurrentState, "Idle");
    }

    @Override
    public void update(float dt) {
        if (playWinAnimation) {
            checkOnGround();
            if (!isOnGround) {
                gameObject.transform.scale.x = -0.25f;
                gameObject.transform.position.y -= dt;
                stateMachine.trigger("stopRunning");
                stateMachine.trigger("stopJumping");
            } else {
                if (this.walkTime > 0) {
                    gameObject.transform.scale.x = 0.25f;
                    gameObject.transform.position.x += dt;
                    stateMachine.trigger("startRunning");
                }
                if (!AssetPool.getSound("assets/sounds/stage_clear.ogg").isPlaying()) {
                    AssetPool.getSound("assets/sounds/stage_clear.ogg").play();
                }
                timeToCastle -= dt;
                walkTime -= dt;

                if (timeToCastle <= 0) {
                    Window.changeScene(new LevelEditorSceneInitializer());
                }
            }

            return;
        }

        if (isDead) {
            if (this.gameObject.transform.position.y < deadMaxHeight && deadGoingUp) {
                this.gameObject.transform.position.y += dt * walkSpeed / 2.0f;
            } else if (this.gameObject.transform.position.y >= deadMaxHeight && deadGoingUp) {
                deadGoingUp = false;
            } else if (!deadGoingUp && gameObject.transform.position.y > deadMinHeight) {
                this.rb.setBodyType(BodyType.Kinematic);
                this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
                this.velocity.y += this.acceleration.y * dt;
                this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
                this.rb.setVelocity(this.velocity);
                this.rb.setAngularVelocity(0);
            } else if (!deadGoingUp && gameObject.transform.position.y <= deadMinHeight) {
                Window.changeScene(new LevelSceneInitializer());
            }
            return;
        }

        if (hurtInvincibilityTimeLeft > 0) {
            hurtInvincibilityTimeLeft -= dt;
            blinkTime -= dt;

            if (blinkTime <= 0) {
                blinkTime = 0.2f;
                if (spr.getColor().w == 1) {
                    spr.setColor(new Vector4f(1, 1, 1, 0));
                } else {
                    spr.setColor(new Vector4f(1, 1, 1, 1));
                }
            } else {
                if (spr.getColor().w == 0) {
                    spr.setColor(new Vector4f(1, 1, 1, 1));
                }
            }
        }

        checkOnGround();

        if ((KeyListener.isKeyPressed(GLFW_KEY_SPACE)) && (jumpTime > 0 || isOnGround || groundDebounce > 0)) {
            if ((isOnGround || groundDebounce > 0) && jumpTime == 0) {
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0f;
        } else if (enemyBounce > 0) {
            enemyBounce--;
            this.velocity.y = ((enemyBounce / 2.2f) * jumpBoost);
        } else if (!isOnGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        } else {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.acceleration.x = walkSpeed;
            this.gameObject.transform.scale.x = marioWidth;

            if (this.velocity.x < 0) {
                changeState(marioCurrentState, "Switch Direction");
                this.velocity.x += slowDownForce;
            } else {
                changeState(marioCurrentState, "Run");
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.acceleration.x = -walkSpeed;
            this.gameObject.transform.scale.x = -marioWidth;

            if (this.velocity.x > 0) {
                changeState(marioCurrentState, "Switch Direction");
                this.velocity.x -= slowDownForce;
            } else {
                changeState(marioCurrentState, "Run");
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
                changeState(marioCurrentState, "Idle");
            }
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);

        if (!this.isOnGround) {
            changeState(marioCurrentState, "Jump");
        }
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;
        if (collidingObject.compareTag("TopGround")) {
            float posYP = this.gameObject.transform.position.y - (this.gameObject.transform.scale.y / 2);
            float posYT = collidingObject.transform.position.y + (collidingObject.transform.scale.y / 2);
            if (contactNormal.y > 0 && posYP < posYT) {
                contact.setEnabled(false);
                return;
            }
        }
        if (collidingObject.getComponent(Ground.class) != null) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = 0;
                this.acceleration.y = 0;
                this.jumpTime = 0;
            }
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {
        if (collidingObject.compareTag("TopGround") || collidingObject.compareTag("Enemy")) {
            float posYP = this.gameObject.transform.position.y - (this.gameObject.transform.scale.y / 2);
            float posYT = collidingObject.transform.position.y + (collidingObject.transform.scale.y / 2);
            Debug.Log("PosYP: " + posYP + " | PosYT: " + posYT);
            if (hitNormal.y > 0 && posYP < posYT) {
                contact.setEnabled(false);
                return;
            }
        }
    }

    private void changeState(MarioState state, String targetState) {
        this.marioCurrentState = state;

        switch (state) {
            case NORMAL -> {

            }
            case BIG -> {
                targetState = "Big" + targetState;
            }
            case FIRE -> {
                targetState = "Fire" + targetState;
            }
        }

        this.stateMachine.setCurrentState(targetState);
    }

    public void enemyBounce() {
        this.enemyBounce = 8;
    }

    public boolean isSmall() {
        return this.marioCurrentState == MarioState.NORMAL;
    }

    public void playWinAnimation(GameObject flagpole) {
        if (!playWinAnimation) {
            playWinAnimation = true;
            velocity.set(0.0f, 0.0f);
            acceleration.set(0.0f, 0.0f);
            rb.setVelocity(velocity);
            rb.setIsSensor();
            rb.setBodyType(BodyType.Static);
            gameObject.transform.position.x = flagpole.transform.position.x;
            AssetPool.getSound("assets/sounds/flagpose.ogg");
        }
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void setPosition(Vector2f newPos) {
        this.gameObject.transform.position.set(newPos);
        this.rb.setPosition(newPos);
    }

    public void die() {
        changeState(MarioState.NORMAL, "Die");

        if (this.marioCurrentState == MarioState.NORMAL) {
            this.velocity.set(0, 0);
            this.acceleration.set(0, 0);
            this.rb.setVelocity(new Vector2f());
            this.isDead = true;
            this.rb.setIsSensor();
            AssetPool.getSound("assets/sounds/mario_die.ogg").play();
            deadMaxHeight = this.gameObject.transform.position.y + 0.3f;
            this.rb.setBodyType(BodyType.Static);
            if (gameObject.transform.position.y > 0) {
                deadMinHeight = -0.25f;
            }
        } else if (this.marioCurrentState == MarioState.BIG) {
            this.marioCurrentState = MarioState.NORMAL;
            gameObject.transform.scale.y = 0.25f;
            PillboxCollider pb = gameObject.getComponent(PillboxCollider.class);
            if (pb != null) {
                jumpBoost /= bigJumpBoostFactor;
                walkSpeed /= bigJumpBoostFactor;
                pb.setHeight(0.31f);
            }
            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
            AssetPool.getSound("assets/sounds/pipe.ogg").play();
        } else if (this.marioCurrentState == MarioState.FIRE) {
            this.marioCurrentState = MarioState.BIG;
            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
            AssetPool.getSound("assets/sounds/pipe.ogg").play();
        }
    }

    public void checkOnGround() {
        float innerPlayerWidth = marioWidth * 0.6f;
        float yVal = marioCurrentState == MarioState.NORMAL ? -0.15f : -0.24f;

        isOnGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    public boolean isInvincible() {
        return this.hurtInvincibilityTimeLeft > 0 || this.playWinAnimation;
    }

    public void powerup() {
        String currentState = this.stateMachine.getCurrentStateTitle();

        if (marioCurrentState == MarioState.NORMAL) {
            marioCurrentState = MarioState.BIG;
            AssetPool.getSound("assets/sounds/powerup.ogg").play();
            gameObject.transform.scale.y = 0.42f;
            Box2DCollider box2DCollider = this.gameObject.getComponent(Box2DCollider.class);
            if (box2DCollider != null) {
                jumpBoost *= bigJumpBoostFactor;
                walkSpeed *= bigJumpBoostFactor;
                box2DCollider.setOffset(new Vector2f(box2DCollider.getOffset()).add(new Vector2f(0, 0.125f)));
                box2DCollider.setHalfSize(new Vector2f(box2DCollider.getOffset()).add(new Vector2f(0, 0.25f)));
            }
            changeState(marioCurrentState, currentState);
        } else if (marioCurrentState == MarioState.BIG) {
            marioCurrentState = MarioState.FIRE;
            changeState(marioCurrentState, currentState);
            AssetPool.getSound("assets/sounds/powerup.ogg").play();
        }
    }

    public boolean hasWon() {
        return false;
    }

    public boolean isHurtInvincible() {
        return this.hurtInvincibilityTimeLeft > 0;
    }
}
