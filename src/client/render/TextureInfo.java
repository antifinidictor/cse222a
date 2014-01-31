package client.render;

import com.jogamp.opengl.util.texture.Texture;

public class TextureInfo {
	public TextureInfo(Texture texture, int framesWide, int framesHigh) {
		this.texture = texture;
		this.framesWide = framesWide;
		this.framesHigh = framesHigh;
	}
	
	public Texture getTexture() { return texture; }
	public int getFramesWide() { return framesWide; }
	public int getFramesHigh() { return framesHigh; }
	
	private Texture texture;	//OpenGL texture
	private int framesWide;		//Number of animation frames wide
	private int framesHigh;		//Number of animation frames high
	//TODO: Put texture stretching factors here?
}
