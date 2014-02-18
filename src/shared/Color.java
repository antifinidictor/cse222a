package shared;

public class Color {
	private float r, g, b, a;
	
	public float getRed()	{ return r; }
	public float getGreen()	{ return g; }
	public float getBlue()	{ return b; }
	public float getAlpha()	{ return a; }
	
	public Color() {
		this(1.f, 1.f, 1.f, 1.f);
	}
	
	public Color(final Color src) {
		this(src.getRed(), src.getGreen(), src.getBlue(), src.getAlpha());
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1.f);
	}
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
}
