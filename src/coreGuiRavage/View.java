package coreGuiRavage;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

public abstract class View implements Drawable
{
	// RenderWindow
	protected RenderTexture m_render;
	// Tecture du m_render
	protected Sprite m_spriteRender;
	// origin du centre de l'image
	protected Vector2f m_origin;


	
	
	/**
	 * @return the m_render
	 */
	public RenderTexture getM_render() {
		return m_render;
	}

	/**
	 * @param m_render the m_render to set
	 */
	public void setM_render(RenderTexture m_render) {
		this.m_render = m_render;
	}



	public Vector2f getM_origin() {
		return m_origin;
	}



	public void setM_origin(Vector2f m_origin) {
		this.m_origin = m_origin;
	}



	@Override
	public abstract void draw(RenderTarget render, RenderStates state); 

}
