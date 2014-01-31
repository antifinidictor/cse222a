package client.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import shared.Box;
import shared.Locatable;
import shared.Vec3f;

public class HmapRenderModel implements RenderModel {
	
	public HmapRenderModel(String filename, TextureInfo tex, Locatable myPos, Box bounds) {
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
					hmap[i][j] = img.getRGB(i, j) * bounds.height() / 0xFFFFFF;
				}
			}
		} catch (IOException e) {
			System.err.println("Failed to load heightmap from " + filename);
			System.err.println(e);
			hmap = new float[DEFAULT_HMAP_SIZE][DEFAULT_HMAP_SIZE];
		}
	}

	@Override
	public void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		//Translation, rotation
		gl.glPushMatrix();
		gl.glTranslatef(
			myPos.pos().x + bounds.min.x,
			myPos.pos().y + bounds.min.y,
			myPos.pos().z + bounds.min.z
		);
		//insert rotation here
		
		//TODO: Figure out color and texture stuff
	    gl.glColor4f(1.f, 1.f, 1.f, 0.f);
	    tex.getTexture().bind(gl);
	    //gl.glBindTexture(tex.getTexture().getTarget(), tex.getTexture().getTextureObject());

	    float w = bounds.width() / (hmap.length - 1);  //x resolution (width of one unit)
	    float l = bounds.length() / (hmap[0].length - 1);  //z resolution (length of one unit)
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
		
		//Undo matrix changes made for this object
		gl.glPopMatrix();
	}

	private Locatable myPos;
	private float hmap[][];
	private Box bounds;
	private TextureInfo tex;
	static final int DEFAULT_HMAP_SIZE = 10;
}
