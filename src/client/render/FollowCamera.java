package client.render;

import shared.Mobile;
import shared.Vec3f;

import com.jogamp.opengl.math.Quaternion;

public class FollowCamera implements Camera {
	public FollowCamera(Mobile leader) {
		this.leader = leader;
	}
	
	@Override
	public Vec3f loc() {
		return leader.loc();
	}

	@Override
	public Quaternion ori() {
		return leader.ori();
	}

	@Override
	public void moveBy(Vec3f shift) {
		leader.moveBy(shift);
	}

	@Override
	public void moveTo(Vec3f pos) {
		leader.moveTo(pos);
	}

	@Override
	public void rotateTo(Quaternion q) {
		leader.rotateTo(q);
	}

	@Override
	public void rotateBy(Quaternion q) {
		leader.rotateBy(q);
	}

	@Override
	public Vec3f lookAt() {
		Vec3f lookAt = new Vec3f(
			ori().mult(unrotLookAtVec.toVector())
		);
		lookAt.add(loc());
		return lookAt;
	}

	@Override
	public void moveRelative(Vec3f shift) {
		Vec3f rotShift = new Vec3f(shift);
		rotShift = new Vec3f(
			ori().mult(rotShift.toVector())
		);
		moveBy(rotShift);
	}

	private static final Vec3f unrotLookAtVec = new Vec3f(0.f, 0.f, -1.f);
	Mobile leader;
}
