package shared;

public class Quaternion {
	public float x, y, z, w;
	
	public Quaternion(final Vec3f axis, float angle) {
		float sinHalfAngle = (float)Math.sin(angle / 2.f);
		float cosHalfAngle = (float)Math.cos(angle / 2.f);
		x = axis.x * sinHalfAngle;
		y = axis.y * sinHalfAngle;
		z = axis.z * sinHalfAngle;
		w = cosHalfAngle;
	}
	
	public Quaternion(final Quaternion q) {
		copy(q);
	}
	
	public void copy(final Quaternion q) {
		this.x = q.x;
		this.y = q.y; 
		this.z = q.z;
		this.w = q.w;
	}
	
	//Concatenate a new rotation into this one
	public void concat(final Quaternion q) {
		
	}
}
