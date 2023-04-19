package scenes;

import components.*;
import components.scripts.test.mario.BreakableBrick;
import components.scripts.test.mario.Ground;
import editor.JImGui;
import org.lwjgl.glfw.GLFW;
import system.Direction;
import imgui.ImGui;
import imgui.ImVec2;
import system.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

import java.io.File;
import java.util.Collection;

public class LevelEditorSceneInitializer extends SceneInitializer {
    //region Fields
    private Spritesheet sprites;
    private GameObject levelEditorStuff;
    private boolean rename = false;
    private String selectedFolder = "";
    private String lastSelectedFolder = "";
    private int click = 0;
    //endregion

    //region Contructors
    public LevelEditorSceneInitializer() {

    }
    //endregion

    //region Override methods
    @Override
    public void init(Scene scene) {
        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("assets/images/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/birds.png",
                new Spritesheet(AssetPool.getTexture("assets/images/birds.png"),
                        532, 532, 2, 0));
        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
        AssetPool.addSpritesheet("assets/images/turtle.png",
                new Spritesheet(AssetPool.getTexture("assets/images/turtle.png"),
                        16, 24, 4, 0));
        AssetPool.addSpritesheet("assets/images/bigSpritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/bigSpritesheet.png"),
                        16, 32, 42, 0));
        AssetPool.addSpritesheet("assets/images/pipes.png",
                new Spritesheet(AssetPool.getTexture("assets/images/pipes.png"),
                        32, 32, 6, 0));
        AssetPool.addSpritesheet("assets/images/items.png",
                new Spritesheet(AssetPool.getTexture("assets/images/items.png"),
                        16, 16, 43, 0));
        AssetPool.addSpritesheet("assets/images/gizmos.png",
                new Spritesheet(AssetPool.getTexture("assets/images/gizmos.png"),
                        24, 48, 3, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");


        AssetPool.addSound("assets/sounds/main-theme-overworld.ogg", true);
        AssetPool.addSound("assets/sounds/flagpole.ogg", false);
        AssetPool.addSound("assets/sounds/break_block.ogg", false);
        AssetPool.addSound("assets/sounds/bump.ogg", false);
        AssetPool.addSound("assets/sounds/coin.ogg", false);
        AssetPool.addSound("assets/sounds/gameover.ogg", false);
        AssetPool.addSound("assets/sounds/jump-small.ogg", false);
        AssetPool.addSound("assets/sounds/mario_die.ogg", false);
        AssetPool.addSound("assets/sounds/pipe.ogg", false);
        AssetPool.addSound("assets/sounds/powerup.ogg", false);
        AssetPool.addSound("assets/sounds/powerup_appears.ogg", false);
        AssetPool.addSound("assets/sounds/stage_clear.ogg", false);
        AssetPool.addSound("assets/sounds/stomp.ogg", false);
        AssetPool.addSound("assets/sounds/kick.ogg", false);
        AssetPool.addSound("assets/sounds/invincible.ogg", false);

        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }

            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Assets");

        File folder = new File("assets");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                // System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                ImGui.pushID(i);
                Sprite spr = new Sprite();
                spr.setTexture(AssetPool.getTexture("assets/images/folder-icon.png"));
                ImGui.image(spr.getTexId(), 28, 28);
                ImGui.sameLine();

                if (ImGui.isMouseDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                    click = 2;
                } else if (ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                    if (!ImGui.isAnyItemHovered()) {
                        click = 0;
                        rename = false;
                    } else
                        click = 1;
                }


                if (!rename || !selectedFolder.equals(listOfFiles[i].getName())) {
                    if (ImGui.button(listOfFiles[i].getName())) {
                        selectedFolder = listOfFiles[i].getName();

                        if (click == 2) {
                            rename = true;
                        } else {
                            rename = false;
                        }

                    }
                }
                if (rename && selectedFolder.equals(listOfFiles[i].getName())) {
                    String[] newName = JImGui.inputTextNoLabel(listOfFiles[i].getName());
                    if (newName[0].equals("true")) {
                        System.out.println("rename " + listOfFiles[i].getName() + " to " + newName[1]);
                        File srcFile = new File("assets/" + listOfFiles[i].getName());
                        File desFile = new File("assets/" + newName[1]);
                        System.out.println("rename " + srcFile.renameTo(desFile));
                        rename = false;
                        click = 0;
                    }

                }


                if (!lastSelectedFolder.equals(selectedFolder)) {

                    if (rename) {

                    }
                    lastSelectedFolder = selectedFolder;
                }

                ImGui.popID();
                ImGui.newLine();
            }
        }
        if (!ImGui.isAnyItemHovered()) {
            if (ImGui.beginPopupContextWindow("New folder")) {
                if (ImGui.menuItem("New folder")) {
                    ImGui.beginChild(listOfFiles.length);
                    ImGui.pushID(listOfFiles.length + 1);
                    ImGui.popID();
                    int tmp = 1;
                    File theDir = new File("assets/New folder");
                    while (theDir.exists()) {
                        theDir = new File("assets/New folder (" + tmp + ")");
                        tmp++;
                    }
                    theDir.mkdirs();
                    ImGui.endChild();
                    click = 2;
                    rename = true;
                    selectedFolder = theDir.getName();
                }
                ImGui.endPopup();
            }
        } else {
            if (ImGui.isItemHovered() && ImGui.isMouseClicked(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                System.out.println("item hover item and right click");
            }
        }
        ImGui.end();

        ImGui.begin("Objects");
        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Sprite")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                int id;
                float spriteWidth;
                float spriteHeight;
                Vector2f[] texCoords;

                Spritesheet birds = AssetPool.getSpritesheet("assets/images/birds.png");
                for (int i = 0; i < birds.size(); i++) {
                    Sprite bird = birds.getSprite(i);
                    id = bird.getTexId();
                    spriteWidth = bird.getWidth() / 16.625f;
                    spriteHeight = bird.getHeight() / 16.625f;
                    texCoords = bird.getTexCoords();

                    ImGui.pushID(id);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y,
                            texCoords[1].x, texCoords[1].y)) {
                        GameObject object = Prefabs.generateSpriteObject(bird, 0.25f, 0.25f);
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();
                    ImGui.sameLine();
                }

                Spritesheet pipes = AssetPool.getSpritesheet("assets/images/pipes.png");
                for (int i = 0; i < pipes.size(); i++) {
                    Sprite sprite = pipes.getSprite(i);
                    spriteWidth = sprite.getWidth();
                    spriteHeight = sprite.getHeight();
                    id = sprite.getTexId();
                    texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);

                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y,
                            texCoords[1].x, texCoords[1].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }

                    ImGui.popID();
                    ImGui.sameLine();
                }

                for (int i = 0; i < sprites.size(); i++) {
                    if (i == 34) continue;
                    if (i >= 38 && i < 61) continue;

                    Sprite sprite = sprites.getSprite(i);
                    spriteWidth = sprite.getWidth() * 2;
                    spriteHeight = sprite.getHeight() * 2;
                    id = sprite.getTexId();
                    texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[3].x, texCoords[3].y,
                            texCoords[1].x, texCoords[1].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        RigidBody2D rb = new RigidBody2D();
                        rb.setBodyType(BodyType.Static);
                        object.addComponent(rb);
                        Box2DCollider b2d = new Box2DCollider();
                        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                        object.addComponent(b2d);
                        object.addComponent(new Ground());

                        if (i == 12) {
                            object.addComponent(new BreakableBrick());
                        }

                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Decoration")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 34; i < 61; i++) {
                    if (i >= 35 && i < 38) continue;
                    if (i >= 42 && i < 45) continue;

                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                            texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        object.addComponent(new Ground());
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Raw image")) {
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Prefabs")) {
                int uid = 0;
                Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 2;
                float spriteHeight = sprite.getHeight() * 2;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateMario();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet items = AssetPool.getSpritesheet("assets/images/items.png");
                sprite = items.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateQuestionBlock();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = playerSprites.getSprite(14);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateGoomba();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet turtle = AssetPool.getSpritesheet("assets/images/turtle.png");
                sprite = turtle.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateTurtle();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = items.getSprite(6);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateFlagtop();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = items.getSprite(33);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateFlagPole();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                Spritesheet pipes = AssetPool.getSpritesheet("assets/images/pipes.png");
                sprite = pipes.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Down);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(1);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Up);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(2);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Right);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(3);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                        texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generatePipe(Direction.Left);
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying()) {
                            sound.play();
                        } else {
                            sound.stop();
                        }
                    }

                    if (ImGui.getContentRegionAvailX() > 100) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }

            ImGui.endTabBar();
        }
        ImGui.end();
    }

    //endregion
}
