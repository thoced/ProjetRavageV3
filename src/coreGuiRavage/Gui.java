package coreGuiRavage;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import ravage.IBaseRavage;
import coreEvent.IEventCallBack;
import coreEvent.IEventInterfaceCallBack;

public abstract class Gui implements IBaseRavage
{
	protected Model m_model;   // model
	
	protected View m_view; 	   /// view
	
	protected boolean m_isMovable = true; // le gui est-il déplacable ?
	

	public Model getM_model() {
		return m_model;
	}

	public View getM_view() {
		return m_view;
	}

	public void setM_model(Model m_model) {
		this.m_model = m_model;
	}

	public void setM_view(View m_view) {
		this.m_view = m_view;
	}

	
	
	public boolean isM_isMovable() {
		return m_isMovable;
	}

	public void setM_isMovable(boolean m_isMovable) {
		this.m_isMovable = m_isMovable;
	}

	public void addWidget(Widget widget)
	{
		
	}
	
	public void removeAllWidget()
	{
		
	}
	
	public void removeWidget(Widget widget)
	{
		
	}
	

	public abstract boolean onMouse(Vector2f position, Mouse.Button mouseType);

	
	public abstract boolean onKeyboard(KeyEvent keyboardEvent);


	public abstract boolean onMouseMove(Vector2f position); 
	

	
	public abstract boolean onMousePressed(Vector2f position, Mouse.Button mouseType); 

	
	public abstract boolean onMouseReleased(Vector2f position, Mouse.Button mouseType);


}
