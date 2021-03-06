package client;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import shared.Box;
import shared.Vec3f;
import client.network.ClientNetworkEngine;
import client.physics.PhysicsEngine;
import client.render.RenderEngine;

import com.jogamp.opengl.math.Quaternion;

public class GameEngine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameEngine.init();
		RenderEngine.init();
		PhysicsEngine.init();
		ClientNetworkEngine.init();
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
		removeAll();
		addAll();
		
		for(GameObject obj : objs) {
			//Logic update
			obj.onUpdate();
		}
		PhysicsEngine.get().updatePhysics();
	}
	
	public void kill() { isRunning = false; }
	
	public void add(GameObject obj) {
		objsToAdd.add(obj);
	}
	
	public void remove(GameObject obj) {
		objsToRemove.add(obj);
	}
	
	private void addAll() {
		for(GameObject obj : objsToAdd) {
			if(obj.getPhysics() != null) {
				PhysicsEngine.get().add(obj.getPhysics());
			}
			if(obj.getRender() != null) {
				RenderEngine.get().add(obj.getRender());
			}
			ClientNetworkEngine.get().add(obj);
			objs.add(obj);
		}
		objsToAdd.clear();
	}
	
	private void removeAll() {
		for(GameObject obj : objsToRemove) {
			if(obj.getPhysics() != null) {
				PhysicsEngine.get().remove(obj.getPhysics());
			}
			if(obj.getRender() != null) {
				RenderEngine.get().remove(obj.getRender());
			}
			ClientNetworkEngine.get().remove(obj);
			objs.remove(obj);
			freeID(obj.getID());	//presumably this object is dead
		}
		objsToRemove.clear();
	}
	
	/*
	 * Put initial world creation stuff in here
	 */
	public void buildWorld() {
		float [] axis = {0.f, 1.f, 0.f};
		Player player = new LocalPlayer(genID(), new Vec3f(1.f, 0.f, 0.f), new Quaternion(axis, (float)Math.PI / 1.5f));
		add(player);

    	int texID = RenderEngine.get().makeTexture("res/grass.png");
		Landscape testLandscape = new Landscape(
			genID(),
			"res/hmap.jpg",
			RenderEngine.get().getTexture(texID),
			new Box(-5, -1, -5, 10, 2, 10)
		);
		add(testLandscape);
		testLandscape = new Landscape(
				genID(),
				"res/hmap.jpg",
				RenderEngine.get().getTexture(texID),
				new Box(-15, -1, -5, 10, 1, 10)
			);
		add(testLandscape);
		
		texID = RenderEngine.get().makeTexture("res/ball.png");
		Ball ball = new Ball(
			genID(),
			new Vec3f(0.f, 1.f, 0.f),
			.5f,	//radius
			RenderEngine.get().getTexture(texID)
		);
		//add(ball);
		
		texID = RenderEngine.get().makeTexture("res/player.png", 8, 20);
		NetworkPlayer netPlayer = new NetworkPlayer(
			genID(),
			new Vec3f(-3.f, .5f, 0.f),
			new Quaternion(axis, 0 * (float)Math.PI / 4),
			RenderEngine.get().getTexture(texID)
		);
		add(netPlayer);
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
	
	public int genID() {
		if(freeIds.isEmpty()) {
			return nextID++; 
		} else {
			return freeIds.remove();
		}
	}
	
	public void freeID(int id) {
		freeIds.add(id);
	}
	
	public int requestID(int id) {
		//IDs we skipped over should be added to the queue
		while(nextID <= id) {
			freeIds.add(nextID++);
		}
		
		//Otherwise try to remove from the queue (a little inefficient but hopefully not too bad
		if(freeIds.remove(id)) {
			return id;
		} else {
			System.out.println("ERROR: Tried to reuse id " + id + "! (replacing with id " + nextID + ")");
			return nextID++;
		}
	}
	
	public Frame getWindow() { return window; }

	private boolean isRunning = true;
	private Frame window;
	private List<GameObject> objs = new ArrayList();
	private List<GameObject> objsToAdd = new ArrayList();
	private List<GameObject> objsToRemove = new ArrayList();
	private Queue<Integer> freeIds = new LinkedList();
	private int nextID = 0;
	
	private static GameEngine instance = null;
}
