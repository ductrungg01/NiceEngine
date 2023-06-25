package components.mariodemo;

import components.Component;
import org.joml.Vector2f;
import system.Camera;
import system.Window;

public class HUDController extends Component {
    private transient Camera camera;
    @Override
    public void start() {
        this.camera = Window.getScene().camera();
    }

    @Override
    public void update(float dt) {
        this.gameObject.transform.position.x = this.camera.position.x + this.camera.getProjectionSize().x / 2;
        this.gameObject.transform.position.y = this.camera.position.y + this.gameObject.transform.scale.y / 2;
    }
}
