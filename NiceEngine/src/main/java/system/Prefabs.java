package system;

import components.*;

public class Prefabs {
    //region user custom prefabs
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        return generateSpriteObject(sprite, sizeX, sizeY, "Sprite Object Generated");
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, String gameObjectName) {
        GameObject sprite_go = Window.getScene().createGameObject(gameObjectName);
        sprite_go.transform.scale.x = sizeX;
        sprite_go.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        sprite_go.addComponent(renderer);

        return sprite_go;
    }
    //endregion
}
