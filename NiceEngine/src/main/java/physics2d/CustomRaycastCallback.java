package physics2d;

import components.DemoPVZ.PeeShooter;
import editor.Debug;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import system.GameObject;

public class CustomRaycastCallback implements RayCastCallback {
    private GameObject requestObject;

    public CustomRaycastCallback(GameObject requestObject){
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
