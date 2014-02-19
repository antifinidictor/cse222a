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
public class Player implements GameObject, KeyListener, MouseMotionListener, MouseListener, CollisionListener {
	
	private float forwardSpeed;
	private float strafeSpeed;
	private float xrot;
	private float yrot;
	
	private float lastCamX;
	private float lastCamY;

	private PhysicsModel pmdl;
	private RenderModel  rmdl;
	private Camera camera;

	private PhysicsModel collidingWith;
	private int id;
	
	private static final float MOVE_SPEED = 0.01f;
	private static final float PUNCH_FORCE_MAGNITUDE = 10.f;
	protected static final int MOVE_FORWARD = 0;
	protected static final int MOVE_BACKWARD = 1;
	protected static final int TURN_LEFT = 2;
	protected static final int TURN_RIGHT = 3;
	
	public Player(int id, final Vec3f loc, final Quaternion ori) {
		this.id = id;
		float radius = 0.5f;
		pmdl = new PhysicsModel(loc, ori);
		camera = new FollowCamera(pmdl);

		GameEngine.get().getWindow().addKeyListener(this);
		RenderEngine.get().getCanvas().addMouseMotionListener(this);
		RenderEngine.get().getCanvas().addMouseListener(this);
		RenderEngine.get().setCamera(camera);
		
		CapsuleCollisionModel cmdl = new CapsuleCollisionModel(
			new Vec3f(0.f,-radius,0.f),	//Player's feet are 1.5m below the camera
			new Vec3f(0.f,0.f,0.f),
			radius
		);
		pmdl.setCollision(cmdl);
		pmdl.setCollisionListener(this);
		rmdl = null;
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
		collidingWith = null;
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
		case KeyEvent.VK_SPACE:
			if(collidingWith == null) {
				//Make a new ball
				makeBall();
			} else {
				//Punt the ball
				puntBall();
			}
			break;
		case KeyEvent.VK_Q:
			if(collidingWith == null) {
				System.out.println("Not touching anything!");
			} else {
				puntBall();
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

	@Override
	public void onCollision(PhysicsModel pmdl) {
		collidingWith = pmdl;
	}
	
	protected void makeBall() {
		float [] fwd = {0.f, 0.f, -1.f};
		Vec3f ballPos = new Vec3f(pmdl.ori().mult(fwd));
		ballPos.add(pmdl.loc());
		Ball ball = new Ball(GameEngine.get().genID(), ballPos, 0.5f, RenderEngine.get().getTexture(1));
		GameEngine.get().add(ball);
	}
	
	protected void puntBall() {
		Vec3f force = new Vec3f(collidingWith.loc());
		force.sub(pmdl.loc());
		force.normalizeTo(PUNCH_FORCE_MAGNITUDE);
		collidingWith.applyForce(force);
	}
	
	protected void onStrafeLeft() {
		strafeSpeed = -MOVE_SPEED;
	}
	
	protected void onStrafeRight() {
		strafeSpeed = MOVE_SPEED;
	}
	
	protected void onMoveForward() {
		forwardSpeed = -MOVE_SPEED;
	}
	
	protected void onMoveBackward() {
		forwardSpeed = MOVE_SPEED;
	}

	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof GameObject) && ((((GameObject)obj)).getID() == getID());
	}

}
