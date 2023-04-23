package editor;

public class Debug {
    public static void Log(String s) {
        ConsoleWindow.getInstance().debugLogs.add(s);
    }
}
