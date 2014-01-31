package shared;

public class Vec3f {
	public Vec3f(final Vec3f src) {
		copy(src);
	}
	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void copy(final Vec3f src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
	}
	public void add(final Vec3f src) {
		this.x += src.x;
		this.y += src.y;
		this.z += src.z;
	}
	public float x, y, z;
}
