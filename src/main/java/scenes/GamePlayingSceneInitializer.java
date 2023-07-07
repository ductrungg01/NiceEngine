package scenes;

import util.AssetPool;

public class GamePlayingSceneInitializer extends SceneInitializer {
    //region Constructors
    public GamePlayingSceneInitializer() {

    }
    //endregion

    //region Override methods
    @Override
    public void init(Scene scene) {
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("system-assets/shaders/default.glsl");
    }

    @Override
    public void imgui() {

    }
    //endregion
}
