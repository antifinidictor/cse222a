package client;

import com.jogamp.opengl.math.Quaternion;

import client.network.NetworkObject;
import client.physics.HmapCollisionModel;
import client.physics.PhysicsModel;
import client.render.HmapRenderModel;
import client.render.RenderModel;
import client.render.TextureInfo;
import shared.*;

public class Landscape implements GameObject {
	private PhysicsModel pmdl;
	private HmapRenderModel rmdl;
	private int id;
	
	public Landscape(int id, final String filename, TextureInfo tex, final Box bounds) {
		this.id = id;
		Vec3f pos = bounds.center();
		Box relativeBounds = new Box(bounds);
		relativeBounds.max.sub(pos);
		relativeBounds.min.sub(pos);
		pmdl = new PhysicsModel(this, pos, new Quaternion(), false);	//static
		rmdl = new HmapRenderModel(this, filename, tex, pmdl, relativeBounds);
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
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public byte getType() {
		return NetworkObject.LANDSCAPE;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof GameObject) && ((((GameObject)obj)).getID() == getID());
	}
}
