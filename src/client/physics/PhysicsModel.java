package client.physics;

import shared.Mobile;
import shared.Vec3f;
import client.GameObject;

import com.jogamp.opengl.math.Quaternion;

public class PhysicsModel implements Mobile {
	
	//Physics information
	private float mass;
	private float frictionDivider;
	private Vec3f vel;
	private Vec3f accel;
	private Vec3f loc;
	private Quaternion ori;
	private boolean isDynamic;
	private CollisionModel cmdl;
	private boolean onSurface;
	private CollisionListener collListener;
	private GameObject parent;
	
	public PhysicsModel(GameObject parent, final Vec3f loc, final Quaternion ori, boolean isDynamic) {
		this.loc = new Vec3f(loc);
		this.ori = new Quaternion();
		this.isDynamic = isDynamic;
		this.parent = parent;
		rotateTo(ori);
		
		vel = new Vec3f(0.f, 0.f, 0.f);
		accel = new Vec3f(0.f, 0.f, 0.f);
		
		//These values could be dynamic
		mass = 1.f;
		frictionDivider = 0.9f;
	}
	
	public PhysicsModel(GameObject parent, final Vec3f loc, final Quaternion ori) {
		this(parent, loc, ori, true);
	}
	
	public GameObject getParent() {
		return parent;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PhysicsModel) && ((((PhysicsModel)obj)).getParent().getID() == getParent().getID());
	}

	@Override
	public void moveBy(final Vec3f shift) {
		loc.add(shift);
	}

	@Override
	public void moveTo(final Vec3f pos) {
		this.loc.copy(pos);
	}

	@Override
	public Vec3f loc() {
		return loc;
	}

	@Override
	public Quaternion ori() {
		return ori;
	}

	@Override
	public void rotateTo(Quaternion q) {
		this.ori.setX(q.getX());
		this.ori.setY(q.getY());
		this.ori.setZ(q.getZ());
		this.ori.setW(q.getW());
	}

	@Override
	public void rotateBy(Quaternion q) {
		ori.mult(q);	//Multiply quaternions to get a new angle
	}

	public void applyForce(final Vec3f force) {
		if(force.x() != force.x()) {
		    System.err.println("Not-a-number error " + loc.x() + ", " + loc.y() + ", " + loc.z());
		}
		Vec3f newAccel = new Vec3f(force);
		newAccel.scale(1.f / mass);
		if(newAccel.x() != newAccel.x()) {
		    System.err.println("Not-a-number error " + loc.x() + ", " + loc.y() + ", " + loc.z());
		}
		accel.add(newAccel);
	}
	
	public void applyAcceleration(final Vec3f accel) {
		if(accel.x() != accel.x()) {
		    System.err.println("Not-a-number error " + loc.x() + ", " + loc.y() + ", " + loc.z());
		}
		this.accel.add(accel);
	}
	
	public void onUpdate(float deltaTime) {
	    Vec3f deltaLoc = new Vec3f(
			0.5f * accel.x() * deltaTime * deltaTime + vel.x() * deltaTime,
		    0.5f * accel.y() * deltaTime * deltaTime + vel.y() * deltaTime,
		    0.5f * accel.z() * deltaTime * deltaTime + vel.z() * deltaTime
	    );
	    moveBy(deltaLoc);
	    if(loc.x() != loc.x()) {
		    System.err.println("Not-a-number error " + loc.x() + ", " + loc.y() + ", " + loc.z());
	    }
	    vel = new Vec3f(
    		accel.x() * deltaTime + vel.x() * frictionDivider,
    		accel.y() * deltaTime + vel.y() * frictionDivider,
	    	accel.z() * deltaTime + vel.z() * frictionDivider
	    );
	    accel = new Vec3f(0.f, 0.f, 0.f);
	}
	
	public float getMass() { return mass; }
	public void setMass(float mass) { this.mass = mass; }
	
	public boolean isDynamic() { return isDynamic; }
	
	public CollisionModel getCollision() { return cmdl; }
	public void setCollision(CollisionModel cmdl) {
		cmdl.setPositionable(this);
		this.cmdl = cmdl;
	}
	
	public boolean onSurface() { return onSurface; }
	public void setOnSurface(boolean onSurface) { this.onSurface = onSurface; }
	
	public void setFrictionDivider(float fdiv) { this.frictionDivider = fdiv; }
	
	public void setCollisionListener(CollisionListener collListener) { this.collListener = collListener; }
	public void onCollision(PhysicsModel collMdl) {
		if(collListener != null) {
			collListener.onCollision(collMdl);
		}
	}
}
