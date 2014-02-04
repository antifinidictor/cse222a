package shared;

public class Box {
	public Box(float x, float y, float z, float w, float h, float l) {
		min = new Vec3f(x, y, z);
		max = new Vec3f(x + w, y + h, z + l);
	}
	public Box(Vec3f min, Vec3f max) {
		min = new Vec3f(min);
		max = new Vec3f(max);
	}
	@Override
	public String toString() {
		return "{" + min + ":" + max + "}";
	}
	
	public Vec3f min;
	public Vec3f max;
	public float width() { return max.x() - min.x(); }
	public float length() { return max.z() - min.z(); }
	public float height() { return max.y() - min.y(); }
}
