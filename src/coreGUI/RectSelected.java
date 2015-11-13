package coreGUI;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;

import coreEvent.IEventCallBack;
import ravage.FrameWork;
import ravage.IBaseRavage;


public class RectSelected implements IBaseRavage, Drawable,IEventCallBack
{
	
	private FloatRect boundSelected;
	
	private float x,y,width,height;
	
	private boolean isButtonPressed = false;
	
	private VertexArray buffer;
	
	private static  List<IRegionSelectedCallBack> listCallBack;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		// instance du buffer
		buffer = new VertexArray(PrimitiveType.LINE_STRIP);
		// instance du listcallback
		listCallBack = new ArrayList<IRegionSelectedCallBack>();
		
		x = 0f;
		y = 0f;
		width = 32f;
		height = 32f;
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public FloatRect getRegionSelected()
	{
		// return la region sélectionné
		return new FloatRect(x,y,width,height);
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		// TODO Auto-generated method stub
		
		buffer.clear();
		
		Vector2f p1 = new Vector2f(x,y);
		Vector2f p2 = Vector2f.add(p1, new Vector2f(width,0));
		Vector2f p3 = Vector2f.add(p1, new Vector2f(width,height));
		Vector2f p4 = Vector2f.add(p1, new Vector2f(0,height));
		
		Vertex v1 = new Vertex(p1,Color.BLUE);
		Vertex v2 = new Vertex(p2,Color.MAGENTA);
		Vertex v3 = new Vertex(p3,Color.BLUE);
		Vertex v4 = new Vertex(p4,Color.RED);
		Vertex v5 = new Vertex(p1,Color.BLUE);
		
		buffer.add(v1);
		buffer.add(v2);
		buffer.add(v3);
		buffer.add(v4);
		buffer.add(v5);
		
		arg0.draw(buffer);
		
	}

	

	

	@Override
	public boolean onMouse(org.jsfml.window.event.MouseEvent buttonEvent)
	{
		// TODO Auto-generated method stub
		Vector2f coord = FrameWork.getWindow().mapPixelToCoords(buttonEvent.position);
		
		if(this.isButtonPressed && buttonEvent != null)
		{
			width = coord.x - x;
			height = coord.y - y;
			
		}
		
		if(!this.isButtonPressed && Mouse.isButtonPressed(Button.LEFT))
		{
			x = coord.x;
			y = coord.y;
			this.isButtonPressed = true;
		}
		else
		{
			this.isButtonPressed = false;
		}
		
		return false;
	}

	@Override
	public boolean onMouseMove(org.jsfml.window.event.MouseEvent event)
	{
		Vector2f coord = FrameWork.getWindow().mapPixelToCoords(event.position);
		
		if(this.isButtonPressed)
		{
			width = coord.x - x;
			height = coord.y - y;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onMousePressed(MouseButtonEvent event) 
	{
		Vector2f coord = FrameWork.getWindow().mapPixelToCoords(event.position);
		
		if(event.button == Button.LEFT)
		{
			this.x = coord.x;
			this.y = coord.y;
			this.isButtonPressed = true;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onMouseReleased(MouseButtonEvent event) 
	{
		// TODO Auto-generated method stub
		if(event.button == Button.LEFT)
		{
			this.isButtonPressed = false;
			// on appel les callback
			this.CallBack();
			// on position le cadre en 0 pixel
			x = y = width = height = 0;
			return true;
		}
		
		return false;
	}


	public static void attachCallBack(IRegionSelectedCallBack obj) 
	{
		RectSelected.listCallBack.add( obj);
		
	}
	
	private void CallBack()
	{
		// reception de la region selectionné
		FloatRect region = this.getRegionSelected();
		for(IRegionSelectedCallBack i : this.listCallBack)
		{
			// appel aux objets attachés
			i.onRegionSelected(region);
		}
	}

	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		return false;
	}


	

	

}
