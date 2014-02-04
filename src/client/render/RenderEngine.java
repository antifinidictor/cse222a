package client.render;

import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import shared.Box;
import shared.Vec3f;
import client.GameEngine;
import client.Player;
import client.physics.PhysicsModel;

import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class RenderEngine implements GLEventListener {
	
	
	private RenderEngine() {
		//RenderEngine setup
		models = new ArrayList();
		textures = new ArrayList();
		
		//OpenGL setup
		GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        
        Frame window = GameEngine.get().getWindow();
        window.add(canvas);
		window.setSize(640, 480);
		window.setVisible(true);

		canvas.addFocusListener(
			new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
					GameEngine.get().getWindow().requestFocus();
				}

				@Override
				public void focusLost(FocusEvent arg0) {
				}
				
			}
		);
        
        animator = new FPSAnimator(canvas, 60);
        animator.start();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		render(drawable);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("Disposing RenderEngine");
	    GL2 gl = drawable.getGL().getGL2();
		for(TextureInfo tex : textures) {
			tex.getTexture().destroy(gl);
		}
		textures.clear();
		models.clear();
		System.out.println("RenderEngine disposed");
	}

	@Override
	public void init(GLAutoDrawable drawable) {
	    GL2 gl = drawable.getGL().getGL2();

	    gl.glEnable(GL.GL_TEXTURE_2D);
	    gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);	// Set background color to black and opaque
	    gl.glClearDepth(1.0f);						// Set background depth to farthest
	    gl.glEnable(GL.GL_DEPTH_TEST);				// Enable depth testing for z-culling
	    gl.glDepthFunc(GL.GL_LEQUAL);				// Set the type of depth-test
	    gl.glShadeModel(GL2.GL_SMOOTH);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		//Resize the OpenGL context on a window resize
	    GL2 gl = drawable.getGL().getGL2();
	    GLU glu = GLU.createGLU(gl);
	    
	    // Compute aspect ratio of the new window
	    if (height == 0) height = 1;                // To prevent divide by 0
	    this.width = width;
	    this.height = height;

	    // Set the viewport to cover the new window
	    gl.glViewport(0, 0, width, height);

	    // Set the aspect ratio of the clipping volume to match the viewport
	    gl.glMatrixMode(gl.GL_PROJECTION);
	    gl.glLoadIdentity();
        
        // Enable perspective projection with fovy, aspect, zNear and zFar
        float aspect = (float)width / (float)height;
        glu.gluPerspective(45.0f, aspect, 0.1f, 10.f);
        glu.destroy();
	}

	public static RenderEngine get() {
		return instance;
	}
	
	public static void init() {
		instance = new RenderEngine();
	}
	
	
	//TODO: Test variables.  Remove them
	private static float z = 0.f;
	private HmapRenderModel hrm = null;
	private PhysicsModel pmdl = null;
	private Player player = null;
	
	private void render(GLAutoDrawable drawable) {
	    GL2 gl = drawable.getGL().getGL2();
	    gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
	    GLU glu = GLU.createGLU(gl);
	    
	    if(camera != null) {
	    	gl.glPushMatrix();
	    	Vec3f eye = camera.loc();
	    	Vec3f lookAt = camera.lookAt();
	    	glu.gluLookAt(
    			eye.x(), eye.y(), eye.z(),			//Camera position
    			lookAt.x(), lookAt.y(), lookAt.z(),	//Look at this position
    			0.f, 1.f, 0.f						//Up vector
			);
	    }
	    
	    //TODO: TEST- REMOVE!
	    if(hrm == null) {
	    	int texID = makeTexture("res/grass.png");
	    	pmdl = new PhysicsModel(new Vec3f(0.f, 0.f, -3.f), new Quaternion());
	    	hrm = new HmapRenderModel("res/hmap.jpg", textures.get(texID), pmdl, new Box(-5, 0, -5, 10, 1, 10));
	    	models.add(hrm);
	    	float [] axis = {0.f, 1.f, 0.f};
	    	//FullCamera cam = new FullCamera(new Vec3f(0.f, 0.f, 0.f), new Quaternion(axis, 0.f));
	    	//setCamera(cam);
	    	player = new Player(new Vec3f(0.f, 0.f, 0.f), new Quaternion(axis, 0.f));
	    } else {
	    	//pmdl.moveBy(new Vec3f(0.f, 0.f, -0.01f));
	    	float[] axis = {0.f, 1.f, 0.f};
	    	//pmdl.rotateBy(new Quaternion(axis, (float) (Math.PI / 100.f)));
	    	//camera.rotateBy(new Quaternion(axis, (float) (Math.PI / 250.f)));
	    	//camera.moveRelative(new Vec3f(0.f, 0.f, -0.01f));
	    	player.onUpdate();
	    }
	    //TODO: END TEST
	    
	    //Render objects
	    for(RenderModel rmodel : models) {
	    	rmodel.render(drawable);
	    }

	    if(camera != null) {
	    	gl.glPopMatrix();
	    }
	    
	}
	
	public int makeTexture(String filename) {
		return makeTexture(filename, 1, 1);
	}
	
	public int makeTexture(String filename, int framesWide, int framesHigh) {
		//Return the id of this texture
		int texID = -1;
		try {
			GL gl = GLContext.getCurrentGL();
			Texture tex = TextureIO.newTexture(new File(filename), true);
			tex.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			tex.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			
			textures.add(new TextureInfo(tex, framesWide, framesHigh));
			texID = textures.size() - 1;
		} catch (GLException e) {
			System.out.println("Failed to create texture " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to open image file " + filename);
			e.printStackTrace();
		}
		return texID;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public GLCanvas getCanvas() { return canvas; }
	
	public void kill() {
		animator.stop();
	}
	
	private FPSAnimator animator = null;
	private List<RenderModel> models = null;
	private List<TextureInfo> textures = null;	//OpenGL textures are stored by ID
	private int width = 0, height = 0;			//Window width and height
	private Camera camera = null;
	private GLCanvas canvas;
	
	private static RenderEngine instance;

}
