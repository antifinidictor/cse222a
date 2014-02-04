package client.physics;


public interface CollisionModel {
	CollisionTypes type();
	public static enum CollisionTypes {
		HMAP,
		CAPSULE,
		SPHERE
	}
}
