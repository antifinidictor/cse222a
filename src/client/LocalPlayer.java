//Tarfah!

package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
	
	boolean forward, backward, left, right, shift;

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
		if(forward) {
			if(shift) {
				this.onPitchUp();
			} else {
				this.onMoveForward();
			}
		} else if(backward) {
			if(shift) {
				this.onPitchDown();
			} else {
				this.onMoveBackward();
			}
		}
		if(left) {
			this.onRotateLeft();
		} else if(right) {
			this.onRotateRight();
		}
		super.onUpdate();
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
				//Punt the ball
				puntBall();
			} else {
				//Make a new ball
				makeBall();
			}
			break;
		case KeyEvent.VK_Q:
			if(collidingWithObject()) {
				puntBall();
			} else {
				System.out.println("Not touching anything!");
			}
			break;
		case KeyEvent.VK_E:
			makeBall();
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
}
