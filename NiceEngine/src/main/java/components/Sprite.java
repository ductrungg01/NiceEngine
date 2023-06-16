package components;

import org.joml.Vector2f;
import org.w3c.dom.Text;
import renderer.Texture;
import util.AssetPool;

public class Sprite implements INonAddableComponent {
    //region Fields
    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1, 0),
            new Vector2f(1, 1),
            new Vector2f(0, 1),
            new Vector2f(0, 0),
    };
    //endregion

    //region Constructors
    public Sprite() {
    }

    public Sprite(Texture texture) {
        setTexture(texture);
        calcWidthAndHeight();
    }

    public Sprite(String TexturePath) {
        setTexture(AssetPool.getTexture(TexturePath));
        calcWidthAndHeight();
    }
    //endregion

    //region Properties
    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public void setTexture(Texture tex) {
        this.texture = tex;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }

    public void calcWidthAndHeight() {
        float w = this.texture.getWidth();
        float h = this.texture.getHeight();
        setWidth(w);
        setHeight(h);
    }
    //endregion
}
