package components.mariodemo;

import components.Component;
import components.StateMachine;
import org.joml.Vector2f;
import system.Camera;
import system.GameObject;
import system.Prefabs;
import system.Window;

import java.util.ArrayList;
import java.util.List;

public class HUDController extends Component {
    public transient static int coin;
    public transient static float time;
    public transient static int point = 0;
    private transient List<GameObject> listCoinText = new ArrayList<>();
    private transient List<GameObject> listTimeText = new ArrayList<>();
    private transient List<GameObject> listPointText = new ArrayList<>();
    private transient Camera camera;
    private Vector2f startPos = new Vector2f();

    @Override
    public void start() {
        this.camera = Window.getScene().camera();
        this.coin = 0;
        this.time = 300;
        this.point = 0;
        listCoinText.clear();
        listTimeText.clear();
        listPointText.clear();
    }

    @Override
    public void update(float dt) {
        if (listCoinText.size() == 0) {
            initText();
        }

        time -= dt;
        if (time <= 0) {
            MarioMoving.marioHP = 1;
            MarioMoving.getHit();
        }

        this.gameObject.transform.position.x = this.camera.position.x + this.camera.getProjectionSize().x / 2;
        this.gameObject.transform.position.y = this.camera.position.y + this.gameObject.transform.scale.y / 2;

        renderText(new Vector2f(this.gameObject.transform.position).add(new Vector2f(0.55f, 0.055f)), new Vector2f(0.140625f, 0.125f), listCoinText, coin);
        renderText(new Vector2f(this.gameObject.transform.position).add(new Vector2f(0.55f, -0.075f)), new Vector2f(0.140625f, 0.125f), listTimeText, (int) time);
        renderText(new Vector2f(this.gameObject.transform.position).add(new Vector2f(startPos)), new Vector2f(0.140625f, 0.125f), listPointText, point);
    }

    void renderText(Vector2f startPosition, Vector2f gap, List<GameObject> list, int value) {
        if (value < 0) value = 0;
        for (int i = 0; i < list.size(); i++) {
            Vector2f gapSize = new Vector2f(gap).mul(new Vector2f(i, 0));
            list.get(i).transform.position = new Vector2f(startPosition).sub(gapSize);
            String state = String.valueOf(value % 10);
            value = value / 10;
            list.get(i).getComponent(StateMachine.class).setCurrentState(state);
        }
    }

    void initText() {
        for (int i = 0; i < 2; i++) {
            GameObject text = Prefabs.createChildFrom("text");
            listCoinText.add(text);
            Window.getScene().addGameObjectToScene(text);
        }

        for (int i = 0; i < 3; i++) {
            GameObject text = Prefabs.createChildFrom("text");
            listTimeText.add(text);
            Window.getScene().addGameObjectToScene(text);
        }

        for (int i = 0; i < 7; i++) {
            GameObject text = Prefabs.createChildFrom("text");
            listPointText.add(text);
            Window.getScene().addGameObjectToScene(text);
        }
    }
}
