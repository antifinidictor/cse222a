package client.physics;

import shared.*;

public class CapsuleCollisionModel implements CollisionModel {

	public CapsuleCollisionModel(final Vec3f end1, final Vec3f end2, float radius) {
		this.end1 = new Vec3f(end1);
		this.end2 = new Vec3f(end2);
		this.radius = radius;
	}
	
	@Override
	public void setPositionable(Positionable myPos) {
		this.myPos = myPos;
	}
	
	@Override
	public CollisionTypes type() {
		return CollisionTypes.CAPSULE;
	}
	
	public Vec3f getClosestPoint(final Vec3f comp) {
		//Math thanks to http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html
		Vec3f loc = myPos.loc();
		Vec3f v1 = new Vec3f(end1);
		v1.add(loc);	//ends are relative to myPos
		v1.sub(comp);
		Vec3f vLine = new Vec3f(end2);
		vLine.sub(end1);
		float time = -1 * Vec3f.dot(v1, vLine) / vLine.squareMagnitude();
		
		//Use the 'time' to calculate the capsule's nearest point.  If t < 0.f or
		// t is > 1.f then the 'nearest point' is one of the ends.
		if(time < 0.f) {
			Vec3f result = new Vec3f(end1);
			result.add(loc);
			return result;
		} else if(time > 1.f) {
			Vec3f result = new Vec3f(end2);
			result.add(loc);
			return result;
		} else {
			return new Vec3f(
				end1.x() + (end2.x() - end1.x()) * time + loc.x(),
				end1.y() + (end2.y() - end1.y()) * time + loc.y(),
				end1.z() + (end2.z() - end1.z()) * time + loc.z()
			);
		}
	}
	
	public float getRadius() { return radius; }
	
	public Vec3f getEnd1() {
		Vec3f result = new Vec3f(end1);
		result.add(myPos.loc());
		return result;
	}
	
	public Vec3f getEnd2() {
		Vec3f result = new Vec3f(end2);
		result.add(myPos.loc());
		return result;
	}
	
	private Vec3f end1, end2;
	private float radius;
	private Positionable myPos;
}
