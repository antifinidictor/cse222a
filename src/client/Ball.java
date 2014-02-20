package client;

import com.jogamp.opengl.math.Quaternion;

import shared.Vec3f;
import client.network.NetworkObject;
import client.physics.PhysicsModel;
import client.physics.SphereCollisionModel;
import client.render.RenderModel;
import client.render.SpriteRenderModel;
import client.render.TextureInfo;

public class Ball implements GameObject {

	private PhysicsModel pmdl;
	private SpriteRenderModel rmdl;
	private int id;
	
	public Ball(int id, final Vec3f loc, float radius, TextureInfo tex) {
		this.id = id;
		Vec3f centerOffset = new Vec3f(0.f,0.f,0.f);
		pmdl = new PhysicsModel(this, loc, new Quaternion());
		rmdl = new SpriteRenderModel(this, pmdl, centerOffset, tex);
		rmdl.setHeightScale(radius * 2.f);
		rmdl.setWidthScale(radius * 2.f);
		SphereCollisionModel cmdl = new SphereCollisionModel(centerOffset, radius);
		pmdl.setCollision(cmdl);
		pmdl.setFrictionDivider(0.99f);
		pmdl.setMass(100f);
	}

	@Override
	public PhysicsModel getPhysics() {
		return pmdl;
	}

	@Override
	public RenderModel getRender() {
		return rmdl;
	}

	@Override
	public void onUpdate() {
		//All we do is obey the laws of physics...
	}

	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public byte getType() {
		return NetworkObject.BALL;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof GameObject) && ((((GameObject)obj)).getID() == getID());
	}
}
