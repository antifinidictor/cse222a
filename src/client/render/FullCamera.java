package client.render;

import com.jogamp.opengl.math.Quaternion;

import shared.Movable;
import shared.Positionable;
import shared.Rotatable;
import shared.Vec3f;

/**
 * Simple camera class.  Allows operations to be performed by rotations or by position/lookat
 * @author Nathan Heisey
 *
 */
public class FullCamera implements Camera {
	public FullCamera(final Vec3f loc, final Quaternion q) {
		this.loc = new Vec3f(loc);
		this.ori = new Quaternion(q);
	}

	@Override
	public Vec3f loc() {
		return loc;
	}
	
	@Override
	public Vec3f lookAt() {
		Vec3f lookAt = new Vec3f(
			ori.mult(unrotLookAtVec.toVector())
		);
		lookAt.add(loc);
		return lookAt;
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
		ori.mult(q);
	}

	@Override
	public void moveBy(Vec3f shift) {
		loc.add(shift);
	}
	
	@Override
	public void moveRelative(Vec3f shift) {
		Vec3f rotShift = new Vec3f(shift);
		ori.mult(rotShift.toVector());
		loc.add(rotShift);
	}

	@Override
	public void moveTo(Vec3f pos) {
		loc.copy(pos);
	}

	private static final Vec3f unrotLookAtVec = new Vec3f(0.f, 0.f, -1.f);
	private Quaternion ori;
	private Vec3f loc;
}
