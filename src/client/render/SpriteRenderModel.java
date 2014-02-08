package client.render;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import shared.Positionable;
import shared.Vec3f;

import com.jogamp.opengl.math.Quaternion;

public class SpriteRenderModel implements RenderModel {
	
	public SpriteRenderModel(Positionable myPos, final Vec3f offset, TextureInfo tex) {
		this.myPos = myPos;
		this.offset = new Vec3f(offset);
		this.tex = tex;
		
		//If you need a different value, set it later
		this.widthScale = 1.f;
		this.heightScale = 1.f;
		this.frameW = 0;
		this.frameH = 0;
	}

	@Override
	public void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		//Determine orientation: Vector from our position to the camera's position
		Vec3f camVec = new Vec3f(RenderEngine.get().getCamera().loc());
		Vec3f loc = myPos.loc();
		camVec.sub(loc);
		camVec.y(0.f);
		camVec.normalize();
		
		float [] forwardVec = {0.f, 0.f, 1.f};
		Quaternion ori = new Quaternion(forwardVec, camVec.toVector());
		
		//Apply matrix modifications
		gl.glPushMatrix();
		gl.glTranslatef(
			loc.x(),
			loc.y(),
			loc.z()
		);
		gl.glMultMatrixf(ori.toMatrix(), 0);

		
		float frameWidth = tex.getTexture().getWidth() / tex.getFramesWide();
		float frameHeight = tex.getTexture().getWidth() / tex.getFramesWide();
		float width  = widthScale * frameWidth / frameHeight;
		float height = heightScale;
		
		gl.glTranslatef(
			offset.x() - width / 2.f,
			offset.y() - height / 2.f,
			offset.z()
		);
		
		//Texture coordinates
	    float texLeft   = (frameW * 1.0f) / tex.getFramesWide(),
	          texTop    = (frameH + 1.0f) / tex.getFramesHigh(),
	          texRight  = (frameW + 1.0f) / tex.getFramesWide(),
	          texBottom = (frameH * 1.0f) / tex.getFramesHigh();

		tex.getTexture().bind(gl);

		gl.glBegin(GL2.GL_QUADS);
	        //Top-left vertex (corner)
	        gl.glTexCoord2f(texLeft, texTop);
	        gl.glVertex3f(0.f, height, 0.f);

	        //Top-right vertex (corner)
	        gl.glTexCoord2f(texRight, texTop);
	        gl.glVertex3f(width, height, 0.f);

	        //Bottom-right vertex (corner)
	        gl.glTexCoord2f(texRight, texBottom);
	        gl.glVertex3f(width, 0.f, 0.f);

	        //Bottom-left vertex (corner)
	        gl.glTexCoord2f(texLeft, texBottom);
	        gl.glVertex3f(0.f, 0.f, 0.f);
        gl.glEnd();
        gl.glPopMatrix();
	}
	
	public void setWidthScale(float widthScale) {
		this.widthScale = widthScale;
	}

	public void setHeightScale(float heightScale) {
		this.heightScale = heightScale;
	}
	
	//Use -1 to leave the same
	public void setFrame(int frameW, int frameH) {
		if(frameW > 0) {
			this.frameW = frameW;
		}
		if(frameH > 0) {
			this.frameH = frameH;
		}
	}
	
	public TextureInfo getTexInfo() { return tex; }
	
	private int frameW, frameH;
	private Positionable myPos;
	private TextureInfo tex;
	private Vec3f offset;
	private float widthScale, heightScale;
}
