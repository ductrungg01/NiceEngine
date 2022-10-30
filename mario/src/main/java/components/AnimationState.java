package components;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    private boolean doesLoop = false;

    public void addFrame(Sprite sprite, float frameTime){
        animationFrames.add(new Frame(sprite, frameTime));
    }

    public void setLoop(boolean doesLoop){
        this.doesLoop = doesLoop;
    }

    public void update(float dt){
        if (currentSprite < animationFrames.size()){
            timeTracker -= dt;
            if (timeTracker < 0) {
                if (!(currentSprite == animationFrames.size() - 1 && !doesLoop)){
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                }
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
    }

    public Sprite getCurrentSprite(){
        if (currentSprite < animationFrames.size()){
            return animationFrames.get(currentSprite).sprite;
        }
        return defaultSprite;
    }
}
