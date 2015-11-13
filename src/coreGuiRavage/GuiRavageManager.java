package coreGuiRavage;

import java.util.ArrayList;
import java.util.List;

import javax.json.stream.JsonParser.Event;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import ravage.IBaseRavage;
import coreEvent.IEventCallBack;
import coreEvent.IEventInterfaceCallBack;

public class GuiRavageManager implements IBaseRavage,IEventInterfaceCallBack, Drawable 
{

	private static List<Panel> m_panels = new ArrayList<Panel>();;
	
	public GuiRavageManager()
	{
		
	}
	
	public static void addPanel(Panel p)
	{
		m_panels.add(p);
	}
	
	
	
	@Override
	public void draw(RenderTarget render, RenderStates state) 
	{
		for(Panel p : m_panels)
			p.m_view.draw(render, state);
	}

	@Override
	public boolean onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseMove(MouseEvent event) 
	{
		boolean l_ret = false;
		
		for(Panel p : m_panels)
		{
			l_ret = p.onMouseMove(new Vector2f(event.position.x,event.position.y));
				if(l_ret)
					return true;
		}
		
		return l_ret;
		
		
	}

	@Override
	public boolean onMousePressed(MouseButtonEvent event) 
	{
	
		for(Panel p: m_panels)
		{
			if(p.onMousePressed(new Vector2f(event.position.x,event.position.y),event.button) == true) // un le click se trouve sur un panel, return true
				return true;
		}
		return false; // aucun clic sur un panel
	}

	@Override
	public boolean onMouseReleased(MouseButtonEvent event) 
	{
	
		for(Panel p : m_panels)
		{
			if(p.onMouseReleased(new Vector2f(event.position.x,event.position.y),event.button) == true)
				return true;
		}
		
		return false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Time deltaTime) 
	{
		for(Panel p : m_panels)
		{
			p.update(deltaTime);
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}



}
