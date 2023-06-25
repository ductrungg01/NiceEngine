package system;

import components.*;
import editor.Debug;
import org.joml.Vector2f;
import util.AssetPool;
import util.Settings;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        return generateSpriteObject(sprite, sizeX, sizeY, "Sprite Object Generated");
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, String gameObjectName) {
        if (sprite.getTexId() == -1) {
            String texturePath = sprite.getTexture().getFilePath();
            sprite.setTexture(AssetPool.getTexture(texturePath));
        }

        GameObject sprite_go = Window.getScene().createGameObject(gameObjectName);
        sprite_go.transform.scale.x = sizeX;
        sprite_go.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        sprite_go.addComponent(renderer);

        return sprite_go;
    }

    public static GameObject generateSpriteObject(Sprite spr, String name) {
        float MIN_SCALE_X = Settings.GRID_WIDTH;
        float MIN_SCALE_Y = Settings.GRID_HEIGHT;

        float offset = Math.max(MIN_SCALE_X / spr.getWidth(), MIN_SCALE_Y / spr.getHeight());

        Vector2f size = new Vector2f(spr.getWidth() * offset, spr.getHeight() * offset);
        return generateSpriteObject(spr, size.x, size.y, name);
    }

    public static GameObject getPrefab(String name) {
        for (GameObject prefab : GameObject.PrefabLists) {
            if (prefab.name.equals(name)) {
                return prefab;
            }
        }

        System.out.println("Cannot find prefab '" + name + "'");
        Debug.Log("Cannot find prefab '" + name + "'");
        return null;
    }

    public static GameObject createChildFrom(String prefabName) {
        GameObject prefab = getPrefab(prefabName);

        if (prefab == null) return null;

        GameObject newGo = prefab.generateChildGameObject();

        return newGo;
    }
}
