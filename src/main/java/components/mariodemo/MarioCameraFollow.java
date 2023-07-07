package components.mariodemo;

import components.Component;
import editor.Debug;
import org.joml.Vector4f;
import system.Camera;
import system.GameObject;
import system.Window;

import static editor.uihelper.NiceShortCall.COLOR_Black;
import static editor.uihelper.NiceShortCall.COLOR_NiceBlue;

public class MarioCameraFollow extends Component {
    //region Fields
    private transient GameObject player;
    private transient Camera camera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float undergroundYLevel = 0.0f;
    private transient float skyYLevel = 0.0f;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = 0.25f;
    private transient boolean changeClearCol = false;

    private Vector4f skyColor = COLOR_NiceBlue;
    private Vector4f undergroundColor = COLOR_Black;
    private float endLevelXPosition = 35;
    private float cameraFrameHeight = 0.8f;
    //endregion

    //region Constructors
    public MarioCameraFollow(Camera camera) {
        this.camera = camera;
    }

    public MarioCameraFollow() {

    }
    //endregion

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        this.camera = Window.getScene().camera();

        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);

        this.undergroundYLevel = this.camera.position.y -
                this.camera.getProjectionSize().y - this.cameraBuffer;
    }

    @Override
    public void update(float dt) {
        if (!changeClearCol){
            this.camera.clearColor.set(skyColor);
            changeClearCol = true;
        }

        if (player != null && player.getComponent(MarioMoving.class) != null) {
            camera.position.x = player.transform.position.x - 2.5f;
            camera.position.x = Math.max(camera.position.x, -3f);
            camera.position.x = Math.min(camera.position.x, endLevelXPosition);

            camera.position.y = player.transform.position.y - (camera.getProjectionSize().y * cameraFrameHeight);
            camera.position.y = Math.max(0, camera.position.y);
        }
    }
    //endregion
}
