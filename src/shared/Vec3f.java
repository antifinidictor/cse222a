package shared;

public class Vec3f {
	public Vec3f(final Vec3f src) {
		vec = new float[3];
		copy(src);
	}
	
	public Vec3f(final float [] vec) {
		this.vec = new float[3];
		for(int i = 0; i < 3; ++i) {
			this.vec[i] = vec[i];
		}
	}
	
	public Vec3f(float x, float y, float z) {
		vec = new float[3];
		vec[X] = x;
		vec[Y] = y;
		vec[Z] = z;
	}
	
	public void copy(final Vec3f src) {
		float [] srcVector = src.toVector();
		for(int i = 0; i < 3; ++i) {
			vec[i] = srcVector[i];
		}
	}
	
	public void add(final Vec3f src) {
		float [] srcVector = src.toVector();
		for(int i = 0; i < 3; ++i) {
			vec[i] += srcVector[i];
		}
	}
	
	public void sub(final Vec3f src) {
		float [] srcVector = src.toVector();
		for(int i = 0; i < 3; ++i) {
			vec[i] -= srcVector[i];
		}
	}
	
	public static float dot(final Vec3f src1, final Vec3f src2) {
		float dotProduct = 0.f;
		float [] srcVector1 = src1.toVector();
		float [] srcVector2 = src2.toVector();
		for(int i = 0; i < 3; ++i) {
			dotProduct += srcVector1[i] * srcVector2[i];
		}
		return dotProduct;
	}
	
	public static Vec3f cross(final Vec3f src1, final Vec3f src2) {
		float x = src1.y() * src2.z() - src1.z() * src2.y();
		float y = src1.z() * src2.x() - src1.x() * src2.z();
		float z = src1.x() * src2.y() - src1.y() * src2.x();
		Vec3f crossProduct = new Vec3f(x,y,z);
		return crossProduct;
	}
	
	public float squareMagnitude() {
		return (x() * x() + y() * y() + z() * z());
	}
	
	public float magnitude() {
		return (float)Math.sqrt(squareMagnitude());
	}
	
	public void scale(float scalar) {
		for(int i = 0; i < 3; ++i) {
			vec[i] *= scalar;
		}
	}
	
	public void normalize() {
		scale(1.f / magnitude());
	}
	
	public void normalizeTo(float scale) {
		scale(scale / magnitude());
	}
	
	@Override
	public String toString() {
		return "(" + x() + "," + y() + "," + z() + ")";
	}
	
	public float x() { return vec[X]; }
	public float y() { return vec[Y]; }
	public float z() { return vec[Z]; }
	
	public void x(float x) { vec[X] = x; }
	public void y(float y) { vec[Y] = y; }
	public void z(float z) { vec[Z] = z; }
	
	public float[] toVector() { return vec; }
	
	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	
	private float [] vec;
}
