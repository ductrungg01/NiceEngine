package util;

public class Time {
    //region Fields
    public static float timeStarted =  System.nanoTime();
    //endregion

    //region Properties
    public static float getTime(){
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
    //endregion
}
