package editor.windows;

import editor.MessageBox;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;
import system.MouseListener;
import system.Window;
import util.Settings;
import util.Time;

public class GameViewWindow {
    public static boolean isPlaying = false;
    private static GameViewWindow instance = null;
    //region Singleton
    private final float diffScreenX = 10.0f;
    private final float diffScreenY = -20.0f;
    public float debounceTimeToCapture = 0;

    //endregion
    //region Fields
    private float leftX, rightX, topY, bottomY;

    private GameViewWindow() {
    }

    public static GameViewWindow getInstance() {
        if (instance == null) {
            instance = new GameViewWindow();
        }

        return instance;
    }
    //endregion

    //region Methods
    public void imgui() {
        if (debounceTimeToCapture > 0)
            debounceTimeToCapture -= Time.deltaTime;

        ImGui.setNextWindowSizeConstraints(Settings.MIN_WIDTH_GROUP_WIDGET, Settings.MIN_HEIGHT_GROUP_WIDGET, Window.getWidth(), Window.getHeight());

        ImGui.begin("Game viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse |
                ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
            EventSystem.notify(null, new Event(EventType.GameEngineStartPlay));
        }
        if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
            EventSystem.notify(null, new Event(EventType.GameEngineStopPlay));
        }
        ImGui.endMenuBar();

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenterPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 widgetPos = new ImVec2();
        ImGui.getWindowPos(widgetPos);


        leftX = windowPos.x + widgetPos.x + diffScreenX;
        rightX = windowPos.x + windowSize.x + widgetPos.x + diffScreenX;
        bottomY = windowPos.y + widgetPos.y + diffScreenY;
        topY = windowPos.y + windowSize.y + widgetPos.y + diffScreenY;

        int textureId = Window.getFramebuffer().getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(windowPos.x + 10 + widgetPos.x, windowPos.y + widgetPos.y + diffScreenY));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    public boolean getWantCaptureMouse() {
        if (FileDialog.getInstance().isOpen()
            || CreateNewProjectWindow.isOpen()
            || OpenProjectWindow.isOpen()
            || AddingSpritesheetWindow.getInstance().isOpened()
            || MessageBox.showMsb) {
            return false;
        }

        if (debounceTimeToCapture > 0) return false;

        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();

        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenterPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImVec2 windowPos = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        ImGui.getWindowPos(windowPos);
        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }
    //endregion
}
