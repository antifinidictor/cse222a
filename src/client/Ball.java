package client;

import java.nio.ByteBuffer;

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

	@Override
	public int serializeAll(ByteBuffer buf) {
		 Vec3f pos = getPhysics().loc();
			Quaternion ori = getPhysics().ori();
			int initBufPos = buf.position();
			
			//no input state? 
			
			buf.putFloat(pos.x());
			buf.putFloat(pos.y());
			buf.putFloat(pos.z());
			
			buf.putFloat(ori.getW());
			buf.putFloat(ori.getX());
			buf.putFloat(ori.getY());
			buf.putFloat(ori.getZ());
							
			return buf.position() - initBufPos;
	}

	@Override
	public int serializeInput(ByteBuffer buf) {
		// no serialize input
		return 0;
	}

	@Override
	public int deserializeAll(ByteBuffer buf) {
		Vec3f pos = new Vec3f(0.f, 0.f, 0.f);
		Quaternion ori = new Quaternion();
		int initBufPos = buf.position();
		
        //no input state to get?
		
		pos.x(buf.getFloat());
		pos.y(buf.getFloat());
		pos.z(buf.getFloat());
		
		ori.setW(buf.getFloat());
		ori.setW(buf.getFloat());
		ori.setW(buf.getFloat());
		ori.setW(buf.getFloat());
						
		getPhysics().moveTo(pos);
		getPhysics().rotateTo(ori);
		
		return buf.position() - initBufPos;
	}

	@Override
	public int deserializeInput(ByteBuffer buf) {
		// no deserialize input
		return 0;
	}
}
