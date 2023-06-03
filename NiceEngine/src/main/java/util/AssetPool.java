package util;

import components.Spritesheet;
import system.Sound;
import renderer.Shader;
import renderer.Texture;

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
        if (AssetPool.textures.containsKey(file.getPath())) {
            return AssetPool.textures.get(file.getPath());
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getPath(), texture);
            return texture;
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getPath())) {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been to ascess pool";
        }

        return AssetPool.spritesheets.getOrDefault(file.getPath(), null);
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
        if (sounds.containsKey(file.getPath())) {
            return sounds.get(file.getPath());
        } else {
            assert false : "Sound file not added '" + soundFile + "'";
        }

        return null;
    }
    //endregion

    //region Methods
    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getPath())) {
            return sounds.get(file.getPath());
        } else {
            Sound sound = new Sound(file.getPath(), loops);
            AssetPool.sounds.put(file.getPath(), sound);

            return sound;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getPath())) {
            AssetPool.spritesheets.put(file.getPath(), spritesheet);
        }
    }

    public static void removeSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.spritesheets.containsKey(file.getPath())) {
            AssetPool.spritesheets.remove(file.getPath());
        }
    }
    //endregion

}
