package components.mariodemo;

import components.Component;

public class PointShow extends Component {
    private transient float showTime;
    private transient float upSpeed;

    @Override
    public void start() {
        showTime = 0.25f;
        upSpeed = 0.1f;
    }

    @Override
    public void update(float dt) {
        if (showTime > 0) {
            showTime -= dt;
            this.gameObject.transform.position.y += upSpeed;
        } else {
            this.gameObject.destroy();
        }
    }
}
