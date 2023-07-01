package components.mariodemo;

import components.Component;
import components.SpriteRenderer;
import components.StateMachine;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import physics2d.RaycastInfo;
import physics2d.components.Box2DCollider;
import physics2d.components.Capsule2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import renderer.DebugDraw;
import system.KeyListener;
import system.Window;

public class MarioMoving extends Component {
    protected transient static float jumpTime;
    protected transient static float startJumpTime;
    protected transient static float directionChangeTime;
    protected transient static float onGroundTime;
    protected transient static float dieTime;
    protected transient static float gameOverTime;
    protected transient static boolean disableJump;
    protected transient static boolean isDead;
    protected transient static int marioHP;
    protected static float jumpTimeBuffer = 0.2f;
    protected transient static float hurtInvincibilityTimeLeft = 0;
    protected transient static float hurtInvincibilityTime = 2f;
    protected transient static boolean updateForm = false;
    protected transient static boolean endScene = false;
    protected transient Capsule2DCollider capsule2DCollider;
    protected transient RigidBody2D rb;
    protected transient SpriteRenderer spr;
    protected transient StateMachine stateMachine;
    protected transient SoundController soundController = SoundController.getInstance();
    protected Vector2f maxVelocity = new Vector2f(2.5f, 4f);
    protected Vector2f acceleration = new Vector2f(5f, 0);
    protected float SLOWDOWN_FORCE = 5f;
    protected Vector2f velocity;
    protected float directionChangeDebounce = 0.2f;
    protected float velocityDebounce = 0.2f;
    protected float jumpTimeDebounce = 0.3f;
    protected float onGroundTimeDebounce = 0.05f;
    protected float maxJumpTime = jumpTimeDebounce + jumpTimeBuffer;
    protected transient boolean isOnGround = false;
    private transient float blinkTime = 0.0f;

    static void hitBlock() {
        jumpTime = 0;
        startJumpTime = 0;
        disableJump = true;
    }

    static void bounce() {
        jumpTime = jumpTimeBuffer;
        startJumpTime = jumpTimeBuffer;
        disableJump = false;
    }

    static void getHit() {
        if (!isDead && hurtInvincibilityTimeLeft <= 0) {
            if (marioHP > 1) {
                marioHP -= 1;
                updateForm = true;
                hurtInvincibilityTimeLeft = hurtInvincibilityTime;
                SoundController.PlaySound(MarioEvent.MarioGetHit);
            } else {
                isDead = true;
                dieTime = 0.6f;
                MarioEventHandler.handleEvent(MarioEvent.MarioDie);
            }
        }
    }

    void changeForm() {
        if (marioHP == 1) {
            CircleCollider topCircle = this.capsule2DCollider.getTopCircle();
            topCircle.setRadius(0.07f);
            topCircle.setOffset(new Vector2f(0, 0.04f));

            CircleCollider botCircle = this.capsule2DCollider.getBottomCircle();
            botCircle.setRadius(0.07f);
            botCircle.setOffset(new Vector2f(0, -0.04f));

            Box2DCollider box2DCollider = this.capsule2DCollider.getBox();
            box2DCollider.setOffset(new Vector2f(-0.01f, 0));
            box2DCollider.setHalfSize(new Vector2f(0.14f, 0.12f));

            this.capsule2DCollider.setTopCircle(topCircle);
            this.capsule2DCollider.setBottomCircle(botCircle);
            this.capsule2DCollider.setBox2DCollider(box2DCollider);

        } else {
            CircleCollider topCircle = this.capsule2DCollider.getTopCircle();
            topCircle.setRadius(0.07f);
            topCircle.setOffset(new Vector2f(0, 0.13f));

            CircleCollider botCircle = this.capsule2DCollider.getBottomCircle();
            botCircle.setRadius(0.07f);
            botCircle.setOffset(new Vector2f(0, -0.13f));

            Box2DCollider box2DCollider = this.capsule2DCollider.getBox();
            box2DCollider.setOffset(new Vector2f(-0.01f, -0.01f));
            box2DCollider.setHalfSize(new Vector2f(0.16f, 0.3f));

            this.capsule2DCollider.setTopCircle(topCircle);
            this.capsule2DCollider.setBottomCircle(botCircle);
            this.capsule2DCollider.setBox2DCollider(box2DCollider);
        }
    }

    @Override
    public void start() {
        capsule2DCollider = this.gameObject.getComponent(Capsule2DCollider.class);
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.spr = this.gameObject.getComponent(SpriteRenderer.class);
        this.stateMachine = this.gameObject.getComponent(StateMachine.class);
        this.velocity = this.rb.getVelocity();
        this.dieTime = 0.6f;
        this.gameOverTime = 3f;
        this.isDead = false;
        this.disableJump = false;
        this.jumpTime = 0;
        this.startJumpTime = 0;
        this.directionChangeTime = 0;
        this.onGroundTime = 0;
        this.marioHP = 1;
        this.endScene = false;

    }

