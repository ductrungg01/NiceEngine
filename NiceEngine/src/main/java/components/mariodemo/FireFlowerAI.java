package components.mariodemo;

import components.Component;
import components.StateMachine;
import system.GameObject;
import system.Window;

public class FireFlowerAI extends Component {
    transient boolean canFire = true;
    transient GameObject player;
    private transient FlowerAI flowerAI;

    @Override
    public void start() {
        flowerAI = this.gameObject.getComponent(FlowerAI.class);
        player = Window.getScene().findGameObjectWith(MarioMoving.class);
    }

    @Override
    public void update(float dt) {
        float posX = this.gameObject.transform.position.x + (this.gameObject.transform.scale.x / 2);
        float posY = this.gameObject.transform.position.y + (this.gameObject.transform.scale.y / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            float posXPlayer = player.transform.position.x + (player.transform.scale.x / 2);
            float posYPlayer = player.transform.position.y + (player.transform.scale.y / 2);
            if (posX < posXPlayer) this.gameObject.transform.scale.x = -0.25f;
            else this.gameObject.transform.scale.x = 0.25f;
            if (canFire) {
                if (posY > posYPlayer)
                    this.gameObject.getComponent(StateMachine.class).setCurrentState("FaceDown");
                else this.gameObject.getComponent(StateMachine.class).setCurrentState("FaceUp");
            } else {
                if (posY > posYPlayer)
                    this.gameObject.getComponent(StateMachine.class).setCurrentState("FireDown");
                else this.gameObject.getComponent(StateMachine.class).setCurrentState("FireUp");
            }
            if (this.flowerAI.active && this.flowerAI.delay > 0 && !this.flowerAI.isUp) {
                if (canFire) {
//                    GameObject fireball = Prefabs.generateEnemyFireball(new Vector2f(posX - (this.transform.scale.x / 2), posY ));
//                    fireball.getComponent(Fireball.class).goingRight = this.gameObject.transform.scale.x < 0;
//                    fireball.getComponent(Fireball.class).diffY = posYPlayer - posY;
//                    Window.getScene().addGameObjectToScene(fireball);
                    canFire = false;
                }
            }
        }
        if (this.flowerAI.delay < 0) canFire = true;
    }
}
