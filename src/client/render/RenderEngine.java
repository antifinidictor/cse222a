package client.render;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import shared.Box;
import shared.Vec3f;

import client.physics.PhysicsModel;

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
        GLCanvas canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        
		frame = new Frame("CSE 222A Wi14 Project");	//This title may not stick
		frame.setSize(640, 480);
		frame.add(canvas);
		frame.setVisible(true);

        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        animator = new FPSAnimator(canvas, 60);
        animator.start();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		render(drawable);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
	    GL2 gl = drawable.getGL().getGL2();
	    
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set background color to black and opaque
	    gl.glClearDepth(1.0f);                   // Set background depth to farthest
	    gl.glEnable(GL.GL_DEPTH_TEST);   // Enable depth testing for z-culling
	    gl.glDepthFunc(GL.GL_LEQUAL);    // Set the type of depth-test
	    gl.glEnable(GL.GL_TEXTURE_2D);
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

	public void setFrameTitle(String title) {
		frame.setTitle(title);
	}
	
	public static RenderEngine get() {
		if(instance == null) {
			instance = new RenderEngine();
		}
		return instance;
	}
	
	//TODO: Test variables.  Remove them
	private static float z = 0.f;
	private HmapRenderModel hrm = null;
	private PhysicsModel pmdl = null;
	
	private void render(GLAutoDrawable drawable) {
	    GL2 gl = drawable.getGL().getGL2();
	    gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
	    
	    //TODO: TEST- REMOVE!
	    if(hrm == null) {
	    	int texID = makeTexture("res/grass.jpg");
	    	pmdl = new PhysicsModel(new Vec3f(0.f, 0.f, 0.f));
	    	hrm = new HmapRenderModel("res/hmap.jpg", textures.get(texID), pmdl, new Box(-1, 0, -1, 2, 1, 2));
	    	models.add(hrm);
	    } else {
	    	pmdl.moveBy(new Vec3f(0.f, 0.f, -0.01f));
	    }
	    //TODO: END TEST
	    
	    //Render objects
	    for(RenderModel rmodel : models) {
	    	rmodel.render(drawable);
	    }
	    
	    /*
	    //TODO: Test.  Remove it.
	    // draw a triangle filling the window
	    gl.glBegin(GL.GL_TRIANGLES);
		    gl.glColor3f(1, 0, 0);
		    gl.glVertex3f(-1, -1, z);
		    
		    gl.glColor3f(0, 1, 0);
		    gl.glVertex3f(0, 1, z);
		    
		    gl.glColor3f(0, 0, 1);
		    gl.glVertex3f(1, -1, z);
	    gl.glEnd();
	    z -= 0.01f;
	    */
	    
	}
	
	public int makeTexture(String filename) {
		return makeTexture(filename, 1, 1);
	}
	
	public int makeTexture(String filename, int framesWide, int framesHigh) {
		//Return the id of this texture
		int texID = -1;
		try {
			Texture tex = TextureIO.newTexture(new File(filename), false);
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
	
	private Frame frame = null;
	private FPSAnimator animator;
	private List<RenderModel> models;
	private List<TextureInfo> textures;		//OpenGL textures are stored by ID
	private Queue<String> texsToMake;	//Textures waiting to be made, since GLs exist only in the callbacks
	private int width, height;		//Window width and height
	
	private static RenderEngine instance = new RenderEngine();

}