    @Override
    public void update(float dt) {
        if (endScene) {
            isOnGround = checkOnGround();
            if (!isOnGround) {
                velocity = new Vector2f(0, -2f);
            } else {
                velocity = new Vector2f(0.75f, -1f);
            }
            this.rb.setVelocity(velocity);
            this.rb.setAngularVelocity(0);
            setState();
            return;
        }
        if (isDead) {
            if (dieTime > 0) {
                dieTime -= dt;
                this.rb.setVelocity(new Vector2f(0, maxVelocity.y));
                this.rb.setAngularVelocity(0);
            } else {
                if (gameOverTime > 0) {
                    gameOverTime -= dt;
                    this.rb.setVelocity(new Vector2f(0, -maxVelocity.y));
                    this.rb.setAngularVelocity(0);
                } else {
                    MarioEventHandler.handleEvent(MarioEvent.GameOver);
                }
            }
            return;
        }

        if (updateForm) {
            changeForm();
            updateForm = false;
        }

        if (this.gameObject.transform.position.y < this.gameObject.transform.scale.y) {
            MarioEventHandler.handleEvent(MarioEvent.FallOver);
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
        } else if (spr.getColor().w == 0) {
            spr.setColor(new Vector4f(1, 1, 1, 1));
        }
        //region handle velocity X
        if (!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT) && KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            if (velocity.x < 0) {
                velocity.x = 0;
                directionChangeTime = directionChangeDebounce;
                if (isOnGround) {
                    MarioEventHandler.handleEvent(MarioEvent.ChangeDirection);
                }
            } else {
                if (directionChangeTime <= 0) {
                    velocity.x += dt * acceleration.x;
                    velocity.x = Math.min(velocity.x, maxVelocity.x);
                }
            }
        }
        if (!KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT) && KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            if (velocity.x > 0) {
                velocity.x = 0;
                directionChangeTime = directionChangeDebounce;
                if (isOnGround) {
                    MarioEventHandler.handleEvent(MarioEvent.ChangeDirection);
                }
            } else {
                if (directionChangeTime <= 0) {
                    velocity.x -= dt * acceleration.x;
                    velocity.x = Math.max(velocity.x, -maxVelocity.x);
                }
            }
        }
        if (!KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT) && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT) || (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT) && KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT))) {
            if (Math.abs(velocity.x) > velocityDebounce) {
                velocity.x -= dt * (velocity.x > 0 ? SLOWDOWN_FORCE : -SLOWDOWN_FORCE);
                velocity.x = Math.max(0, Math.abs(velocity.x)) * Math.signum(velocity.x);
                velocity.x = Math.abs(velocity.x) > velocityDebounce ? velocity.x : 0;
            } else velocity.x = 0;
        }

        if (directionChangeTime > 0) {
            directionChangeTime -= dt;
        }
        //endregion

        //region handle velocity Y
        if (checkOnGround() && !isOnGround) {
            isOnGround = true;
            onGroundTime = onGroundTimeDebounce;
        } else
            isOnGround = checkOnGround();
        if (onGroundTime >= 0 && isOnGround) {
            onGroundTime -= dt;
        }

        if ((KeyListener.keyBeginPress(GLFW.GLFW_KEY_SPACE) || KeyListener.keyBeginPress(GLFW.GLFW_KEY_S)) && !disableJump && isOnGround) {
            startJumpTime = maxJumpTime;
            jumpTime = jumpTimeDebounce;
            MarioEventHandler.handleEvent(MarioEvent.MarioJump);
        }
        if (jumpTime > 0 && startJumpTime > 0) {
            jumpTime -= dt;
            startJumpTime -= dt;
            velocity.y = maxVelocity.y;
        } else {
            if (!disableJump && startJumpTime > 0) {
                jumpTime = jumpTimeBuffer;
                disableJump = true;
            } else {
                jumpTime = 0;
                if (!isOnGround) {
                    velocity.y = -maxVelocity.y;
                }
            }
        }
        if (KeyListener.isKeyRelease(GLFW.GLFW_KEY_SPACE) || KeyListener.isKeyRelease(GLFW.GLFW_KEY_S)) {
            disableJump = true;
            startJumpTime = jumpTime;
        }
        if (isOnGround && onGroundTime <= 0) {
            disableJump = false;
        }
        //endregion

        this.rb.setVelocity(velocity);
        this.rb.setAngularVelocity(0);
        setState();
    }

    void setState() {
        if (velocity.x != 0) {
            this.gameObject.transform.scale.x *= Math.signum(this.gameObject.transform.scale.x * velocity.x);
        }
        if (marioHP == 1) {
            this.gameObject.transform.scale.y = 0.25f;
            if (!isOnGround || jumpTime > 0) {
                stateMachine.setCurrentState("Jump");
                return;
            }

            if (Math.abs(velocity.x) > velocityDebounce) {
                stateMachine.setCurrentState("Run");
            } else {
                if (directionChangeTime > 0) {
                    stateMachine.setCurrentState("ChangeDirection");
                } else {
                    stateMachine.setCurrentState("Idle");
                }
            }
        } else {
            this.gameObject.transform.scale.y = 0.4375f;
            if (!isOnGround || jumpTime > 0) {
                stateMachine.setCurrentState("BigJump");
                return;
            }

            if (Math.abs(velocity.x) > velocityDebounce) {
                stateMachine.setCurrentState("BigRun");
            } else {
                if (directionChangeTime > 0) {
                    stateMachine.setCurrentState("BigChangeDirection");
                } else {
                    stateMachine.setCurrentState("BigIdle");
                }
            }
        }
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
