package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.physics.PhysicsModel;
import client.render.RenderModel;

/**
 * A class that handles player input and update events
 * @author Nathan Heisey
 *
 */
public class Player implements GameObject, KeyListener {
	public Player() {
		GameEngine.get().getWindow().addKeyListener(this);
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
	}
	

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private PhysicsModel pmdl;
	private RenderModel  rmdl;
}
