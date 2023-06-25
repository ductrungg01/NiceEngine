package util;

import system.Spritesheet;
import system.Sound;
import renderer.Shader;
import renderer.Texture;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class AssetPool {
    //region Fields
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();
    //endregion

    //region Properties
    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        String filePath = file.getPath().replace("\\", "/");
        if (AssetPool.textures.containsKey(filePath)) {
            return AssetPool.textures.get(filePath);
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(filePath, texture);
            return texture;
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        String filePath = file.getPath().replace("\\", "/");
        if (!AssetPool.spritesheets.containsKey(filePath)) {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been to ascess pool";
        }

        return AssetPool.spritesheets.getOrDefault(filePath, null);
    }

    public static List<Spritesheet> getAllSpritesheets() {
        List<Spritesheet> sprs = new ArrayList<>();
        for (Spritesheet spr : spritesheets.values()) {
            sprs.add(spr);
        }

        return sprs;
    }


    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        String filePath = file.getPath().replace("\\", "/");
        if (!sounds.containsKey(filePath)) {
            addSound(soundFile, false);
        }

        return sounds.get(filePath);
    }

    public static Sound getLoopSound(String soundFile) {
        File file = new File(soundFile);
        String filePath = file.getPath().replace("\\", "/");
        if (!sounds.containsKey(filePath)) {
            addSound(soundFile, true);
        }

        return sounds.get(filePath);
    }
    //endregion

    //region Methods
    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        String filePath = file.getPath().replace("\\", "/");
        if (sounds.containsKey(filePath)) {
            return sounds.get(filePath);
        } else {
            Sound sound = new Sound(filePath, loops);
            AssetPool.sounds.put(filePath, sound);

            return sound;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        String filePath = file.getPath().replace("\\", "/");
        if (!AssetPool.spritesheets.containsKey(filePath)) {
            AssetPool.spritesheets.put(filePath, spritesheet);
        } else {

        }
    }

    public static void updateSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        String filePath = file.getPath().replace("\\", "/");
        if (AssetPool.spritesheets.containsKey(filePath)) {
            AssetPool.spritesheets.replace(resourceName, spritesheet);
        } else {
            JOptionPane.showMessageDialog(null, "Cannot find spritesheet '" + resourceName + "' to update!",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void removeSpritesheet(String resourceName) {
        File file = new File(resourceName);
        String filePath = file.getPath().replace("\\", "/");
        if (AssetPool.spritesheets.containsKey(filePath)) {
            AssetPool.spritesheets.remove(filePath);
        }
    }
    //endregion

}
