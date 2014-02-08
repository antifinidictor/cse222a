package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import shared.Vec3f;
import client.physics.CapsuleCollisionModel;
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
public class Player implements GameObject, KeyListener, MouseMotionListener, MouseListener {
	public Player(final Vec3f loc, final Quaternion ori) {
		pmdl = new PhysicsModel(loc, ori);
		camera = new FollowCamera(pmdl);

		GameEngine.get().getWindow().addKeyListener(this);
		RenderEngine.get().getCanvas().addMouseMotionListener(this);
		RenderEngine.get().getCanvas().addMouseListener(this);
		RenderEngine.get().setCamera(camera);
		
		CapsuleCollisionModel cmdl = new CapsuleCollisionModel(
			new Vec3f(0.f,-.5f,0.f),	//Player's feet are 1.5m below the camera
			new Vec3f(0.f,0.f,0.f),
			0.5f
		);
		pmdl.setCollision(cmdl);
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
		//camera.moveRelative(new Vec3f(strafeSpeed, 0.f, forwardSpeed));
		float [] vec = {strafeSpeed, 0.f, forwardSpeed};
		pmdl.applyForce(new Vec3f(pmdl.ori().mult(vec)));

    	//float[] axis = {0.f, 1.f, 0.f};
    	//pmdl.rotateBy(new Quaternion(axis, (float) (Math.PI / 250.f)));
	}
	

	@Override
	public void keyPressed(KeyEvent ekey) {
		
		switch(ekey.getKeyCode()) {
		case KeyEvent.VK_W:
			forwardSpeed = -MOVE_SPEED;
			break;
		case KeyEvent.VK_A:
			strafeSpeed = -MOVE_SPEED;
			break;
		case KeyEvent.VK_S:
			forwardSpeed = MOVE_SPEED;
			break;
		case KeyEvent.VK_D:
			strafeSpeed = MOVE_SPEED;
			break;
		default:
			System.out.println("Unrecognized key pressed");
		}
	}

	@Override
	public void keyReleased(KeyEvent ekey) {
		switch(ekey.getKeyCode()) {
		case KeyEvent.VK_W:
			forwardSpeed = 0.f;
			break;
		case KeyEvent.VK_A:
			strafeSpeed = 0.f;
			break;
		case KeyEvent.VK_S:
			forwardSpeed = 0.f;
			break;
		case KeyEvent.VK_D:
			strafeSpeed = 0.f;
			break;
		default:
			System.out.println("Unrecognized key pressed");
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseDragged(MouseEvent emouse) {
		// TODO Auto-generated method stub
		float x = (emouse.getX() - lastCamX) / 640.f * (float)Math.PI;
		float y = (emouse.getY() - lastCamY) / 640.f * (float)Math.PI;
		lastCamX = emouse.getX();
		lastCamY = emouse.getY();
		
		float [] axis = {0.f, 1.f, 0.f};
		Quaternion xquat = new Quaternion(axis, x);
		pmdl.rotateBy(xquat);
		
		axis[0] = 1.f;
		axis[1] = 0.f;
		Quaternion yquat = new Quaternion(axis, y);
		pmdl.rotateBy(yquat);
		//System.out.println("Mouse dragged");
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
	
	private float forwardSpeed;
	private float strafeSpeed;
	private float xrot;
	private float yrot;
	
	private float lastCamX;
	private float lastCamY;

	private PhysicsModel pmdl;
	private RenderModel  rmdl;
	private Camera camera;
	
	private static final float MOVE_SPEED = 0.05f;

}
