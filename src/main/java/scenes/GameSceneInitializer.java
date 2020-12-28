package main.java.scenes;

import imgui.flag.ImGuiWindowFlags;
import main.java.components.*;
import imgui.ImGui;
import main.java.engine.GameObject;
import main.java.util.AssetPool;

public class GameSceneInitializer extends SceneInitializer {

    private GameObject gameScene;

    public GameSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        gameScene = scene.createGameObject("LevelEditor");
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
        }

        if (ImGui.button("Businesses")) {
            System.out.println("Selected Businesses");
        }

        if (ImGui.button("Profile")) {
            System.out.println("Selected Profile");
        }

        ImGui.endMenuBar();

        ImGui.end();

    }

}
