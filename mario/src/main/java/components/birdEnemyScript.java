package components;

import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import system.GameObject;

public class birdEnemyScript extends Component{
    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal){
        woodScript woodScript = obj.getComponent(components.woodScript.class);
        if(woodScript != null){
            System.out.println("gameover");
        }
    }
}
