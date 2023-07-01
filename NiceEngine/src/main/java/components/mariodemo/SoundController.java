package components.mariodemo;

import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import system.GameObject;
import system.Sound;
import util.AssetPool;

public class SoundController implements Observer {
    private static String defaultFilePath = "assets/sounds/";
    private static SoundController soundController = null;

    private SoundController() {
        EventSystem.addObserver(this);
    }

    public static SoundController getInstance() {
        if (SoundController.soundController == null) {
            SoundController.soundController = new SoundController();
        }
        return SoundController.soundController;
    }

    static String GetFilePath(MarioEvent event) {
        String filePath = "";
        switch (event) {
            case GamePlay -> filePath = "smb3_background_sound.ogg";
            case MarioJump -> filePath = "jump-small.ogg";
            case MarioDie -> filePath = "smb3_player_down.ogg";
            case GetCoin -> filePath = "coin.ogg";
            case JumpHitBlock -> filePath = "bump.ogg";
            case ChangeDirection -> filePath = "kick.ogg";
            case MushroomAppear -> filePath = "vine.ogg";
            case EnemyGetHit -> filePath = "stomp.ogg";
            case MarioGetHit -> filePath = "pipe.ogg";
            case LevelUp -> filePath = "1-up.ogg";
            case KickEnemy -> filePath = "kick.ogg";
            case EndScene -> filePath = "stage_clear.ogg";
        }
        return defaultFilePath + filePath;
    }

    public static void PlaySound(MarioEvent event) {
        String filePath = GetFilePath(event);
        Sound sound = AssetPool.getSound(filePath);
        if (sound != null) {
            if (sound.isPlaying()) {
                sound.stop();
            }
            sound.play();
        }
    }

    public static void PlayLoopSound(MarioEvent event) {
        String filePath = GetFilePath(event);
        Sound sound = AssetPool.getLoopSound(filePath);
        if (sound != null) {
            if (sound.isPlaying()) {
                sound.stop();
            }
            sound.play();
        }
    }

    public static void StopSound(MarioEvent event) {
        String filePath = GetFilePath(event);
        Sound sound = AssetPool.getSound(filePath);
        if (sound != null) {
            sound.stop();
        }
    }

    public static void StopAllSound() {
        for (MarioEvent event : MarioEvent.values()) {
            StopSound(event);
        }
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
                PlayLoopSound(MarioEvent.GamePlay);
                break;
            case GameEngineStopPlay:
                StopAllSound();
                break;
        }
    }
}
