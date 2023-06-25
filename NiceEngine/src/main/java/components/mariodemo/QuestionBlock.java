package components.mariodemo;

import components.AnimationState;
import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import system.GameObject;
import system.Prefabs;
import system.Spritesheet;
import system.Window;
import util.AssetPool;

public class QuestionBlock extends Component {
    private transient static boolean isColliding = false;
    private BlockType blockType = BlockType.Coin;
    private transient boolean active = true;
    private transient boolean goUp = true;
    private transient boolean shake = false;
    private transient Vector2f startPosition;
    private transient float mushroomAppearTime = 0;
    private transient boolean mushroomAppear = false;

    @Override
    public void start() {
        startPosition = new Vector2f(this.gameObject.transform.position);
    }

    @Override
    public void update(float dt) {
        if (mushroomAppear) {
            if (mushroomAppearTime > 0) {
                mushroomAppearTime -= dt;
            } else {
                Spritesheet sprs = AssetPool.getSpritesheet("assets/images/spritesheets/item.png");
                GameObject mushroom = Prefabs.generateSpriteObject(sprs.getSprite(0), 0.25f, 0.25f, "Mushroom");
                RigidBody2D rb = new RigidBody2D();
                rb.setBodyType(BodyType.Dynamic);

                CircleCollider circleCollider = new CircleCollider();
                circleCollider.setRadius(0.115f);

                mushroom.addComponent(circleCollider);
                mushroom.addComponent(rb);
                mushroom.setNoSerialize();
                mushroom.transform.position = new Vector2f(this.gameObject.transform.position);
                mushroom.addComponent(new MushroomAI());

                Window.getScene().addGameObjectToScene(mushroom);
                mushroomAppear = false;
            }
        }

        if (!active || MarioMoving.isDead) return;
        if (shake) {
            if (goUp) {
                if (this.gameObject.transform.position.y < this.startPosition.y + 0.1f) {
                    this.gameObject.transform.position.y += 0.025f;
                } else {
                    goUp = false;
                }
            } else {
                this.gameObject.transform.position.y -= 0.025f;
                if (this.gameObject.transform.position.y <= this.startPosition.y) {
                    active = false;
                    isColliding = false;
                    StateMachine stateMachine = this.gameObject.getComponent(StateMachine.class);
                    stateMachine.setCurrentState("Opened");
                    switch (blockType) {
                        case Mushroom -> {
                            SoundController.PlaySound(MarioEvent.MushroomAppear);
                            mushroomAppear = true;
                            mushroomAppearTime = 0.2f;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (contactNormal.y < -0.8f) {
            if (active && !isColliding) {
                shake = true;
                isColliding = true;
                if (blockType == BlockType.Coin) {
                    SoundController.PlaySound(MarioEvent.GetCoin);

                    Spritesheet sprs = AssetPool.getSpritesheet("assets/images/spritesheets/coin.png");
                    GameObject coin = Prefabs.generateSpriteObject(sprs.getSprite(0), 0.15f, 0.25f, "Coin");
                    RigidBody2D rb = new RigidBody2D();
                    rb.setBodyType(BodyType.Static);


                    StateMachine stateMachine = new StateMachine();
                    AnimationState fromBlockState = new AnimationState();
                    fromBlockState.title = "FromBlock";
                    fromBlockState.addFrame(sprs.getSprite(1), 0.05f);
                    fromBlockState.addFrame(sprs.getSprite(2), 0.05f);
                    fromBlockState.setLoop(true);
                    stateMachine.addState(fromBlockState);
                    stateMachine.setDefaultState(fromBlockState.title);

                    coin.addComponent(rb);
                    coin.addComponent(stateMachine);
                    coin.setNoSerialize();
                    coin.transform.position = new Vector2f(this.gameObject.transform.position).add(0, 0.25f);
                    coin.addComponent(new BounceCoin());

                    Window.getScene().addGameObjectToScene(coin);
                }
            }
            MarioEventHandler.handleEvent(MarioEvent.JumpHitBlock);
        }
    }

    public enum BlockType {
        Coin,
        Mushroom
    }
}
