package main.java.editor;

import imgui.ImGui;
import main.java.observers.EventSystem;
import main.java.observers.events.Event;
import main.java.observers.events.EventType;

public class MenuBar {

    public void imgui() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveGame));
            }

            if (ImGui.menuItem("load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadGame));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}
