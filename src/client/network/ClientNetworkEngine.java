package client.network;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import shared.Vec3f;
import client.GameEngine;
import client.LocalPlayer;
import client.Player;

import com.jogamp.opengl.math.Quaternion;

public class ClientNetworkEngine implements TheNetwork {
	private Player player = null;
	//private int clientID = -1;
	private Set<NetworkObject> objs;
	private static final int BUFFER_SIZE = 256;
	
	private static ClientNetworkEngine instance;
	
	public static ClientNetworkEngine get() { return instance; }
	public static void init() { instance = new ClientNetworkEngine(); }
	
	private ClientNetworkEngine() {
		Comparator<NetworkObject> comp = new Comparator<NetworkObject>() {
			@Override
			public int compare(NetworkObject obj0, NetworkObject obj1) {
				return obj0.getID() - obj1.getID();
			}
		};
		objs  = new TreeSet(comp);
	}
	
	public void add(NetworkObject obj) {
		objs.add(obj);
	}
	
	public void remove(NetworkObject obj) {
		objs.remove(obj);
	}
	
	private NetworkObject find(int id) {
		for(NetworkObject obj : objs) {
			if(obj.getID() == id) {
				return obj;
			}
		}
		return null;
	}
	
	/* This will be passed the aggregated user input, so parse through it to get individual input events.*/
	@Override
	public void updateState(byte[] buf) {
		ByteBuffer wrapped = ByteBuffer.wrap(buf);
		while(wrapped.position() < wrapped.limit()) {
			//Pull out the next object id
			int id = wrapped.getInt();
			byte type = wrapped.get();
			NetworkObject obj = find(id);
			if(obj == null) {
				//TODO: Create the object?
			} else {
				//Update the object
				obj.deserializeInput(wrapped);
			}
		}
	}

	/*This returns the entire state serialized.*/
	@Override
	public byte[] getState() {
		ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
		for(NetworkObject obj : objs) {
			buf.putInt(obj.getID());
			buf.put(obj.getType());
			obj.serializeAll(buf);
		}
		return buf.array();
	}
	
	/* This, given a serialized state, replaces the entire state with the given one. 
	 * TODO: Handle objects that exist in this client, but not in the provided state. */
	@Override
	public void decodeState(ByteBuffer wrapped) {
		while(wrapped.position() < wrapped.limit()) {
			//Pull out the next object id
			int id = wrapped.getInt();
			byte type = wrapped.get();
			NetworkObject obj = find(id);
			if(obj == null) {
				//TODO: Create the object?
			} else {
				//Update the object
				obj.deserializeAll(wrapped);
			}
		}
		
	}

	@Override
	public void setClientID(int id) {
		//If we have not yet received an ID, make a new player
		if(player == null) {
			player = new LocalPlayer(id, new Vec3f(0.f, 0.f, 0.f), new Quaternion());
			GameEngine.get().add(player);
		} else {
			GameEngine.get().freeID(player.getID());
			GameEngine.get().requestID(id);	//Not sure what to do if the ID is taken
			player.setID(id);
		}
	}

}
