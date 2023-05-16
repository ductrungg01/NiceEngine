package components;

import util.FileUtils;

public class Frame {
    //region Fields
    public Sprite sprite = FileUtils.getDefaultSprite();
    public float frameTime = 0.0f;
    //endregion

    //region Contructors
    public Frame() {
    }

    public Frame(Sprite sprite, float frameTime) {
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
    //endregion
}
