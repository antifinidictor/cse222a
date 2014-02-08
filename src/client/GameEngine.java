package client;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import shared.Box;
import shared.Vec3f;

import com.jogamp.opengl.math.Quaternion;

import client.physics.PhysicsEngine;
import client.physics.PhysicsModel;
import client.render.HmapRenderModel;
import client.render.RenderEngine;

public class GameEngine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameEngine.init();
		RenderEngine.init();
		PhysicsEngine.init();
		GameEngine.get().run();
	}
	
	public void run() {
		//I guess we'll stick our main game loop stuff in here?
		//Game engine handles input, RenderEngine handles rendering, PhysicsEngine handles physics,
		//NetworkEngine communicates state to all objects
		/* This thread is unused and probably always will be.  See the RenderEngine
		while(isRunning) {
			//This is a separate thread from the renderer.  Currently we aren't doing anything in here,
			// the render engine handles updating for us
		}
		System.out.println("Exiting game");
		*/
	}
	
	public void update() {
		for(GameObject obj : objs) {
			//Logic update
			obj.onUpdate();
		}
		PhysicsEngine.get().updatePhysics();
	}
	
	public void kill() { isRunning = false; }
	
	public void add(GameObject obj) {
		if(obj.getPhysics() != null) {
			PhysicsEngine.get().add(obj.getPhysics());
		}
		if(obj.getRender() != null) {
			RenderEngine.get().add(obj.getRender());
		}
		objs.add(obj);
	}
	
	public void remove(GameObject obj) {
		if(obj.getPhysics() != null) {
			PhysicsEngine.get().remove(obj.getPhysics());
		}
		if(obj.getRender() != null) {
			RenderEngine.get().remove(obj.getRender());
		}
		objs.remove(obj);
	}
	
	/*
	 * Put initial world creation stuff in here
	 */
	public void buildWorld() {
		float [] axis = {0.f, 1.f, 0.f};
		Player player = new Player(new Vec3f(0.f, 0.f, 0.f), new Quaternion(axis, 0.f));
		add(player);

    	int texID = RenderEngine.get().makeTexture("res/grass.png");
		Landscape testLandscape = new Landscape(
			"res/hmap.jpg",
			RenderEngine.get().getTexture(texID),
			new Box(-5, 0, -5, 10, 1, 10)
		);
		add(testLandscape);
		testLandscape = new Landscape(
				"res/hmap.jpg",
				RenderEngine.get().getTexture(texID),
				new Box(-15, 0, -5, 10, 1, 10)
			);
		add(testLandscape);
		
		texID = RenderEngine.get().makeTexture("res/ball.png");
		Ball ball = new Ball(
			new Vec3f(0.f, .5f, -3.f),
			.5f,	//radius
			RenderEngine.get().getTexture(texID)
		);
		add(ball);
	}
	
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
	private List<GameObject> objs = new ArrayList();
	
	private static GameEngine instance = null;
}
