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
		animState = AnimState.ANIM_STANDING;
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
		
		//animations.  Set the animState variable to change which animation
        // is playing.
		if(animTimer > MAX_ANIM_TIMER) {
			animTimer = 0;
			if(--animFrame < ANIM_MAP[animState.index()][LAST_FRAME]) {
				animFrame = ANIM_MAP[animState.index()][FIRST_FRAME];
			}
			rmdl.setFrame(-1, animFrame);
		} else {
			animTimer++;
		}
	}

	private int animTimer = 0;
	private int animFrame = 0;
	private PhysicsModel pmdl;
	private SpriteRenderModel  rmdl;
	private AnimState animState;
	
	private static final int NORTH = 4;
	private static final int EAST = 3;
	private static final int SOUTH = 2;
	private static final int WEST = 1;
	
	private enum AnimState {
		ANIM_STANDING(0),
		ANIM_WALKING(1),
		ANIM_THROWING(2),
		ANIM_PUSHING(3),
		ANIM_CLIMBING(4);

		public int index() { return index; }
		
		private AnimState(int index) {
			this.index = index;
		}
		private int index;
	}
	private static final int MAX_ANIM_TIMER = 10;
	private static final int FIRST_FRAME = 0;
	private static final int LAST_FRAME = 1;
	private static final int [][] ANIM_MAP =
		{{19,19},	//standing
		 {18,15},	//walking
		 {14,12},	//throwing
		 {11,8},	//pushing
		 {7,0}};	//climbing
}
