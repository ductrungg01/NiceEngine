package components.DemoPVZ;

import components.Component;
import editor.Debug;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.components.RigidBody2D;
import renderer.DebugDraw;
import system.GameObject;
import system.Prefabs;
import system.Window;

public class PeeShooter extends Component {

    private transient float bulletCooldownRemain = 0;
    private final transient float BULLET_COOLDOWN = 2f;
    public transient boolean isHaveEnemy = false;

    @Override
    public void update(float dt) {
        checkEnemy();

        Debug.Clear();
        Debug.Log(isHaveEnemy);

        if (isHaveEnemy){
            bulletCooldownRemain -= dt;
            if (bulletCooldownRemain < 0){
                bulletCooldownRemain = BULLET_COOLDOWN;
                generateBullet();
            }
        } else {
            bulletCooldownRemain = 0;
        }
    }

    private void checkEnemy(){
        Vector2f start = new Vector2f(this.gameObject.transform.position);
        Vector2f end = new Vector2f(start).add(3, 0);

        DebugDraw.addLine2D(start, end, new Vector3f(1, 0, 0));
        PVZRaycastCallback raycast = new PVZRaycastCallback(this.gameObject);
        Window.getPhysics().raycast(raycast, start, end);
        Debug.Clear();
    }

    private void generateBullet(){
        GameObject bullet = Prefabs.createChildFrom("PeeShooterBullet");
        bullet.setNoSerialize();

        bullet.transform.position = new Vector2f(this.gameObject.transform.position).add(3, 0);
        bullet.getComponent(RigidBody2D.class).setVelocity(new Vector2f(0, 1f));

        Window.getScene().addGameObjectToScene(bullet);
    }
}
