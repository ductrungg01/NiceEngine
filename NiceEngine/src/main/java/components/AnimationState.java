package components;

import editor.ReferenceConfig;
import editor.ReferenceType;
import editor.uihelper.ButtonColor;
import editor.uihelper.NiceImGui;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static editor.uihelper.NiceShortCall.*;

public class AnimationState implements INonAddableComponent {
    //region Fields
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop = false;
    //endregion

    //region Properties
    public void setLoop(boolean doesLoop) {
        this.doesLoop = doesLoop;
    }
    //endregion

    //region Methods
    public void refreshTextures() {
        for (Frame frame : animationFrames) {
            if (frame.sprite != null)
                frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilePath()));
        }
    }

    public void addFrame(Sprite sprite, float frameTime) {
        animationFrames.add(new Frame(sprite, frameTime));
    }

    public void update(float dt) {
        if (currentSprite < animationFrames.size()) {
            timeTracker -= dt;
            if (timeTracker <= 0) {
                if (currentSprite != animationFrames.size() - 1 || doesLoop) {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                }
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
    }

    public boolean imgui(StateMachine stateMachine) {
        ImGui.pushID("AnimationState" + this.title + this.hashCode());

        float w = ImGui.getContentRegionAvailX() * 0.97f;
        float h = (ImGui.getTextLineHeightWithSpacing() + ImGui.getStyle().getFramePaddingX()) * (4 * animationFrames.size() + 6);

        ImGui.beginChild("## AnimationState" + this.title, w, h, true);

        this.title = NiceImGui.inputText("Title: ", this.title, "AnimationState" + this.title + this.hashCode());

        this.doesLoop = NiceImGui.checkbox("Loop?", this.doesLoop);
        this.setLoop(doesLoop);

        int index = 0;

        for (int i = 0; i < animationFrames.size(); i++) {
            Frame frame = animationFrames.get(i);

            ImGui.text("Frame (" + index + ")");

            frame.sprite = (Sprite) NiceImGui.ReferenceButton("    Sprite: ",
                    new ReferenceConfig(ReferenceType.SPRITE),
                    frame.sprite,
                    "AnimationState" + this.title + "Frame" + index);

            frame.frameTime = NiceImGui.dragfloat("    Time: ",
                    frame.frameTime, 0f, 100f, 0.001f, "Frame time of" + this.title + index);

            ImGui.text("                             ");
            ImGui.sameLine();

            if (ImGui.button("Remove frame (" + index + ")")) {
                animationFrames.remove(i);
                currentSprite = 0;
                i--;
                index--;
            }

            index++;
        }

        if (NiceImGui.drawButton("Add new Frame",
                new ButtonColor(COLOR_DarkBlue, COLOR_Blue, COLOR_Blue))) {
            Frame frame = new Frame();
            animationFrames.add(frame);
        }

        boolean needToRemove = false;

        if (NiceImGui.drawButton("Change to this state (" + this.title + ")",
                new ButtonColor(COLOR_DarkGreen, COLOR_Green, COLOR_Green))) {
            stateMachine.setCurrentState(this.title);
        }

        if (NiceImGui.drawButton("Remove this state (" + this.title + ")",
                new ButtonColor(COLOR_DarkRed, COLOR_Red, COLOR_Red))) {
            needToRemove = true;
        }

        ImGui.endChild();
        ImGui.popID();

        return needToRemove;
    }

    public Sprite getCurrentSprite() {
        if (currentSprite < animationFrames.size()) {
            return animationFrames.get(currentSprite).sprite;
        }
        return defaultSprite;
    }
    //endregion
}
