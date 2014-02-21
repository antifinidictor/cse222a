package client;

import java.awt.Color;
import java.nio.ByteBuffer;

import shared.Vec3f;

import com.jogamp.opengl.math.Quaternion;

import client.physics.CapsuleCollisionModel;
import client.physics.PhysicsModel;
import client.physics.SphereCollisionModel;
import client.render.RenderEngine;
import client.render.RenderModel;
import client.render.SpriteRenderModel;
import client.render.TextureInfo;

public class NetworkPlayer extends Player {

	private int animTimer = 0;
	private int animFrame = 0;
	private SpriteRenderModel  rmdl;
	private AnimState animState;
	
	private static final int NORTH = 8;
	private static final int NORTHEAST = 7;
	private static final int EAST = 6;
	private static final int SOUTHEAST = 5;
	private static final int SOUTH = 4;
	private static final int SOUTHWEST = 3;
	private static final int WEST = 2;
	private static final int NORTHWEST = 1;
    private static final double SECTION = Math.PI / 4.f;
	
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
	private int id;
	
	public NetworkPlayer(int id, final Vec3f loc, final Quaternion ori, TextureInfo tex) {
		super(id, loc, ori);
		this.id = id;
		float radius = 0.5f;
		Vec3f renderOffset = new Vec3f(0.f,-radius,0.f);
		Vec3f centerOffset = new Vec3f(0.f,0.f,0.f);
		rmdl = new SpriteRenderModel(this, getPhysics(), renderOffset, tex);
		//rmdl.setHeightScale(radius * 2.f);
		//rmdl.setWidthScale(radius * 2.f);
		rmdl.setFrame(2, 0);
		animState = AnimState.ANIM_WALKING;
		//animState = AnimState.ANIM_STANDING;
	}

	@Override
	public RenderModel getRender() {
		return rmdl;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		PhysicsModel pmdl = getPhysics();

		
		/*
		 * Rotating the sprite and updating the animation
		 */
		//Change the frame relative to the camera position and my rotation
		Vec3f camVec = new Vec3f(RenderEngine.get().getCamera().loc());
		camVec.sub(pmdl.loc());
		
		Vec3f myDir = new Vec3f(pmdl.ori().mult(camVec.toVector()));
		myDir.normalize();

        double theta = (Math.atan2(myDir.x(), myDir.z()));
        int frameDir = (int)Math.floor((theta + SECTION / 2.f) / SECTION) + 4;
        if(frameDir < 1) {
        	frameDir = rmdl.getTexInfo().getFramesWide();
        }
        //System.out.println("Frame = " + frameDir + ", angle = " + (theta * 180 / Math.PI));
        rmdl.setFrame(frameDir, -1);
		
		//Animations: Set the animState variable to change which animation
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

	@Override
	public int serializeAll(ByteBuffer buf) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int serializeInput(ByteBuffer buf) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deserializeAll(ByteBuffer buf) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deserializeInput(ByteBuffer buf) {
		// TODO Auto-generated method stub
		return 0;
	}
}
