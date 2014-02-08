package client.physics;

import shared.Positionable;
import shared.Vec3f;

public class SphereCollisionModel implements CollisionModel {

	public SphereCollisionModel(final Vec3f centerOffset, float radius) {
		this.center = new Vec3f(centerOffset);
		this.radius = radius;
		myPos = null;
	}
	
	@Override
	public CollisionTypes type() {
		return CollisionTypes.SPHERE;
	}

	@Override
	public void setPositionable(Positionable myPos) {
		this.myPos = myPos;
	}
	
	public Vec3f getCenter() {
		Vec3f result = new Vec3f(center);
		result.add(myPos.loc());
		return result;
	}
	
	public float getRadius() {
		return radius;
	}
	
	private Vec3f center;
	private float radius;
	private Positionable myPos;
}
