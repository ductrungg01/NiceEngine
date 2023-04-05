package observers;

import system.GameObject;
import observers.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {
    //region FIelds
    private static List<Observer> observers = new ArrayList<>();
    //endregion

    //region Methods
    public static void addObserver(Observer observer){
        observers.add(observer);
    }

    public static void notify(GameObject obj, Event event){
        for (Observer observer: observers){
            observer.onNotify(obj, event);
        }
    }
    //endregion
}
