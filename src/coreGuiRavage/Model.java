package coreGuiRavage;

import org.jsfml.system.Vector2f;

public class Model 
{
	// est ton pressé
	protected boolean m_isPressed;
	// position absolue 
	protected Vector2f m_position;
		// size 
	protected Vector2f m_size;
	
	
	
	
	public Model(Vector2f position,Vector2f size)
	{
		super();
		m_position = position;
		m_size = size;
	}

	public boolean isPressed() {
		return m_isPressed;
	}

	public void setPressed(boolean m_isPressed) {
		this.m_isPressed = m_isPressed;
	}

	
	
	
}
