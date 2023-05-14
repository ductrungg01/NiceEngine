package editor.uihelper;

public class ReferenceConfig {
    // TODO: We can add more configuration
    public boolean showGameObject = true;
    public boolean showAllFile = true;
    public boolean showJavaFile = true;
    public boolean showSoundFile = true;
    public boolean showImageFile = true;


    public ReferenceConfig(boolean showGameObject,
                           boolean showAllFile,
                           boolean showJavaFile,
                           boolean showSoundFile,
                           boolean showImageFile) {
        this.showGameObject = showGameObject;
        this.showAllFile = showAllFile;
        this.showJavaFile = showJavaFile;
        this.showSoundFile = showSoundFile;
        this.showImageFile = showImageFile;
    }

    public ReferenceConfig() {
        // show all
    }
}
