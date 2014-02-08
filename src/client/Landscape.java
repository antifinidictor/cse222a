package client;

import com.jogamp.opengl.math.Quaternion;

import client.physics.HmapCollisionModel;
import client.physics.PhysicsModel;
import client.render.HmapRenderModel;
import client.render.RenderModel;
import client.render.TextureInfo;
import shared.*;

public class Landscape implements GameObject {
	
	public Landscape(final String filename, TextureInfo tex, final Box bounds) {
		Vec3f pos = bounds.center();
		Box relativeBounds = new Box(bounds);
		relativeBounds.max.sub(pos);
		relativeBounds.min.sub(pos);
		pmdl = new PhysicsModel(pos, new Quaternion(), false);	//static
		rmdl = new HmapRenderModel(filename, tex, pmdl, relativeBounds);
		HmapCollisionModel cmdl = new HmapCollisionModel(rmdl.getHmap(), relativeBounds);
		pmdl.setCollision(cmdl);
	}

	@Override
	public PhysicsModel getPhysics() { return pmdl; }

	@Override
	public RenderModel getRender() { return rmdl; }

	@Override
	public void onUpdate() {
		//Do nothing
	}
	
	PhysicsModel pmdl;
	HmapRenderModel rmdl;
}