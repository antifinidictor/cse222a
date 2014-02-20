package client.network;

import java.nio.ByteBuffer;

public interface NetworkObject {
	//Object types
	public static final byte PLAYER = 0;
	public static final byte LANDSCAPE = 1;
	public static final byte BALL = 2;
	
	//Identification methods
	public int getID();
	public byte getType();

	//Network methods
	/* Serializes this object's state into the buffer at index startIndex, returning the number of bytes used */
	public int serializeAll(ByteBuffer buf);
	
	/* Serializes any randomness into the buffer and returns the number of bytes used */
	public int serializeInput(ByteBuffer buf);
	
	/* Deserializes this object's state from the buffer at index startIndex, returning the number of bytes read */
	public int deserializeAll(ByteBuffer buf);
	
	/* Deserializes this object's randomness from the buffer, returning the number of bytes read */
	public int deserializeInput(ByteBuffer buf);
	

}
