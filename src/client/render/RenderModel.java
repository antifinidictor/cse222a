package client.render;

import javax.media.opengl.GLAutoDrawable;

import client.GameObject;

/**
 * Interface defining basic render functionality
 * @author Nathan Heisey
 *
 */
public interface RenderModel {
	public void render(GLAutoDrawable drawable);

	GameObject getParent();
}
