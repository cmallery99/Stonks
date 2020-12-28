package main.java.scenes;

import imgui.flag.ImGuiWindowFlags;
import main.java.components.*;
import imgui.ImGui;
import main.java.engine.GameObject;
import main.java.util.AssetPool;

public class GameSceneInitializer extends SceneInitializer {

    private GameObject gameScene;
    private boolean stonksSelected = false;
    private boolean businessSelected = false;
    private boolean profileSelected = false;

    public GameSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        gameScene = scene.createGameObject("GameScene");
        gameScene.setNoSerialize();
        scene.addGameObjectToScene(gameScene);
    }

    @Override
    public void loadResources(Scene scene) {
        for (GameObject go : scene.getGameObjects()) {
            if (go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getOrCreateTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Main Panel", ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoScrollWithMouse
                | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.button("Stonks")) {
            System.out.println("Selected Stonks");
            stonksSelected = true;
        }

        if (ImGui.button("Businesses")) {
            System.out.println("Selected Businesses");
            businessSelected = true;
        }

        if (ImGui.button("Profile")) {
            System.out.println("Selected Profile");
            profileSelected = true;
        }
        ImGui.endMenuBar();

        if (stonksSelected) {
            ImGui.setNextWindowSize(600, 400);
            ImGui.begin("Stonks Panel");
            if (ImGui.button("test button")) {
                System.out.println("Test");
            }

            ImGui.end();
        }
        if (businessSelected) {
            ImGui.begin("Business Panel");
            ImGui.setNextWindowSize(600, 400);
            if (ImGui.button("test button")) {
                System.out.println("Test");
            }

            ImGui.end();
        }
        if (profileSelected) {
            ImGui.setNextWindowSize(600, 400);
            ImGui.begin("Profile Panel");
            if (ImGui.button("test button")) {
                System.out.println("Test");
            }

            ImGui.end();
        }

        ImGui.end();

    }

}
