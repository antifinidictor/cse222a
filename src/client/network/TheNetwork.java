package client.network;

import java.nio.ByteBuffer;

public interface TheNetwork {
	
	/* This will be passed the aggregated user input, so parse through it t get individual input events.*/
	public void updateState(byte[] buf);
    
	/*This returns the entire state serialized.*/
    public byte[] getState();
    
    /*This, given a serialized state, rplaces the entire state with the given one. */
    public void decodeState(ByteBuffer wrapped);
    
    public void setClientID(int id);

}