package scenes;

public abstract class SceneInitializer {
    //region Methods
    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imgui();
    //endregion
}
