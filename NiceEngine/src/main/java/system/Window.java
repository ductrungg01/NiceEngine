package system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import deserializers.ComponentDeserializer;
import deserializers.GameObjectDeserializer;
import deserializers.PrefabDeserializer;
import editor.windows.GameViewWindow;
import editor.windows.OpenProjectWindow;
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
import renderer.Renderer;
import renderer.*;
import scenes.EditorSceneInitializer;
import scenes.GamePlayingSceneInitializer;
import scenes.Scene;
import scenes.SceneInitializer;
import util.AssetPool;
import util.ProjectUtils;
import util.Time;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {
    private static Window window = null;
    private static Scene currentScene;
    private static boolean isWindowFocused = true;
    public long glfwWindow;
    public boolean runtimePlaying = false;
    //region Fields
    private int width, height;
    private String title;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private long audioContext;
    private long audioDevice;

    //endregion
    //region Override methods
    private Vector2f oldEditorCameraPos = new Vector2f();
    //endregion

    private Window() {
        this.width = 3840;
        this.height = 2160;
//        this.width = 700;
//        this.height = 500;
        this.title = "9 Engine";
        EventSystem.addObserver(this);
    }

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

    public static int getWidth() {
        // TODO: fix bug about select multiple gameobject
        // return 3840;
        return get().width;
    }

    private static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static int getHeight() {
        // TODO: fix bug about select multiple gameobject
        // return 2160;
        return get().height;
    }

    private static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }
    //endregion

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImguiLayer() {
        return get().imGuiLayer;
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
            GridLineDraw.beginFrame();
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
                GridLineDraw.draw();
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
                askToSave(true);
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

        String previousProject = getPreviousProject();

        if (previousProject.isEmpty()) {
            OpenProjectWindow.open(false);
        } else {
            changeCurrentProject(previousProject, false, false);
        }
        Window.changeScene(new EditorSceneInitializer());
    }

    public void changeCurrentProject(String projectName, boolean askToSaveCurrentProject, boolean needToReload) {
        if (askToSaveCurrentProject) {
            askToSave(false);
        }

        ProjectUtils.CURRENT_PROJECT = projectName;
        glfwSetWindowTitle(glfwWindow, this.title + " - " + projectName);
        if (needToReload) {
            EventSystem.notify(null, new Event(EventType.LoadLevel));
        }

        saveCurrentProjectName();
    }

    public void askToSave(boolean askFromCloseWindow) {
        String message = (askFromCloseWindow ? "Save the project data before close?"
                : "Save the data of current project (" + ProjectUtils.CURRENT_PROJECT + ") before open/create other project?");
        int jOptionPane = (askFromCloseWindow ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_OPTION);

        if (!ProjectUtils.CURRENT_PROJECT.isEmpty() && checkHaveAnyChance()) {
            int response = JOptionPane.showConfirmDialog(null, message, "SAVE", jOptionPane);
            if (response == JOptionPane.YES_OPTION) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (response == JOptionPane.CANCEL_OPTION) {
                glfwSetWindowShouldClose(glfwWindow, false);
            }
        }
    }

    private void saveCurrentProjectName() {
        try {
            FileWriter writer = new FileWriter("EngineConfig.ini");

            writer.write("PREVIOUS PROJECT:" + ProjectUtils.CURRENT_PROJECT);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkHaveAnyChance() {
        String level_path = "data\\" + ProjectUtils.CURRENT_PROJECT + "\\" + "level.txt";
        String prefab_path = "data\\" + ProjectUtils.CURRENT_PROJECT + "\\" + "prefabs.txt";
        String spritesheet_path = "data\\" + ProjectUtils.CURRENT_PROJECT + "\\" + "spritesheet.txt";

        String previewLevelTxtToSave = "";
        String previewPrefabsTxtToSave = "";
        String previewSpritesheetTxtToSave = "";

        String previousLevelTxt = "";
        String previousPrefabsTxt = "";
        String previousSpritesheetTxt = "";

        int currentMaxGoUid = GameObject.getCurrentMaxUid();

        GameObject.setCurrentMaxUid(-1);

        // region Calc preview to save

        //region get preview file of LEVEL.TXT
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        List<GameObject> objsToSerialize = new ArrayList<>();
        for (GameObject obj : currentScene.getGameObjects()) {
            if (obj.doSerialization()) {
                objsToSerialize.add(obj);
            }
        }

        previewLevelTxtToSave += gson.toJson(objsToSerialize) + "\n";
        //endregion

        //region get preview file of PREFABS.TXT
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new PrefabDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        objsToSerialize = GameObject.PrefabLists;

        previewPrefabsTxtToSave += gson.toJson(objsToSerialize) + "\n";
        //endregion

        //region get preview file of SPRITESHEET.TXT
        List<Spritesheet> spritesheets = AssetPool.getAllSpritesheets();

        for (Spritesheet s : spritesheets) {
            String path = s.getTexture().getFilePath().replace("\\", "/");
            previewSpritesheetTxtToSave += path + "|" + s.spriteWidth + "|" + s.spriteHeight + "|" +
                    s.size() + "|" + s.spacingX + "|" + s.spacingY + "\n";
        }

        //endregion

        //endregion

        GameObject.setCurrentMaxUid(-1);

        //region Load previous data

        //region Load previous spritesheet
        try {
            BufferedReader reader = new BufferedReader(new FileReader(spritesheet_path));
            String line;

            while ((line = reader.readLine()) != null) {
                previousSpritesheetTxt += line + "\n";
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion

        //region Load previous Game object
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            previousLevelTxt = new String(Files.readAllBytes(Paths.get(level_path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        previousLevelTxt += "\n";
        //endregion

        //region Load previous Prefabs
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new PrefabDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            previousPrefabsTxt = new String(Files.readAllBytes(Paths.get(prefab_path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        previousPrefabsTxt += "\n";
        //endregion

        //endregion

        GameObject.setCurrentMaxUid(currentMaxGoUid);

        boolean levelTxtChange = !(previousLevelTxt.equals(previewLevelTxtToSave));
        boolean prefabsTxtChange = !(previousPrefabsTxt.equals(previewPrefabsTxtToSave));
        boolean spritesheetTxtChange = !(previousSpritesheetTxt.equals(previewSpritesheetTxtToSave));

        return levelTxtChange || prefabsTxtChange || spritesheetTxtChange;
    }

    private String getPreviousProject() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("EngineConfig.ini"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(":");

                String title = values[0];
                String value = values[1];

                if (title.equals("PREVIOUS PROJECT")) {
                    return value;
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return "";
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

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
                this.runtimePlaying = true;
                currentScene.save(false);
                GameViewWindow.isPlaying = true;
                Window.getImguiLayer().getInspectorWindow().clearSelected();
                SceneHierarchyWindow.clearSelectedGameObject();
                oldEditorCameraPos = Window.getScene().camera().position;
                currentScene.removeAllGameObjectInScene();
                Window.changeScene(new GamePlayingSceneInitializer());
                break;
            case GameEngineStopPlay:
                this.runtimePlaying = false;
                GameViewWindow.isPlaying = false;
                Window.getImguiLayer().getInspectorWindow().clearSelected();
                SceneHierarchyWindow.clearSelectedGameObject();
                Window.changeScene(new EditorSceneInitializer());
                Window.getScene().camera().position = oldEditorCameraPos;
                break;
            case SaveLevel:
                if (!this.runtimePlaying)
                    currentScene.save(true);
            case LoadLevel:
                Window.changeScene(new EditorSceneInitializer());
                break;
            case UserEvent:
                break;
        }
    }
}
