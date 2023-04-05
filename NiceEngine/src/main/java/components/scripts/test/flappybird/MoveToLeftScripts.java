package components.scripts.test.flappybird;

import components.Component;
import components.scripts.test.flappybird.BirdScript;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import scenes.LevelSceneInitializer;
import system.GameObject;
import system.Window;

public class MoveToLeftScripts extends Component {
    private transient float speed = 0.50f;

    @Override
    public void update(float dt){
        this.gameObject.transform.position.x -= speed * dt;

        if (this.gameObject.transform.position.x <= 0) {
            this.gameObject.transform.position.x = 10.125f;
        }
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal){
        BirdScript birdScript = obj.getComponent(BirdScript.class);

        if (birdScript != null){
            System.out.println("GAME OVER");
            Window.changeScene(new LevelSceneInitializer());
        }
    }
}
