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
		GameEngine.init();
		RenderEngine.init();
		GameEngine.get().run();
	}
	
	public void run() {
		//I guess we'll stick our main game loop stuff in here?
		//Game engine handles input, RenderEngine handles rendering, PhysicsEngine handles physics,
		//NetworkEngine communicates state to all objects
		while(isRunning) {
			//This is a separate thread from the renderer. If syncing is necessary, we'll find a way
		}
		System.out.println("Exiting game");
	}
	
	public void kill() { isRunning = false; }
	
	public static void init() { instance = new GameEngine(); }
	
	public static GameEngine get() { return instance; }
	
	private GameEngine() {
		System.out.println("Initializing main engine");
		window = new Frame("CSE 222A Wi14 Project");	//This title may not stick
		
        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
		window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	RenderEngine.get().kill();	//Stop the animator thread
            	GameEngine.get().kill();	//Stop the game engine
                System.exit(0);				//Kill whatever thread might be here
            }
        });
	}
	
	public Frame getWindow() { return window; }

	private boolean isRunning = true;
	private Frame window;
	
	private static GameEngine instance = null;
}
