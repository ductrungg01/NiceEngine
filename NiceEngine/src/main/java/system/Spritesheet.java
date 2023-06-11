package system;

import components.Sprite;
import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    //region Fields
    private Texture texture;
    private List<Sprite> sprites;

    public int spriteWidth;
    public int spriteHeight;
    public int spacing;
    //endregion

    //region Constructors
    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spacing = spacing;
        int currentX = 0;
        int currentY = 0;

        for (int i = 0; i < numSprites; i++) {
            float topY = (currentY) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = (currentY + spriteHeight) / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY),
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY += spriteHeight + spacing;
            }
        }
    }
    //endregion

    //region Properties
    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public int size() {
        return this.sprites.size();
    }

    public Texture getTexture() {
        return this.texture;
    }
    //endregion
}
