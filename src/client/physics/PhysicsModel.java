package client.physics;

import com.jogamp.opengl.math.Quaternion;

import shared.Movable;
import shared.Positionable;
import shared.Rotatable;
import shared.Vec3f;

public class PhysicsModel implements Positionable, Movable, Rotatable {
	public PhysicsModel(final Vec3f loc, final Quaternion ori) {
		this.loc = new Vec3f(loc);
		this.ori = new Quaternion();
		rotateTo(ori);
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
		this.ori.setX(ori.getX());
		this.ori.setY(ori.getY());
		this.ori.setZ(ori.getZ());
		this.ori.setW(ori.getW());
	}

	@Override
	public void rotateBy(Quaternion q) {
		ori.mult(q);	//Multiply quaternions to get a new angle
	}

	//Physics information
	private Vec3f loc;
	private Quaternion ori;
}
