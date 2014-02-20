package client;

import client.network.NetworkObject;
import client.physics.PhysicsModel;
import client.render.RenderModel;

/**
 * A basic game object has the following properties
 * @author Nathan Heisey
 *
 */
public interface GameObject extends NetworkObject {
	public PhysicsModel getPhysics();
	public RenderModel  getRender();
	public void onUpdate();	//Update event callback
	public int getID();	//Returns a unique identifier for this object
}
