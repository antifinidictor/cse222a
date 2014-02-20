package client;

import java.nio.ByteBuffer;

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
	
/*
	public Landscape(int id, ByteBuffer buf) {
		this.id = id;
		Vec3f pos = bounds.center();
		Box relativeBounds = new Box(bounds);
		relativeBounds.max.sub(pos);
		relativeBounds.min.sub(pos);
		pmdl = new PhysicsModel(this, pos, new Quaternion(), false);	//static
		rmdl = new HmapRenderModel(this, filename, tex, pmdl, relativeBounds);
		HmapCollisionModel cmdl = new HmapCollisionModel(rmdl.getHmap(), relativeBounds);
		pmdl.setCollision(cmdl);
		deserializeAll(buf);
	}
*/

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

	@Override
	public int serializeAll(ByteBuffer buf) {
		//Gather state information
		Vec3f pos = pmdl.loc();
		Quaternion ori = pmdl.ori();
		int initBufPos = buf.position();
		
		//Serialize location
		buf.putFloat(pos.x());
		buf.putFloat(pos.y());
		buf.putFloat(pos.z());
		
		//Serialize orientation
		buf.putFloat(ori.getW());
		buf.putFloat(ori.getX());
		buf.putFloat(ori.getY());
		buf.putFloat(ori.getZ());
		
		//Serialize velocity?
		
		//Return the number of bytes placed in the buffer
		return buf.position() - initBufPos;
	}

	@Override
	public int serializeInput(ByteBuffer buf) {
		//Landscapes don't have any input information to serialize
		return 0;
	}

	@Override
	public int deserializeAll(ByteBuffer buf) {
		//Gather state information
		Vec3f pos = getPhysics().loc();
		Quaternion ori = getPhysics().ori();
		int initBufPos = buf.position();
		
		//Deserialize location
		pos.x(buf.getFloat());
		pos.y(buf.getFloat());
		pos.z(buf.getFloat());
		
		//Deserialize orientation
		ori.setW(buf.getFloat());
		ori.setW(buf.getFloat());
		ori.setW(buf.getFloat());
		ori.setW(buf.getFloat());
		
		//Deserialize velocity?
		
		pmdl.moveTo(pos);
		pmdl.rotateTo(ori);
		
		//Return the number of bytes removed from the buffer
		return buf.position() - initBufPos;
	}

	@Override
	public int deserializeInput(ByteBuffer buf) {
		//Landscapes don't have any input information to deserialize
		return 0;
	}
}
