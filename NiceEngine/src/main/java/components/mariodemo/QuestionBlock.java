package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

public class QuestionBlock extends Component {
    private transient static boolean isColliding = false;
    protected transient boolean shake = false;
    private transient boolean active = true;
    private BlockType blockType = BlockType.Coin;
    private transient boolean goUp = true;
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
                GameObject mushroom = Prefabs.createChildFrom("mushroom");
                mushroom.transform.position = new Vector2f(this.gameObject.transform.position);
                Window.getScene().addGameObjectToScene(mushroom);
                mushroomAppear = false;
            }
        }

        if (shake && blockType == BlockType.MushroomOnGround && !mushroomAppear) {
            shake = false;
            SoundController.PlaySound(MarioEvent.MushroomAppear);
            mushroomAppear = true;
            mushroomAppearTime = 0.2f;
            StateMachine stateMachine = this.gameObject.getComponent(StateMachine.class);
            stateMachine.setCurrentState("Opened");
            return;
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
                    GameObject coin = Prefabs.createChildFrom("coin");
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
        Mushroom,
        MushroomOnGround
    }
}
