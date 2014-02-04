package client.render;

import shared.Movable;
import shared.Positionable;
import shared.Rotatable;
import shared.Vec3f;

public interface Camera extends Positionable, Movable, Rotatable {
	public Vec3f lookAt();
	public void moveRelative(Vec3f shift);
}
