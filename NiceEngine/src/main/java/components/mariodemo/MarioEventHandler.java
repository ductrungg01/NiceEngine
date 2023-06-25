package components.mariodemo;

import components.Component;
import components.StateMachine;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;

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
            case GetCoin -> SoundController.PlaySound(MarioEvent.GetCoin);
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
        }
    }

    @Override
    public void start() {
        this.marioMoving = this.gameObject.getComponent(MarioMoving.class);
        this.stateMachine = this.gameObject.getComponent(StateMachine.class);
    }
}
