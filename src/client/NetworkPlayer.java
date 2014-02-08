package client;

import shared.Vec3f;

import com.jogamp.opengl.math.Quaternion;

import client.physics.CapsuleCollisionModel;
import client.physics.PhysicsModel;
import client.physics.SphereCollisionModel;
import client.render.RenderEngine;
import client.render.RenderModel;
import client.render.SpriteRenderModel;
import client.render.TextureInfo;

public class NetworkPlayer implements GameObject {
	public NetworkPlayer(final Vec3f loc, final Quaternion ori, TextureInfo tex) {
		float radius = 0.5f;
		Vec3f renderOffset = new Vec3f(0.f,-radius,0.f);
		Vec3f centerOffset = new Vec3f(0.f,0.f,0.f);
		pmdl = new PhysicsModel(loc, ori);
		rmdl = new SpriteRenderModel(pmdl, renderOffset, tex);
		//rmdl.setHeightScale(radius * 2.f);
		//rmdl.setWidthScale(radius * 2.f);
		CapsuleCollisionModel cmdl = new CapsuleCollisionModel(
			new Vec3f(0.f,-radius,0.f),	//Player's feet are 1.5m below the camera
			centerOffset,
			radius
		);
		pmdl.setCollision(cmdl);
		rmdl.setFrame(2, 0);
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
		//TODO: Get network info
		
		//Change the frame relative to the camera position and my rotation
		//float [] forwardVec = {0.f, 0.f, -1.f};
		Vec3f camVec = new Vec3f(RenderEngine.get().getCamera().loc());
		camVec.sub(pmdl.loc());
		
		Vec3f myDir = new Vec3f(pmdl.ori().mult(camVec.toVector()));
		myDir.normalize();

        double theta = Math.atan2(myDir.x(), myDir.z());
        if(theta > 3 * Math.PI / 4.f || theta < -3 * Math.PI / 4.f) {
            rmdl.setFrame(NORTH, -1);	//North
        } else if(theta > Math.PI / 4.f && theta < 3 * Math.PI / 4.f) {
            rmdl.setFrame(EAST, -1);	//East
        } else if(theta > -Math.PI / 4.f && theta < Math.PI / 4.f) {
            rmdl.setFrame(SOUTH, -1);	//South
        } else {
            rmdl.setFrame(WEST, -1);	//West
        }
		
		//animations
		if(animTimer > MAX_ANIM_TIMER) {
			animTimer = 0;
			animFrame = (animFrame + 1) % rmdl.getTexInfo().getFramesHigh();
			rmdl.setFrame(-1, animFrame);
		} else {
			animTimer++;
		}
	}

	private static final int MAX_ANIM_TIMER = 20;
	private int animTimer = 0;
	private int animFrame = 0;
	private PhysicsModel pmdl;
	private SpriteRenderModel  rmdl;
	
	private static final int NORTH = 4;
	private static final int EAST = 3;
	private static final int SOUTH = 2;
	private static final int WEST = 1;
}
