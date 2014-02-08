package client.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import shared.Box;
import shared.Positionable;
import shared.Vec3f;
import client.physics.HmapCollisionModel;

public class HmapRenderModel implements RenderModel {
	
	public HmapRenderModel(String filename, TextureInfo tex, Positionable myPos, Box bounds) {
		this.myPos = myPos;
		this.bounds = bounds;
		this.tex = tex;
		
		//Read in heightmap data
		BufferedImage img = null;
		try {
			//Read the image.  This might throw an exception
		    img = ImageIO.read(new File(filename));
		    
		    //Copy image data into the heightmap
		    int w = img.getWidth();
		    int h = img.getHeight();
			hmap = new float[w][h];
			for(int i = 0; i < w; ++i) {
				for(int j = 0; j < h; ++j) {
					//Convert from RGB color to heightmap value.  Max RGB color is 0xFFFFFF if no alpha channel
					hmap[i][j] = img.getRGB(i, j) * bounds.height() / 0x00FFFFFF + bounds.height();
				}
			}
		} catch (IOException e) {
			System.err.println("Failed to load heightmap from " + filename);
			System.err.println(e);
			hmap = new float[DEFAULT_HMAP_SIZE][DEFAULT_HMAP_SIZE];
		}
	}
	
	public float[][] getHmap() { return hmap; }

	@Override
	public void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		//Translation to rotation center
		gl.glPushMatrix();
		gl.glTranslatef(
			myPos.loc().x(),
			myPos.loc().y(),
			myPos.loc().z()
		);
		
		//Rotation
		gl.glMultMatrixf(myPos.ori().toMatrix(), 0);
		
		//Translation to hmap corner
		gl.glTranslatef(
			bounds.min.x(),
			bounds.min.y(),
			bounds.min.z()
		);
		
		//Color?
	    //gl.glColor4f(1.f, 1.f, 1.f, 0.f);
		gl.glColor3f(1.f, 1.f, 1.f);
	    tex.getTexture().bind(gl);

	    float w = bounds.width() / (hmap.length - 1);		//x resolution (width of one unit)
	    float l = bounds.length() / (hmap[0].length - 1);	//z resolution (length of one unit)
	    for(int x = 0; x < hmap.length - 1; ++x) {
	        gl.glBegin(gl.GL_TRIANGLE_STRIP);
            
	        for(int z = 0; z < hmap[0].length; z++) {
	            float y0 = hmap[x][z];
	            float y1 = hmap[x+1][z];
	            
	            gl.glTexCoord2f(x * w, z * l);
	            gl.glVertex3f(x * w, y0, z * l);

	            gl.glTexCoord2f(x * w + w, z * l);
	            gl.glVertex3f(x * w + w, y1, z * l);
	        }
	        gl.glEnd();
	    }
	    /*    
	    //Draw collision box
	    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
	    gl.glBegin(GL.GL_LINE_LOOP);
        	gl.glVertex3f(0.f,            0.f,             0.f);
        	gl.glVertex3f(bounds.width(), 0.f,             0.f);
        	gl.glVertex3f(bounds.width(), 0.f,             bounds.length());
        	gl.glVertex3f(0.f,            0.f,             bounds.length());
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINE_LOOP);
	    	gl.glVertex3f(0.f,            bounds.height(), 0.f);
	    	gl.glVertex3f(bounds.width(), bounds.height(), 0.f);
	    	gl.glVertex3f(bounds.width(), bounds.height(), bounds.length());
	    	gl.glVertex3f(0.f,            bounds.height(), bounds.length());
	    gl.glEnd();
	    gl.glBegin(GL.GL_LINES);
		    gl.glVertex3f(0.f,            0.f,             0.f);
	    	gl.glVertex3f(0.f,            bounds.height(), 0.f);

        	gl.glVertex3f(bounds.width(), 0.f,             0.f);
	    	gl.glVertex3f(bounds.width(), bounds.height(), 0.f);

        	gl.glVertex3f(bounds.width(), 0.f,             bounds.length());
	    	gl.glVertex3f(bounds.width(), bounds.height(), bounds.length());

        	gl.glVertex3f(0.f,            0.f,             bounds.length());
	    	gl.glVertex3f(0.f,            bounds.height(), bounds.length());
	    gl.glEnd();
	    */
	    //TODO: Test drawing surface normals- remove.
	    if(temp != null) {
	    	if(shift >= w) {
	    		shift = -w;
	    	} else {
	    		shift += 0.001f;
	    	}
	    	float xStart = w / 2.f + shift;
	    	float zStart = l / 2.f;
	    	Vec3f drawLoc = new Vec3f(0.f, 0.f, 0.f);
	    	Vec3f testLoc = new Vec3f(0.f, 0.f, 0.f);
			gl.glLineWidth(5.f);
			gl.glBegin(GL.GL_LINES);
    		for(int x = 0; x < hmap.length - 1; ++x) {
	        	testLoc.x(xStart + x * w + myPos.loc().x() + bounds.min.x());
    	        for(int z = 0; z < hmap[0].length; z++) {
    	        	testLoc.z(zStart + z * l + myPos.loc().z() + bounds.min.z());
	        		float y = temp.getHeightAt(testLoc) + bounds.height() / 2.f;
	        		Vec3f normal = temp.getNormalAt(testLoc);
	        		normal.normalizeTo(-.25f);
        			drawLoc.x(xStart + x * w);
	        		drawLoc.z(zStart + z * l);
	        		drawLoc.y(y);

	        		gl.glColor3f(1.f, 0.f, 0.f);
	        		gl.glVertex3f(drawLoc.x(), drawLoc.y(), drawLoc.z());
	        		
	        		
	        		drawLoc.add(normal);
	        		gl.glColor3f(0.f, 0.f, 1.f);
	        		gl.glVertex3f(drawLoc.x(), drawLoc.y(), drawLoc.z());
    	        }
    		}
    		gl.glEnd();
    		gl.glColor3f(1.f, 1.f, 1.f);
	    }
	    
		//Undo matrix changes made for this object
		gl.glPopMatrix();
	}
	
	
	//TODO: Test drawing surface normals- remove.
	int timer = 0;
	float shift = 0.f;
	final int MAX_TIME = 100;
	public HmapCollisionModel temp = null;

	private Positionable myPos;
	private float hmap[][];
	private Box bounds;
	private TextureInfo tex;
	static final int DEFAULT_HMAP_SIZE = 10;
}
