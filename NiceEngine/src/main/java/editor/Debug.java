package editor;

public class Debug {
    public static <T> void Log(T log) {
        ConsoleWindow.getInstance().debugLogs.add(log.toString());
    }
}
