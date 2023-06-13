package components;

import editor.Gif;
import editor.ReferenceType;
import editor.uihelper.ButtonColor;
import editor.NiceImGui;
import imgui.ImGui;
import imgui.type.ImString;
import org.joml.Vector2f;
import util.AssetPool;
import util.FileUtils;

import javax.swing.*;
import java.io.File;
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

    private transient Gif gifPreview;
    //endregion

    //region Constructors
    public AnimationState() {
        gifPreview = new Gif(new ArrayList<>(), doesLoop, new Vector2f(200, 200));
    }
    //endregion

    //region Properties
    public void setLoop(boolean doesLoop) {
        this.doesLoop = doesLoop;
        gifPreview.isLoop = this.doesLoop;
    }
    //endregion

    //region Methods
    public void refreshTextures() {
        for (Frame frame : animationFrames) {
            if (frame.sprite != null) {
                frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilePath()));
            }
        }
    }

    public void addFrame(Sprite sprite, float frameTime) {
        Frame frame = new Frame(sprite, frameTime);
        animationFrames.add(frame);
        gifPreview.frames.add(frame);
    }

    public void removeFrame(Frame frame) {
        animationFrames.remove(frame);
        gifPreview.frames.remove(frame);
        gifPreview.restart();
    }

    public void start() {
        gifPreview = new Gif(this.animationFrames, doesLoop, new Vector2f(200, 200));
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
        ImGui.pushID("AnimationState" + this.hashCode());

        float w = ImGui.getContentRegionAvailX() * 0.97f;
        float h = (ImGui.getTextLineHeightWithSpacing() + ImGui.getStyle().getFramePaddingX()) * (4 * animationFrames.size() + 6);
        final float DEFAULT_LABEL_WIDTH = 100f;
        final float DEFAULT_VALUE_WIDTH = 310.0f;
        float[] columnWidth = {DEFAULT_LABEL_WIDTH, DEFAULT_VALUE_WIDTH};

        ImGui.beginChild("## AnimationStateConfig", w, 0, true);

        this.title = NiceImGui.inputText("Title: ", this.title, "Title of this state", columnWidth, "AnimationState change title" + this.hashCode());

        this.doesLoop = NiceImGui.checkbox("Loop?", this.doesLoop, columnWidth);
        this.setLoop(doesLoop);

        gifPreview.show();

        int index = 0;

        for (int i = 0; i < animationFrames.size(); i++) {
            Frame frame = animationFrames.get(i);
            ImGui.text("Frame (" + index + ")");

            Vector2f oldCursorPos = new Vector2f(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());

            frame.sprite = (Sprite) NiceImGui.ReferenceButton("    Sprite: ", ReferenceType.SPRITE, frame.sprite, columnWidth, "AnimationState" + this.title + "Frame" + index);
            frame.frameTime = NiceImGui.dragFloat("    Time(s): ", frame.frameTime, 0f, Float.MAX_VALUE, 0.001f, columnWidth, "Frame time of" + this.title + index);

            ImGui.text("                  ");
            ImGui.sameLine();
            if (ImGui.button("Remove frame (" + index + ")")) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Remove Frame (" + index + ") from state '" + this.title + "'?",
                        "REMOVE FRAME",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    this.removeFrame(animationFrames.get(i));
                    currentSprite = 0;
                    i--;
                    index--;
                }
            }

            //region Sprite preview
            Vector2f newCursorPos = new Vector2f(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());
            ImGui.setCursorScreenPos(oldCursorPos.x + columnWidth[0] + columnWidth[1], oldCursorPos.y);
            final Vector2f DEFAULT_SIZE_IMAGE = new Vector2f(180, 60);
            NiceImGui.showImage(frame.sprite, DEFAULT_SIZE_IMAGE, true, "", true, new Vector2f(150, 150));
            ImGui.setCursorScreenPos(newCursorPos.x, newCursorPos.y);
            //endregion

            index++;
        }

        ImGui.separator();
        if (NiceImGui.imageButton(FileUtils.getAddIcon(), new Vector2f(50, 50), "Add new Frame")) {
            this.addFrame(FileUtils.getDefaultSprite(), 0);
        }
        ImGui.sameLine();
        boolean needToRemove = false;
        if (NiceImGui.imageButton(FileUtils.getRemoveIcon(), new Vector2f(50, 50), "Remove this AnimationState '" + this.title + "'")) {
            int response = JOptionPane.showConfirmDialog(null,
                    "Remove animation state '" + this.title + "'?",
                    "REMOVE ANIMATION STATE",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                needToRemove = true;
            }
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
