package components.scripts.test.mario;

import components.Component;
import components.INonAddableComponent;
import components.scripts.test.MarioCollision;
import system.Camera;
import system.GameObject;
import system.Window;
import org.joml.Vector4f;

import static editor.uihelper.NiceShortCall.*;

public class GameCamera extends Component implements INonAddableComponent {
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
    public GameCamera(Camera camera) {
        this.camera = camera;
    }

    public GameCamera() {
        this.camera = Window.getScene().camera();
    }
    //endregion

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        this.player = Window.getScene().findGameObjectWith(PlayerController.class);
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
        if (player != null && player.getComponent(PlayerController.class) != null && !player.getComponent(PlayerController.class).hasWon()) {
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
