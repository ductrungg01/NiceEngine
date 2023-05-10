package editor;

public class Debug {
    public static void Log(String s) {
        ConsoleWindow.getInstance().debugLogs.add(s);
    }

    public static <T> void Log(T t) {
        ConsoleWindow.getInstance().debugLogs.add(t.toString());
    }
}
