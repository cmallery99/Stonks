package main.java.observers;

import main.java.engine.GameObject;
import main.java.observers.events.Event;

public interface Observer {

    void onNotify(GameObject object, Event event);

}
