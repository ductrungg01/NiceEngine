package components.scripts.test;

import components.Component;
import components.scripts.test.mario.PlayerController;
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

    private Vector4f skyColor = COLOR_NiceBlue;
    private Vector4f undergroundColor = COLOR_Black;
    //endregion

    //region Constructors
    public MarioCameraFollow(Camera camera) {
        this.camera = camera;
    }

    public MarioCameraFollow() {
        this.camera = Window.getScene().camera();
    }
    //endregion

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        if (this.camera == null) this.camera = Window.getScene().camera();

        this.player = Window.getScene().findGameObjectWith(MarioMoving.class);
        this.camera.clearColor.set(skyColor);
        this.undergroundYLevel = this.camera.position.y -
                this.camera.getProjectionSize().y - this.cameraBuffer;
    }

    /**
     * // Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {
        if (player != null && player.getComponent(MarioMoving.class) != null && !player.getComponent(MarioMoving.class).hasWon()) {
            camera.position.x = Math.max(player.transform.position.x - 2.5f, highestX);
            highestX = Math.max(highestX, camera.position.x);

            if (player.transform.position.y < -playerBuffer) {
                this.camera.position.y = undergroundYLevel;
                this.camera.clearColor.set(undergroundColor);
            } else if (player.transform.position.y >= 0.0f && this.player.transform.position.y < skyYLevel + playerBuffer) {
                this.camera.position.y = 0.0f;
                this.camera.clearColor.set(skyColor);
            } else if (player.transform.position.y > skyYLevel + playerBuffer) {
                this.camera.position.y = skyYLevel;
                this.camera.clearColor.set(skyColor);
            }
        }
    }
    //endregion
}
