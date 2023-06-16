package components;

import editor.ReferenceType;
import editor.NiceImGui;
import system.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component implements INonAddableComponent {
    //region Fields
    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;
    //endregion

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    /**
     * Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void editorUpdate(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui() {
        if (NiceImGui.colorPicker4("Color picker: ", this.color)) {
            this.isDirty = true;
        }

        Sprite tmp = (Sprite) NiceImGui.ReferenceButton("Sprite",
                ReferenceType.SPRITE,
                sprite,
                "Sprite of SpriteRenderer " + this.gameObject.hashCode());

        if (tmp != sprite) {
            setSprite(tmp);
        }

        NiceImGui.showImage(this.sprite, new Vector2f(100, 100));
    }
    //endregion

    //region Properties
    public void setDirty() {
        this.isDirty = true;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }
    //endregion
}
