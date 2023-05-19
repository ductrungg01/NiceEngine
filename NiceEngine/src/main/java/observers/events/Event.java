package observers.events;

public class Event {
    //region Fields
    public EventType type;
    //endregion

    //region Constructors
    public Event(EventType type) {
        this.type = type;
    }

    public Event() {
        this.type = EventType.UserEvent;
    }
    //endregion
}
