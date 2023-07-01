package editor;

import editor.windows.ConsoleWindow;

public class Debug {
    public static <T> void Log(T log) {
        ConsoleWindow.getInstance().debugLogs.add(log.toString());
    }

    public static void Clear() {
        ConsoleWindow.getInstance().debugLogs.clear();
        ConsoleWindow.getInstance().isRemoved = false;
    }
}
