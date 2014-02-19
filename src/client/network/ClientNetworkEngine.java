package client.network;

import java.nio.ByteBuffer;

public class ClientNetworkEngine implements TheNetwork {
	private int clientId = -1;
	
	@Override
	public void updateState(byte[] buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decodeState(ByteBuffer wrapped) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientID(int id) {
		// TODO Auto-generated method stub
		if(clientId < 0) {
		}
	}

}
