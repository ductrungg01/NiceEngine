package components.demo;

import components.Component;
import system.GameObject;
import system.Prefabs;
import system.Window;

import java.util.Random;

public class AutoGenObstacle extends Component {
    private transient float timeRemain = 0;

    @Override
    public void update(float dt) {
        timeRemain -= dt;
        if (timeRemain < 0){
            timeRemain = 2;
            genObstacle();
            genObstacle();
            genObstacle();
            genObstacle();
            genObstacle();
        }
    }

    void genObstacle(){
        GameObject go = Prefabs.createChildFrom("Obstacle");
        Random rnd = new Random();
        go.transform.position.x = rnd.nextFloat(1, 5);
        go.transform.position.y = rnd.nextFloat(4, 5);
        Window.getScene().addGameObjectToScene(go);
    }
}
