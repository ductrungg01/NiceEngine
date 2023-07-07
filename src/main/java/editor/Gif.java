package editor;

import components.Frame;
import imgui.ImGui;
import org.joml.Vector2f;
import util.FileUtils;
import util.Time;

import java.util.ArrayList;
import java.util.List;

public class Gif {
    public List<Frame> frames = new ArrayList<>();
    public boolean isLoop = false;
    Vector2f gifSize = new Vector2f();

    private int currentFrameIndex = 0;
    private float timeTracker = 0;

    public Gif(List<Frame> frames, boolean isLoop, Vector2f gifSize) {
        this.frames.addAll(frames);
        this.isLoop = isLoop;
        this.gifSize = gifSize;

        if (frames.size() > 0) timeTracker = frames.get(0).frameTime;
    }

    public void show() {
        if (currentFrameIndex < this.frames.size()) {
            timeTracker -= Time.deltaTime;
            if (timeTracker < 0) {
                if (currentFrameIndex != this.frames.size() - 1 || this.isLoop) {
                    currentFrameIndex = (currentFrameIndex + 1) % this.frames.size();
                }
                timeTracker = this.frames.get(currentFrameIndex).frameTime;
            }
            ImGui.beginChild("Show gif " + this.hashCode(), gifSize.x + 5, gifSize.y + 5, false);
            Vector2f[] texCoords = frames.get(currentFrameIndex).sprite.getTexCoords();
            ImGui.image(frames.get(currentFrameIndex).sprite.getTexId(), gifSize.x, gifSize.y, texCoords[3].x, texCoords[3].y, texCoords[1].x, texCoords[1].y);
            ImGui.endChild();
            if (ImGui.isItemHovered()){
                ImGui.beginTooltip();
                ImGui.text("Frame [" + currentFrameIndex + "]");
                ImGui.endTooltip();
            }
            if (currentFrameIndex == this.frames.size() - 1 && !this.isLoop){
                if (NiceImGui.imageButton(FileUtils.getIcon(FileUtils.ICON_NAME.RESTART), new Vector2f(25, 25), "Replay")) {
                    currentFrameIndex = 0;
                    timeTracker = this.frames.get(currentFrameIndex).frameTime;
                }
            }
        }
    }

    public void restart() {
        if (this.frames.size() > 0) {
            currentFrameIndex = 0;
            timeTracker = this.frames.get(0).frameTime;
        }
    }
}
