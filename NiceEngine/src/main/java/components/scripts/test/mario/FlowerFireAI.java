package components.scripts.test.mario;

import components.StateMachine;
import components.scripts.test.MarioMoving;
import editor.Debug;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

public class FlowerFireAI extends FlowerAI{
    transient boolean canFire = true;
    @Override
    public void update(float dt) {
        super.update(dt);

        float posX =  this.transform.position.x + (this.transform.scale.x / 2);
        float posY =  this.transform.position.y + (this.transform.scale.y / 2);
        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        if (player != null) {
            float posXPlayer = player.transform.position.x + (player.transform.scale.x / 2);
            float posYPlayer = player.transform.position.y + (player.transform.scale.y / 2);
            if (posX < posXPlayer) this.gameObject.transform.scale.x = -0.25f;
            else this.gameObject.transform.scale.x = 0.25f;
            if (canFire) {
                if (posY > posYPlayer)
                this.gameObject.getComponent(StateMachine.class).setCurrentState("Down");
                else this.gameObject.getComponent(StateMachine.class).setCurrentState("Up");
            } else {
                if (posY > posYPlayer)
                    this.gameObject.getComponent(StateMachine.class).setCurrentState("FireDown");
                else this.gameObject.getComponent(StateMachine.class).setCurrentState("FireUp");
            }
        if (active && delay > 0 && !isUp) {
                if (canFire) {
                    GameObject fireball = Prefabs.generateEnemyFireball(new Vector2f(posX - (this.transform.scale.x / 2), posY ));
                    fireball.getComponent(Fireball.class).goingRight = this.gameObject.transform.scale.x < 0;
                    fireball.getComponent(Fireball.class).diffY = posYPlayer - posY;
                    Window.getScene().addGameObjectToScene(fireball);
                    canFire = false;
                }
            }
        }
        if (delay < 0) canFire = true;
    }
}
