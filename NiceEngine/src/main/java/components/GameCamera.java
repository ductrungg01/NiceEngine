package components;

import components.scripts.test.mario.PlayerController;
import system.Camera;
import system.GameObject;
import system.Window;
import org.joml.Vector4f;

import static editor.uihelper.NiceShortCall.COLOR_Blue;

public class GameCamera extends Component implements INonAddableComponent {
    //region Fields
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float undergroundYLevel = 0.0f;
    private transient float skyYLevel = 0.0f;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = 0.25f;

    private Vector4f skyColor = COLOR_Blue;
    private Vector4f undergroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    //endregion

    //region Constructors
    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    public GameCamera() {
    }
    //endregion

    //region Override methods

    /**
     * Start is called before the first frame update
     */
    @Override
    public void start() {
        this.player = Window.getScene().findGameObjectWith(PlayerController.class);
        this.gameCamera.clearColor.set(skyColor);
        this.undergroundYLevel = this.gameCamera.position.y -
                this.gameCamera.getProjectionSize().y - this.cameraBuffer;
    }

    /**
     * // Update is called once per frame
     *
     * @param dt : The interval in seconds from the last frame to the current one
     */
    @Override
    public void update(float dt) {
        if (player != null && player.getComponent(PlayerController.class) != null && !player.getComponent(PlayerController.class).hasWon()) {
            gameCamera.position.x = Math.max(player.transform.position.x - 2.5f, highestX);
            highestX = Math.max(highestX, gameCamera.position.x);

            if (player.transform.position.y < -playerBuffer) {
                this.gameCamera.position.y = undergroundYLevel;
                this.gameCamera.clearColor.set(undergroundColor);
            } else if (player.transform.position.y >= 0.0f && this.player.transform.position.y < skyYLevel + playerBuffer) {
                this.gameCamera.position.y = 0.0f;
                this.gameCamera.clearColor.set(skyColor);
            } else if (player.transform.position.y > skyYLevel + playerBuffer) {
                this.gameCamera.position.y = skyYLevel;
                this.gameCamera.clearColor.set(skyColor);
            }
        }
    }
    //endregion
}
