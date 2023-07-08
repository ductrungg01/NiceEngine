package components.mariodemo;

import components.Component;
import components.StateMachine;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;
import system.GameObject;
import system.Prefabs;
import system.Window;

public class MarioEventHandler extends Component {
    private static MarioMoving marioMoving;
    private static StateMachine stateMachine;
    private static float timeBeforeGameOver = 3.5f;

    public static void handleEvent(MarioEvent event) {
        switch (event) {
            case FallOver -> {
                SoundController.StopSound(MarioEvent.GamePlay);
                SoundController.PlaySound(MarioEvent.MarioDie);
                marioMoving.isDead = true;
            }
            case MarioDie -> {
                stateMachine.setCurrentState("Die");
                SoundController.StopSound(MarioEvent.GamePlay);
                SoundController.PlaySound(MarioEvent.MarioDie);
            }
            case MarioJump -> SoundController.PlaySound(MarioEvent.MarioJump);
            case GetCoin -> {
                SoundController.PlaySound(MarioEvent.GetCoin);
                HUDController.coin += 1;
            }
            case JumpHitBlock -> MarioMoving.hitBlock();
            case ChangeDirection -> SoundController.PlaySound(MarioEvent.ChangeDirection);
            case EnemyGetHit -> SoundController.PlaySound(MarioEvent.EnemyGetHit);
            case MarioBounce -> MarioMoving.bounce();
            case MarioGetHit -> MarioMoving.getHit();
            case GameOver -> EventSystem.notify(null, new Event(EventType.GameEngineStopPlay));
            case LevelUp -> {
                MarioMoving.marioHP += 1;
                MarioMoving.updateForm = true;
                MarioMoving.hurtInvincibilityTimeLeft = MarioMoving.hurtInvincibilityTime;
                SoundController.PlaySound(MarioEvent.LevelUp);
            }
            case EndScene -> {
                SoundController.StopAllSound();
                SoundController.PlaySound(MarioEvent.EndScene);
            }
        }
    }

    public static void addPoint(Vector2f position, int pointValue) {
        GameObject point = Prefabs.createChildFrom("point");
        point.transform.position = new Vector2f(position);
        point.getComponent(StateMachine.class).setDefaultState(String.valueOf(pointValue));
        Window.getScene().addGameObjectToScene(point);
        HUDController.point += pointValue;
    }

    @Override
    public void start() {
        this.marioMoving = this.gameObject.getComponent(MarioMoving.class);
        this.stateMachine = this.gameObject.getComponent(StateMachine.class);
    }
}
