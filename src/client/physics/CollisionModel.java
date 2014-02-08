package client.physics;

import shared.Positionable;


public interface CollisionModel {
	CollisionTypes type();
	void setPositionable(Positionable myPos);
	public static enum CollisionTypes {
		HMAP,
		CAPSULE,
		SPHERE
	}
}
