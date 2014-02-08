package shared;

public class Box {
	public Vec3f min;
	public Vec3f max;
	
	public Box(final Box box) {
		min = new Vec3f(box.min);
		max = new Vec3f(box.max);
	}
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
	
	public boolean pointIsInside(Vec3f loc) {
		return loc.x() <= max.x() && loc.x() >= min.x() &&
			loc.y() <= max.y() && loc.y() >= min.y() &&
			loc.z() <= max.z() && loc.z() >= min.z();
	}
	
	public float width() { return max.x() - min.x(); }
	public float length() { return max.z() - min.z(); }
	public float height() { return max.y() - min.y(); }
	public Vec3f center() {
		return new Vec3f(
			(max.x() - min.x()) / 2.f,
			(max.y() - min.y()) / 2.f,
			(max.z() - min.z()) / 2.f
		);
	}
}
