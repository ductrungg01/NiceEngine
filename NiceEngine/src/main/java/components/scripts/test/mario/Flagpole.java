package components.scripts.test.mario;

import components.Component;
import system.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class Flagpole extends Component {
    //region Fields
    private boolean isTop = false;
    //endregion

    //region Constructors
    public Flagpole(boolean isTop) {
        this.isTop = isTop;
    }

    public Flagpole() {
    }
    //endregion

    //region Override methods
    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.playWinAnimation(this.gameObject);
        }
    }
    //endregion
}
