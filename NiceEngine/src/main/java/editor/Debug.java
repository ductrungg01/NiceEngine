package editor;

import editor.windows.ConsoleWindow;

public class Debug {
    public static <T> void Log(T log) {
        ConsoleWindow.getInstance().debugLogs.add(log.toString());
    }
}
