package client;

public class InputState {
	private int state;
	
	//Bits devoted to keys
	public static final int MOVE_FORWARD = 0x1;
	public static final int MOVE_BACKWARD = 0x2;
	public static final int STRAFE_LEFT = 0x4;
	public static final int STRAFE_RIGHT = 0x8;
	public static final int ROTATE_LEFT = 0x10;
	public static final int ROTATE_RIGHT = 0x20;
	public static final int NEW_BALL = 0x40;
	public static final int PUNT_BALL = 0x80;
	public static final int DELETE_BALL = 0x100;
	public static final int PITCH_FORWARD = 0x200;
	public static final int PITCH_BACKWARD = 0x400;
	
	public InputState() {
		state = 0;
	}
	
	//Public accessors: Use for the networking code only
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public void setBit(boolean val, int bit) {
		if(val) {
			state |= bit;
		} else {
			state &= ~bit;
		}
	}
	
	public boolean getBit(int bit) {
		return (state & bit) != 0;
	}
	
	/*
	public boolean getForward() {
		return getBit(MOVE_FORWARD);
	}
	
	public boolean getBackward() {
		return getBit(MOVE_BACKWARD);
	}
	
	public boolean getStrafeLeft() {
		return getBit(STRAFE_LEFT);
	}
	
	public boolean getStrafeRight() {
		return getBit(STRAFE_RIGHT);
	}
	
	public boolean getRotateLeft() {
		return getBit(ROTATE_LEFT);
	}
	
	public boolean getRotateRight() {
		return getBit(ROTATE_RIGHT);
	}
	
	public boolean getNewBall() {
		return getBit(NEW_BALL);
	}
	
	public boolean getPuntBall() {
		return getBit(PUNT_BALL);
	}
	
	public boolean getDeleteBall() {
		return getBit(DELETE_BALL);
	}
	
	public void setForward(boolean in) {
		setBit(in, MOVE_FORWARD);
	}
	
	public void setBackward(boolean in) {
		setBit(in, MOVE_BACKWARD);
	}
	
	public void setForward(boolean in) {
		setBit(in, STRAFE_LEFT);
	}
	
	public void setForward(boolean in) {
		setBit(in, STRAFE_RIGHT);
	}
	
	public void setForward(boolean in) {
		setBit(in, MOVE_FORWARD);
	}
	
	public void setForward(boolean in) {
		setBit(in, MOVE_FORWARD);
	}
	
	public void setForward(boolean in) {
		setBit(in, MOVE_FORWARD);
	}
	
	public void setForward(boolean in) {
		setBit(in, MOVE_FORWARD);
	}
	
	public void setForward(boolean in) {
		setBit(in, MOVE_FORWARD);
	}
	*/
}
