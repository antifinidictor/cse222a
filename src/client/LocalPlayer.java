//Tarfah!

package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.ByteBuffer;

import shared.Vec3f;
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
public class LocalPlayer extends Player implements KeyListener, MouseMotionListener, MouseListener {	
	private float lastCamX;
	private float lastCamY;
	
	boolean forward, backward, left, right, shift;	//movement keys
	boolean puntBall, makeBall, deleteBall;			//ball keys

	private Camera camera;

	public LocalPlayer(int id, final Vec3f loc, final Quaternion ori) {
		super(id, loc, ori);
		camera = new FollowCamera(getPhysics());
		GameEngine.get().getWindow().addKeyListener(this);
		RenderEngine.get().getCanvas().addMouseMotionListener(this);
		RenderEngine.get().getCanvas().addMouseListener(this);
		RenderEngine.get().setCamera(camera);
	}

	@Override
	public void onUpdate() {
		//update input state
		inputState.setBit(forward 	&& !shift, InputState.MOVE_FORWARD);
		inputState.setBit(backward 	&& !shift, InputState.MOVE_BACKWARD);
		inputState.setBit(left 		&& !shift, InputState.ROTATE_LEFT);
		inputState.setBit(right 	&& !shift, InputState.ROTATE_RIGHT);
		inputState.setBit(forward 	&&  shift, InputState.PITCH_FORWARD);
		inputState.setBit(backward 	&&  shift, InputState.PITCH_BACKWARD);
		inputState.setBit(left 		&&  shift, InputState.STRAFE_LEFT);
		inputState.setBit(right 	&&  shift, InputState.STRAFE_RIGHT);
		
		inputState.setBit(puntBall, InputState.PUNT_BALL);
		inputState.setBit(makeBall, InputState.NEW_BALL);
		inputState.setBit(deleteBall, InputState.DELETE_BALL);
		
		//update physics and logic
		super.onUpdate();
		
		//update network state
		
		//These can only occur once per round
		inputState.setBit(false, InputState.PUNT_BALL | InputState.NEW_BALL | InputState.DELETE_BALL);
		puntBall = makeBall = deleteBall = false;
	}

	@Override
	public void keyPressed(KeyEvent ekey) {
		switch(ekey.getKeyCode()) {
		case KeyEvent.VK_W:
			forward = true;
			break;
		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_S:
			backward = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		case KeyEvent.VK_SHIFT:
			shift = true;
			break;
		case KeyEvent.VK_SPACE:
			break;
		default:
			System.out.println("Unrecognized key pressed");
		}
	}

	@Override
	public void keyReleased(KeyEvent ekey) {
		switch(ekey.getKeyCode()) {
		case KeyEvent.VK_W:
			forward = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			backward = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		case KeyEvent.VK_SHIFT:
			shift = false;
			break;
		case KeyEvent.VK_SPACE:
			if(collidingWithObject()) {
				puntBall = true;
			} else {
				makeBall = true;
			}
			break;
		case KeyEvent.VK_Q:
			if(collidingWithObject()) {
				puntBall = true;
			} else {
				System.out.println("Not touching anything (cannot punt)!");
			}
			break;
		case KeyEvent.VK_E:
			if(collidingWithObject()) {
				deleteBall = true;
			} else {
				System.out.println("Not touching anything (cannot delete)!");
			}
			break;
		case KeyEvent.VK_ESCAPE:
			//Lose focus, bring the mouse back
			break;
		default:
			System.out.println("Unrecognized key released");
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseDragged(MouseEvent emouse) {
		/*
		// TODO Auto-generated method stub
		float x = (emouse.getX() - lastCamX) / 640.f * (float)Math.PI;
		float y = (emouse.getY() - lastCamY) / 640.f * (float)Math.PI;
		lastCamX = emouse.getX();
		lastCamY = emouse.getY();
		
		float [] axis = {0.f, 1.f, 0.f};
		Quaternion xquat = new Quaternion(axis, x);
		getPhysics().rotateBy(xquat);
		
		axis[0] = 1.f;
		axis[1] = 0.f;
		Quaternion yquat = new Quaternion(axis, y);
		getPhysics().rotateBy(yquat);
		//System.out.println("Mouse dragged");
		 */
	}

	@Override
	public void mouseMoved(MouseEvent emouse) {
		//System.out.println("Mouse moved");
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		GameEngine.get().getWindow().addKeyListener(this);
		//System.out.println("Mouse clicked");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Mouse entered");
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Mouse exited");
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Mouse pressed");
		lastCamX = arg0.getX();
		lastCamY = arg0.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Mouse released");
		
	}

	@Override
	public int serializeAll(ByteBuffer buf) {
		//Gather state information
		Vec3f pos = getPhysics().loc();
		Quaternion ori = getPhysics().ori();
		int initBufPos = buf.position();
		
		//Serialize input state
		buf.putInt(inputState.getState());
		
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
		int initBufPos = buf.position();
		buf.putInt(inputState.getState());
		return buf.position() - initBufPos;
	}

	@Override
	public int deserializeAll(ByteBuffer buf) {
		//Gather state information
		Vec3f pos = new Vec3f(0.f, 0.f, 0.f);
		Quaternion ori = new Quaternion();
		int initBufPos = buf.position();
		
		//Deserialize input state: Do not use, just remove from the buffer
		buf.getInt();
		
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
		
		getPhysics().moveTo(pos);
		getPhysics().rotateTo(ori);
		
		//Return the number of bytes removed from the buffer
		return buf.position() - initBufPos;
	}

	@Override
	public int deserializeInput(ByteBuffer buf) {
		int initBufPos = buf.position();
		buf.getInt();	//Do not use, just remove from the buffer
		return buf.position() - initBufPos;
	}
}
