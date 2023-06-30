package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

public class FireFlowerAI extends Component {
    transient boolean canFire;
    transient GameObject player;
    private transient FlowerAI flowerAI;
    private transient float readyToFireTimeLeft;
    private transient float readyToFireTime = 0.5f;

    @Override
    public void start() {
        this.canFire = false;
        flowerAI = this.gameObject.getComponent(FlowerAI.class);
        player = Window.getScene().findGameObjectWith(MarioMoving.class);
    }

    @Override
    public void update(float dt) {
        float posX = this.gameObject.transform.position.x + (this.gameObject.transform.scale.x / 2);
        float posY = this.gameObject.transform.position.y + (this.gameObject.transform.scale.y / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player.isDead()) return;
        if (player != null) {
            float posXPlayer = player.transform.position.x + (player.transform.scale.x / 2);
            float posYPlayer = player.transform.position.y + (player.transform.scale.y / 2);
            this.gameObject.transform.scale.x = posX < posXPlayer ? -0.25f : 0.25f;
            if (canFire) {
                if (posY > posYPlayer)
                    this.gameObject.getComponent(StateMachine.class).setCurrentState("FaceDown");
                else this.gameObject.getComponent(StateMachine.class).setCurrentState("FaceUp");
            } else {
                if (posY > posYPlayer)
                    this.gameObject.getComponent(StateMachine.class).setCurrentState("FireDown");
                else this.gameObject.getComponent(StateMachine.class).setCurrentState("FireUp");
            }
            if (this.flowerAI.delay > 0 && this.flowerAI.diffY >= this.flowerAI.upRange) {
                if (canFire) {
                    readyToFireTimeLeft -= dt;
                    if (readyToFireTimeLeft > 0) return;
                    GameObject fireball = Prefabs.createChildFrom("flowerfire");
                    fireball.transform.position = new Vector2f(posX - this.gameObject.transform.scale.x, posY - gameObject.transform.scale.y / 2.5f);
                    EnemyFireBall enemyFireBall = fireball.getComponent(EnemyFireBall.class);
                    enemyFireBall.goingRight = posX < posXPlayer;
                    enemyFireBall.velocity.y = 0 - (posY - posYPlayer) / (Math.abs(posX - posXPlayer) / enemyFireBall.fireballSpeed);
                    Window.getScene().addGameObjectToScene(fireball);
                    canFire = false;
                }
            }
        }
        if (this.flowerAI.delay < 0) {
            canFire = true;
            readyToFireTimeLeft = readyToFireTime;
        }
    }
}
