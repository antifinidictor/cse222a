package client.physics;

import shared.Box;
import shared.Vec3f;

public class HmapCollisionModel implements CollisionModel {

	@Override
	public CollisionTypes type() {
		return CollisionTypes.HMAP;
	}
	
	//Positions are from the min corner of bounds
	public float getHeightAt(final Vec3f pos) {
		//Get the indices of the corners
		float width = hmap.length - 1;
		float length = hmap[0].length - 1;
		float x = (pos.x() + bounds.min.x()) * width / bounds.width();
		float z = (pos.x() + bounds.min.x()) * length / bounds.length();
		int minX = limitIndex(0, (int)Math.floor(x), (int)width);
		int minZ = limitIndex(0, (int)Math.floor(z), (int)length);
		int maxX = limitIndex(0, (int)Math.ceil(x), (int)width);
		int maxZ = limitIndex(0, (int)Math.ceil(z), (int)length);

		//Get the height at each of the corner indices
	    float y00 = hmap[minX][minZ];
	    float y01 = hmap[minX][maxZ];
	    float y11 = hmap[maxX][maxZ];
	    float y10 = hmap[maxX][minZ];

	    //Interpolate to get the height at the specified point
	    float xDiff = (minX == maxX) ? 0.5f : (maxX - x) / (maxX - minX);
	    float xInterpMinZ = (xDiff) * y00 + (1.f - xDiff) * y10;
	    float xInterpMaxZ = (xDiff) * y01 + (1.f - xDiff) * y11;
	    float zDiff = (minZ == maxZ) ? 0.5f : (maxZ - z) / (maxZ - minZ);
	    float interp = zDiff * xInterpMinZ + (1 - zDiff) * xInterpMaxZ;
		
		return interp + bounds.min.y();
	}
	
	public Vec3f getNormalAt(final Vec3f pos) {
		//Get the indices of the corners
		float width = hmap.length - 1;
		float length = hmap[0].length - 1;
		float x = (pos.x() + bounds.min.x()) * width / bounds.width();
		float z = (pos.x() + bounds.min.x()) * length / bounds.length();
		int minX = limitIndex(0, (int)Math.floor(x), (int)width);
		int minZ = limitIndex(0, (int)Math.floor(z), (int)length);
		int maxX = limitIndex(0, (int)Math.ceil(x), (int)width);
		int maxZ = limitIndex(0, (int)Math.ceil(z), (int)length);

		//Get the height at each of the corner indices
	    float y00 = hmap[minX][minZ];
	    float y01 = hmap[minX][maxZ];
	    float y11 = hmap[maxX][maxZ];
	    float y10 = hmap[maxX][minZ];
	    
	    //Calculate the normal using the vector cross product
	    /* This is the layout of the triangle strip:
	     * y00-y10
	     *  | / |
	     * y01-y11
	     */
	    Vec3f v1 = new Vec3f(0.f, y10 - y00, 0.f);
	    Vec3f v2 = new Vec3f(0.f, y01 - y00, 0.f);

	    /*
	    //Interpolate to get the height at the specified point
	    float xDiff = (minX == maxX) ? 0.5f : (maxX - x) / (maxX - minX);
	    float xInterpMinZ = (xDiff) * y00 + (1.f - xDiff) * y10;
	    float xInterpMaxZ = (xDiff) * y01 + (1.f - xDiff) * y11;
	    float zDiff = (minZ == maxZ) ? 0.5f : (maxZ - z) / (maxZ - minZ);
	    float interp = zDiff * xInterpMinZ + (1 - zDiff) * xInterpMaxZ;
		*/
		return null;
	}
	
	private int limitIndex(int min, int val, int max) {
		if(val < min) {
			return min;
		} else if(val > max) {
			return max;
		} else {
			return val;
		}
	}

	private float hmap[][];
	private Box bounds;
}
