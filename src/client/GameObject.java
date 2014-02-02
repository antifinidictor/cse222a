package client;

import client.physics.PhysicsModel;
import client.render.RenderModel;

/**
 * A basic game object has the following properties
 * @author Nathan Heisey
 *
 */
public interface GameObject {
	public PhysicsModel getPhysics();
	public RenderModel  getRender();
	public void onUpdate();	//Update event callback
}
