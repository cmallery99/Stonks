package main.java.observers;

import main.java.engine.GameObject;
import main.java.observers.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(GameObject obj, Event event) {
        for (Observer observer : observers) {
            observer.onNotify(obj, event);
        }
    }
}
