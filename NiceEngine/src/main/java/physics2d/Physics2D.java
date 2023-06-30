package physics2d;

import editor.Debug;
import org.jbox2d.callbacks.RayCastCallback;
import physics2d.components.Capsule2DCollider;
import system.GameObject;
import system.Transform;
import system.Window;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.RigidBody2D;

import java.util.ArrayList;
import java.util.List;

public class Physics2D {
    //region Fields
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;
    //endregion

    //region Constructors
    public Physics2D() {
        world.setContactListener(new ContactListener());
    }
    //endregion

    //region Methods
    public void destroyGameObject(GameObject go) {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if (rb != null) {
            if (rb.getRawBody() != null) {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public static boolean checkOnGround(GameObject gameObject,
                                        float innerPlayerWidth,
                                        float height) {
        Vector2f raycastBegin = new Vector2f(gameObject.transform.position);
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, height);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        //DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
        //DebugDraw.addLine2D(raycast2Begin, raycast2End, new Vector3f(1, 0, 0));

        return (info.hit && info.hitObject != null && info.hitObject.compareTag("Ground")) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.compareTag("Ground"));
    }

    public void setIsSensor(RigidBody2D rb) {
        Body body = rb.getRawBody();
        if (body == null) {
            return;
        }

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = true;
            fixture = fixture.m_next;
        }
    }

    public void setNotSensor(RigidBody2D rb) {
        Body body = rb.getRawBody();
        if (body == null) {
            return;
        }

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = false;
            fixture = fixture.m_next;
        }
    }


    public void resetBox2DCollider(RigidBody2D rb, Box2DCollider box2DCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addBox2DCollider(rb, box2DCollider);
        body.resetMassData();
    }

    public void addBox2DCollider(RigidBody2D rb, Box2DCollider box2DCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();

        Vector2f halfSize = new Vector2f(box2DCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = box2DCollider.getOffset();
        Vector2f origin = new Vector2f(box2DCollider.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = box2DCollider.gameObject;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    public void resetCircleCollider(RigidBody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addCircle2DCollider(rb, circleCollider);
        body.resetMassData();
    }

    public void addCircle2DCollider(RigidBody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = circleCollider.gameObject;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    public void resetCapsule2dCollider(RigidBody2D rb, Capsule2DCollider cd){
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++){
            body.destroyFixture(body.getFixtureList());
        }

        addCapsule2dCollider(rb, cd);
        body.resetMassData();
    }

    public void addCapsule2dCollider(RigidBody2D rb, Capsule2DCollider cc) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        addBox2DCollider(rb, cc.getBox());
        addCircle2DCollider(rb, cc.getTopCircle());
        addCircle2DCollider(rb, cc.getBottomCircle());
    }

    public RaycastInfo raycast(GameObject requestingObject, Vector2f point1, Vector2f point2) {
        RaycastInfo callback = new RaycastInfo(requestingObject);
        world.raycast(callback, new Vec2(point1.x, point1.y), new Vec2(point2.x, point2.y));
        return callback;
    }

    public void raycast(RayCastCallback customRaycast, Vector2f point1, Vector2f point2){
        world.raycast(customRaycast, new Vec2(point1.x, point1.y), new Vec2(point2.x, point2.y));
    }

    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }

        return size;
    }
    //endregion

    //region Properties
    public boolean isLocked() {
        return world.isLocked();
    }

    public Vector2f getGravity() {
        return new Vector2f(world.getGravity().x, world.getGravity().y);
    }

    public void add(GameObject go) {
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if (rb != null && rb.getRawBody() == null) {
            Transform transform = go.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();
            bodyDef.gravityScale = rb.gravityScale;
            bodyDef.angularVelocity = rb.angularVelocity;
            bodyDef.userData = rb.gameObject;

            switch (rb.getBodyType()) {
                case Static -> {
                    bodyDef.type = BodyType.STATIC;
                    break;
                }
                case Dynamic -> {
                    bodyDef.type = BodyType.DYNAMIC;
                    break;
                }
                case Kinematic -> {
                    bodyDef.type = BodyType.KINEMATIC;
                    break;
                }
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            rb.setRawBody(body);
            CircleCollider circle2DCollider;
            Box2DCollider box2DCollider;
            Capsule2DCollider capsule2DCollider;

            if ((circle2DCollider = go.getComponent(CircleCollider.class)) != null) {
                addCircle2DCollider(rb, circle2DCollider);
            }

            if ((box2DCollider = go.getComponent(Box2DCollider.class)) != null) {
                addBox2DCollider(rb, box2DCollider);
            }

            if ((capsule2DCollider = go.getComponent(Capsule2DCollider.class)) != null) {
                addCapsule2dCollider(rb, capsule2DCollider);
            }
        }
    }
    //endregion
}
