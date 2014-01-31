package client;

import client.render.*;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

public class GameEngine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameEngine.get().run();
	}
	
	public void run() {
		//I guess we'll stick our main game loop stuff in here?
		//Game engine handles input, RenderEngine handles rendering, PhysicsEngine handles physics,
		//NetworkEngine communicates state to all objects
		while(isRunning) {

		}
	}
	
	public void kill() { isRunning = false; }
	
	public static GameEngine get() {
		if(instance == null) {
			instance = new GameEngine();
		}
		return instance;
	}
	
	private GameEngine() {
		RenderEngine.get().setFrameTitle("Ball-Kicker Client");	//Make sure this gets initialized
	}

	private boolean isRunning = true;
	
	private static GameEngine instance = null;
}
