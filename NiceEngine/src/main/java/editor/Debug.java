package editor;

public class Debug {
    public static <T> void Log(T t) {
        ConsoleWindow.getInstance().debugLogs.add(t.toString());
    }
}
