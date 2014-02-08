package client;

import com.jogamp.opengl.math.Quaternion;

import shared.Vec3f;
import client.physics.PhysicsModel;
import client.physics.SphereCollisionModel;
import client.render.RenderModel;
import client.render.SpriteRenderModel;
import client.render.TextureInfo;

public class Ball implements GameObject {
	public Ball(final Vec3f loc, float radius, TextureInfo tex) {
		Vec3f centerOffset = new Vec3f(0.f,0.f,0.f);
		pmdl = new PhysicsModel(loc, new Quaternion());
		rmdl = new SpriteRenderModel(pmdl, centerOffset, tex);
		rmdl.setHeightScale(radius * 2.f);
		rmdl.setWidthScale(radius * 2.f);
		SphereCollisionModel cmdl = new SphereCollisionModel(centerOffset, radius / 2.f);
		pmdl.setCollision(cmdl);
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

	PhysicsModel pmdl;
	SpriteRenderModel rmdl;
}
