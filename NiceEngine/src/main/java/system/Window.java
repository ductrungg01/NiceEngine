package system;

import editor.windows.SceneHierarchyWindow;
import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import physics2d.Physics2D;
import renderer.*;
import renderer.Renderer;
import scenes.EditorSceneInitializer;
import scenes.GamePlayingSceneInitializer;
import scenes.Scene;
import scenes.SceneInitializer;
import util.AssetPool;
import util.Time;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {
    //region Fields
    private int width, height;
    private String title;
    public long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private boolean runtimePlaying = false;
    private static Window window = null;

    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    private static boolean isWindowFocused = true;

    //endregion

    //region Constructors
    private Window() {
        this.width = 3840;
        this.height = 2160;
//        this.width = 700;
//        this.height = 500;
        this.title = "9 Engine";
        EventSystem.addObserver(this);
    }
    //endregion

    //region Methods
    public static void changeScene(SceneInitializer sceneInitializer) {
        if (currentScene != null) {
            currentScene.destroy();
        }

        getImguiLayer().getInspectorWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("system-assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("system-assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            // Render pass 1.  Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1920, 1080);


            glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            // Render pass 2. Render actual game
            DebugDraw.beginFrame();

            this.framebuffer.bind();
            Vector4f clearColor = currentScene.camera().clearColor;
            glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {

                Renderer.bindShader(defaultShader);
                if (runtimePlaying) {
                    currentScene.update(dt);
                } else {
                    currentScene.editorUpdate(dt);
                }
                DebugDraw.draw();
                currentScene.render();
            }
            this.framebuffer.unbind();

            this.imGuiLayer.update(dt, currentScene);

            glfwSetWindowFocusCallback(glfwWindow, (window, focused) -> {
                isWindowFocused = focused;
            });

            if (!isWindowFocused) {
                GLFW.glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }

            KeyListener.endframe();
            MouseListener.endFrame();

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

            Time.deltaTime = dt;

            if (glfwWindowShouldClose(glfwWindow)) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Do you want to SAVE the scene before close?",
                        "CLOSE",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    EventSystem.notify(null, new Event(EventType.SaveLevel));
                }
                if (response == JOptionPane.CANCEL_OPTION) {
                    glfwSetWindowShouldClose(glfwWindow, false);
                }
            }
        }
    }

    public void run() throws IOException {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Destroy audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init() throws IOException {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        SetWindowIcon();

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window!");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });
        glfwSetDropCallback(glfwWindow, MouseListener::mouseDropCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not supported";
        }

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // 3840, 2160
        this.framebuffer = new Framebuffer(1920, 1080);
        this.pickingTexture = new PickingTexture(1920, 1080);
        glViewport(0, 0, 1920, 1080);

        this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        Window.changeScene(new EditorSceneInitializer());

    }

    void SetWindowIcon() {
        //region Set icon
        // Load the image file
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("system-assets/images/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the image to GLFWImage format
        GLFWImage.Buffer icons = GLFWImage.create(1);
        ByteBuffer pixels = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                pixels.put((byte) ((pixel >> 16) & 0xFF));     // R
                pixels.put((byte) ((pixel >> 8) & 0xFF));      // G
                pixels.put((byte) (pixel & 0xFF));             // B
                pixels.put((byte) ((pixel >> 24) & 0xFF));     // A
            }
        }
        pixels.flip();
        icons.position(0);
        icons.width(image.getWidth());
        icons.height(image.getHeight());
        icons.pixels(pixels);

        // Set the window icon
        glfwSetWindowIcon(glfwWindow, icons);
        //endregion
    }
    //endregion

    //region Properties
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Physics2D getPhysics() {
        return currentScene.getPhysics();
    }

    public static Scene getScene() {
        return currentScene;
    }

    private static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    private static void setWidth(int newWidth) {
        get().width = newWidth;
    }


    public static int getWidth() {
        // TODO: fix bug about select multiple gameobject
        // return 3840;
        return get().width;
    }

    public static int getHeight() {
        // TODO: fix bug about select multiple gameobject
        // return 2160;
        return get().height;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImguiLayer() {
        return get().imGuiLayer;
    }
    //endregion

    //region Override methods
    private Vector2f oldEditorCameraPos = new Vector2f();

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
                this.runtimePlaying = true;
                currentScene.save(false);
                Window.getImguiLayer().getInspectorWindow().clearSelected();
                SceneHierarchyWindow.clearSelectedGameObject();
                oldEditorCameraPos = Window.getScene().camera().position;
                currentScene.removeAllGameObjectInScene();
                Window.changeScene(new GamePlayingSceneInitializer());
                break;
            case GameEngineStopPlay:
                this.runtimePlaying = false;
                Window.changeScene(new EditorSceneInitializer());
                Window.getScene().camera().position = oldEditorCameraPos;
                break;
            case SaveLevel:
                currentScene.save(true);
            case LoadLevel:
                Window.changeScene(new EditorSceneInitializer());
                break;
            case UserEvent:
                break;
        }
    }
    //endregion
}
