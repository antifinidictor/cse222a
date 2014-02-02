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
public class Camera implements Positionable, Movable, Rotatable {

	@Override
	public Vec3f loc() {
		return loc;
	}

	@Override
	public Quaternion ori() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rotateTo(Quaternion q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateBy(Quaternion q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveBy(Vec3f shift) {
		loc.add(shift);
	}

	@Override
	public void moveTo(Vec3f pos) {
		loc.copy(pos);
	}

	Vec3f loc;
}
