package components.DemoPVZ;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import system.GameObject;

public class PVZRaycastCallback implements RayCastCallback {
    private GameObject requestObject;

    public PVZRaycastCallback(GameObject requestObject){
        this.requestObject = requestObject;
        this.requestObject.getComponent(PeeShooter.class).isHaveEnemy = false;

    }

    @Override
    public float reportFixture(Fixture fixture, Vec2 vec2, Vec2 vec21, float v) {
        GameObject go = (GameObject) fixture.m_userData;

        // Raycast collide with Zombie (Zombie is going to left-side)
        if (go.compareTag("Enemy")){
            //Debug.Log("Raycast collide with enemy");
            notice();
        }

        return 1;
    }

    private void notice(){
        this.requestObject.getComponent(PeeShooter.class).isHaveEnemy = true;
    }
}
