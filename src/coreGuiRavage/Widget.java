package coreGuiRavage;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.KeyEvent;

public abstract class Widget extends Gui 
{

	public abstract boolean onMouse(Vector2f position, Mouse.Button mouseType);

	
	public abstract boolean onKeyboard(KeyEvent keyboardEvent);


	public abstract boolean onMouseMove(Vector2f position); 
	

	
	public abstract boolean onMousePressed(Vector2f position, Mouse.Button mouseType); 

	
	public abstract boolean onMouseReleased(Vector2f position, Mouse.Button mouseType);


	
	
}
