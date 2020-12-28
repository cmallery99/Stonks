package main.java.engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import main.java.renderer.*;
import main.java.scenes.GameSceneInitializer;
import main.java.scenes.Scene;
import main.java.scenes.SceneInitializer;
import main.java.util.AssetPool;
import main.java.util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    public float r, g, b, a;

    private static Window window;

    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Business Simulator";

        // tmp
        r = 0.9f;
        b = 0.9f;
        g = 0.9f;
        a = 1;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public static void changeScene(SceneInitializer sceneInitializer) {
        if (currentScene != null) {
            currentScene.destroy();
        }

        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public void run() {
        System.out.println("LWJGL version: " + Version.getVersion());

        init();
        loop();

        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Init GLFW errors
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        // Config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the GLFW Window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (this.glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window!");
        }

        glfwSetCursorPosCallback(this.glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(this.glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make OpenGL context current
        glfwMakeContextCurrent(this.glfwWindow);

        // This enables V-SYNC
        glfwSwapInterval(1);

        glfwShowWindow(this.glfwWindow);

        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new Framebuffer(2560, 1440);
        this.pickingTexture = new PickingTexture(2560, 1440);
        glViewport(0, 0,2560, 1440);

        this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        Window.changeScene(new GameSceneInitializer());
        System.out.println("End of Window init");
    }

    public void loop() {
        double beginTime = Time.getTime();
        double endTime = Time.getTime();
        double dt = -1L;

        Shader defaultShader = AssetPool.getOrCreateShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getOrCreateShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(this.glfwWindow)) {
            // System.out.println("FPS: " + 1.0f/dt);
            glfwPollEvents();

            // Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 2560, 1440);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            // Render game

            DebugDraw.beginFrame();

           // Uncomment to write to Framebuffer

//            this.framebuffer.bind();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

//            if (dt >= 0) {
//                DebugDraw.draw();
//                Renderer.bindShader(defaultShader);
//                if (runtimePlaying) {
//                    currentScene.update(dt);
//                }
//                else {
//                    currentScene.editorUpdate(dt);
//                }
//
//                currentScene.render();
//            }

//            this.framebuffer.unbind();

            this.imGuiLayer.update(dt, currentScene);

            glfwSwapBuffers(this.glfwWindow);
            MouseListener.endFrame();

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

    public static void setHeight(int height) {
        get().height = height;
    }

    public static Framebuffer getFrambuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImGuiLayer() {
        return get().imGuiLayer;
    }

}
