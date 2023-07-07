package components;

import util.FileUtils;

public class Frame implements INonAddableComponent {
    //region Fields
    public Sprite sprite = FileUtils.getDefaultSprite();
    public float frameTime = 0.0f;
    //endregion

    //region Constructors
    public Frame() {
    }

    public Frame(Sprite sprite, float frameTime) {
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
    //endregion
}
