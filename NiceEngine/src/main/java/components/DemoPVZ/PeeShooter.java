package components.DemoPVZ;

import components.Component;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.CustomRaycastCallback;
import physics2d.RaycastInfo;
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
        CustomRaycastCallback raycast = new CustomRaycastCallback(this.gameObject);
        Window.getPhysics().raycast(raycast, start, end);
    }

    private void generateBullet(){
        GameObject bullet = Prefabs.createChildFrom("PeeShooterBullet");
        bullet.setNoSerialize();

        bullet.transform.position = new Vector2f(this.gameObject.transform.position);
        bullet.getComponent(RigidBody2D.class).setVelocity(new Vector2f(0, 1f));

        Window.getScene().addGameObjectToScene(bullet);
    }
}
