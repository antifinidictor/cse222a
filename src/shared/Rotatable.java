package shared;

import com.jogamp.opengl.math.Quaternion;

public interface Rotatable {
	public void rotateTo(final Quaternion q);
	public void rotateBy(final Quaternion q);
}
