package client.physics;

import shared.Locatable;
import shared.Movable;
import shared.Vec3f;

public class PhysicsModel implements Locatable, Movable {
	public PhysicsModel(Vec3f pos) {
		this.pos = pos;
	}

	@Override
	public void moveBy(final Vec3f shift) {
		pos.add(shift);
	}

	@Override
	public void moveTo(final Vec3f pos) {
		this.pos.copy(pos);
	}

	@Override
	public Vec3f pos() {
		return pos;
	}
	
	private Vec3f pos;
}
