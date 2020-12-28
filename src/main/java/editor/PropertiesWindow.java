package main.java.editor;

import main.java.engine.GameObject;
import main.java.engine.MouseListener;
import imgui.ImGui;
import main.java.physics2d.components.Box2DCollider;
import main.java.physics2d.components.CircleCollider;
import main.java.physics2d.components.RigidBody2D;
import main.java.renderer.PickingTexture;
import main.java.scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(double dt, Scene currentScene) {
        debounce -= (float)dt;

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);

            GameObject pickedObject = currentScene.getGameObject(gameObjectId);
            if (pickedObject != null && !MouseListener.isDragging()) {
                activeGameObject = pickedObject;
            }
            this.debounce = 0.2f;
        }
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
    }
}
