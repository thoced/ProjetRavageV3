package coreEvent;

import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

public interface IEventCallBack 
{
	public boolean onMouse(MouseEvent buttonEvent);
	public boolean onKeyboard(KeyEvent keyboardEvent);
	public boolean onMouseMove(MouseEvent event);
	public boolean onMousePressed(MouseButtonEvent event);
	public boolean onMouseReleased(MouseButtonEvent event);
	
}
