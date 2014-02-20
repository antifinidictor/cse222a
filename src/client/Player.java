//Tarfah!

package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import shared.Vec3f;
import client.network.NetworkObject;
import client.physics.CapsuleCollisionModel;
import client.physics.CollisionListener;
import client.physics.PhysicsModel;
import client.render.Camera;
import client.render.FollowCamera;
import client.render.RenderEngine;
import client.render.RenderModel;

import com.jogamp.opengl.math.Quaternion;

/**
 * A class that handles player input and update events
 * @author Nathan Heisey
 *
 */
public abstract class Player implements GameObject, CollisionListener {
	
	private float forwardSpeed;
	private float strafeSpeed;
	private float yaw, yawSpeed;
	private float pitch, pitchSpeed;

	private PhysicsModel pmdl;
	private RenderModel  rmdl;

	private PhysicsModel collidingWith;
	private int id;
	
	private static final float MOVE_SPEED = 0.01f;
	private static final float PUNCH_FORCE_MAGNITUDE = 10.f;
	private static final float ROTATION_SPEED = (float)Math.PI / 100.f;
	protected InputState inputState;
	
	public Player(int id, final Vec3f loc, final Quaternion ori) {
		this.id = id;
		float radius = 0.5f;
		pmdl = new PhysicsModel(this, loc, ori);
		yaw = pitch = yawSpeed = pitchSpeed = 0.f;

		
		CapsuleCollisionModel cmdl = new CapsuleCollisionModel(
			new Vec3f(0.f,-radius,0.f),	//Player's feet are 1.5m below the camera
			new Vec3f(0.f,0.f,0.f),
			radius
		);
		pmdl.setCollision(cmdl);
		pmdl.setCollisionListener(this);
		rmdl = null;
		inputState = new InputState();
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
		//Update speeds
		if(inputState.getBit(InputState.MOVE_FORWARD)) {
			forwardSpeed = -MOVE_SPEED;
		} else if(inputState.getBit(InputState.MOVE_BACKWARD)) {
			forwardSpeed = MOVE_SPEED;
		} else {
			forwardSpeed = 0.f;
		}
		if(inputState.getBit(InputState.STRAFE_LEFT)) {
			strafeSpeed = -MOVE_SPEED;
		} else if(inputState.getBit(InputState.STRAFE_RIGHT)) {
			strafeSpeed = MOVE_SPEED;
		} else {
			strafeSpeed = 0.f;
		}
		if(inputState.getBit(InputState.ROTATE_LEFT)) {
			yawSpeed = ROTATION_SPEED;
		} else if(inputState.getBit(InputState.ROTATE_RIGHT)) {
			yawSpeed = -ROTATION_SPEED;
		} else {
			yawSpeed = 0.f;
		}
		if(inputState.getBit(InputState.PITCH_FORWARD)) {
			pitchSpeed = ROTATION_SPEED;
		} else if(inputState.getBit(InputState.PITCH_BACKWARD)) {
			pitchSpeed = -ROTATION_SPEED;
		} else {
			pitchSpeed = 0.f;
		}
		
		//Make balls
		if(inputState.getBit(InputState.PUNT_BALL)) {
			puntBall();
		} else if(inputState.getBit(InputState.DELETE_BALL)) {
			deleteBall();
		} else if(inputState.getBit(InputState.NEW_BALL)) {
			makeBall();
		}
		
		//rotation
		yaw += yawSpeed;
		pitch += pitchSpeed;
		float [] yawVec = {0.f, 1.f, 0.f};
		float [] pitchVec = {1.f, 0.f, 0.f};
		Quaternion ori = new Quaternion(yawVec, yaw);
		ori.mult(new Quaternion(pitchVec, pitch));
		pmdl.rotateTo(ori);
		
		//movement
		float [] vec = {strafeSpeed, 0.f, forwardSpeed};
		pmdl.applyForce(new Vec3f(ori.mult(vec)));

		//Reset collisions
		collidingWith = null;
	}

	@Override
	public void onCollision(PhysicsModel pmdl) {
		collidingWith = pmdl;
	}
	
	private void makeBall() {
		float [] fwd = {0.f, 0.f, -1.f};
		Vec3f ballPos = new Vec3f(pmdl.ori().mult(fwd));
		ballPos.add(pmdl.loc());
		Ball ball = new Ball(GameEngine.get().genID(), ballPos, 0.5f, RenderEngine.get().getTexture(1));
		GameEngine.get().add(ball);
	}
	
	private void puntBall() {
		if(!collidingWithObject()) {
			System.out.println("ERROR: Could not punt: Not colliding with ball!");
			return;
		}
		Vec3f force = new Vec3f(collidingWith.loc());
		force.sub(pmdl.loc());
		force.normalizeTo(PUNCH_FORCE_MAGNITUDE);
		collidingWith.applyForce(force);
	}
	
	private void deleteBall() {
		if(!collidingWithObject()) {
			System.out.println("ERROR: Could not punt: Not colliding with ball!");
			return;
		}
		GameEngine.get().remove(collidingWith.getParent());
		collidingWith = null;
	}
	
	
	protected boolean collidingWithObject() {
		return collidingWith != null;
	}

	@Override
	public int getID() {
		return id;
	}
	
	//Use when the player disconnects and needs to reconnect
	public void setID(int id) {
		this.id = id;
	}
	
	@Override
	public byte getType() {
		return NetworkObject.PLAYER;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof GameObject) && ((((GameObject)obj)).getID() == getID());
	}

}
