package components;

public class Frame {
    //region Fields
    public Sprite sprite;
    public float frameTime;
    //endregion

    //region Contructors
    public Frame(){}

    public Frame(Sprite sprite, float frameTime){
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
    //endregion
}
