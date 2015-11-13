package coreEvent;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import coreCamera.CameraManager;
import ravage.IBaseRavage;

public class EventManager implements IBaseRavage 
{

	// list d'objets attachés (callback)
	private static List<IEventCallBack> listCallBack;
	// list d'ibhet attacgés (temporary)
	private static List<IEventCallBack> listCallBackTemporary;
	
	// list d'objets attachés pour les Intefaces (callback)
	private static List<IEventInterfaceCallBack> listInterfaceCallBack;
		// list d'ibhet attacgés (temporary)
	private static List<IEventInterfaceCallBack> listInterfaceCallBackTemporary;
	
	// list de suppression des objet callback
	private static List<IEventCallBack> listCallBackRemove;
	
	// variable empechant d'appler en boucle lors des clic souris
	private boolean isMousePressed = false;

	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		// instance du listcallback
		listCallBack = new ArrayList<IEventCallBack>();
		listCallBackTemporary = new ArrayList<IEventCallBack>();
		listInterfaceCallBack = new ArrayList<IEventInterfaceCallBack>();
		listInterfaceCallBackTemporary = new ArrayList<IEventInterfaceCallBack>();
		listCallBackRemove = new ArrayList<IEventCallBack>();

	}

	@Override
	public void update(Time deltaTime) 
	{
		// TODO Auto-generated method stub
		
		// transferts des objets de la liste temporary à la liste call back
		listCallBack.addAll(listCallBackTemporary);
		listCallBackTemporary.clear();
		
		// transferts des objets de la liste temporary interface vers la liste call back des interfaces
		listInterfaceCallBack.addAll(listInterfaceCallBackTemporary);
		listInterfaceCallBackTemporary.clear();
		
		// suppression des objets 
		listCallBack.removeAll(listCallBackRemove);
		listInterfaceCallBack.removeAll(listCallBackRemove);
		listCallBackRemove.clear();
	}
	
	public void catchEvent(Event event)
	{
		
		if(event.type == Event.Type.KEY_PRESSED)
			this.callOnKeyBoard(event);
		
		
		if(!this.isMousePressed && event.type == Event.Type.MOUSE_BUTTON_PRESSED)
		{
			this.callOnMousePressed(event);
			this.isMousePressed = true;
		}
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED)
		{
			this.callOnMouseReleased(event);
			this.isMousePressed = false;
		}
		if(event.type == Event.Type.MOUSE_MOVED)
			this.callOnMouseMoved(event);
			
		
	}
	
	public void callOnKeyBoard(Event  event)
	{
		// Appel dans un premier temps vers les interfaces du premier plan
		int flag = 0;  // flag placé à 0 si plus grand alors une action a été capté par l'inteface
		for(IEventInterfaceCallBack i : listInterfaceCallBack)
		{
			if(i.onKeyboard(event.asKeyEvent()) == true)
				flag++;
				
		}
		
		if(flag == 0) // si aucune action sur l'interface n'a été activée.
		{
			for(IEventCallBack i : listCallBack)
				i.onKeyboard(event.asKeyEvent());
			
			CameraManager.activeCamera(true);
		}
		else
		{
			// on désactive la camera
			CameraManager.activeCamera(false);
		}
	}
	
	public void callOnMousePressed(Event event)
	{
		// Appel dans un premier temps vers les interfaces du premier plan
		int flag = 0;  // flag placé à 0 si plus grand alors une action a été capté par l'inteface
		for(IEventInterfaceCallBack i : listInterfaceCallBack)
		{
			if(i.onMousePressed(event.asMouseButtonEvent()) == true)
				flag++;
						
		}
		
		if(flag == 0)
		{
			for(IEventCallBack i : listCallBack)
				i.onMousePressed(event.asMouseButtonEvent());
			CameraManager.activeCamera(true);
		}
		else
		{
			// on désactive la camera
			CameraManager.activeCamera(false);
		}
	}	
	
	public void callOnMouseReleased(Event event)
	{
		// Appel dans un premier temps vers les interfaces du premier plan
		int flag = 0;  // flag placé à 0 si plus grand alors une action a été capté par l'inteface
		for(IEventInterfaceCallBack i : listInterfaceCallBack)
		{
			if(i.onMouseReleased(event.asMouseButtonEvent()) == true)
				flag++;
								
		}
		
		if(flag == 0)
		{
			for(IEventCallBack i : listCallBack)
				i.onMouseReleased(event.asMouseButtonEvent());
			
			CameraManager.activeCamera(true);
		}
		else
		{
			// on désactive la camera
			CameraManager.activeCamera(false);
		}
	}
	
	public void callOnMouseMoved(Event event)
	{
		// Appel dans un premier temps vers les interfaces du premier plan
		int flag = 0;  // flag placé à 0 si plus grand alors une action a été capté par l'inteface
		for(IEventInterfaceCallBack i : listInterfaceCallBack)
		{
			if(i.onMouseMove(event.asMouseEvent()) == true)
				flag++;
										
		}
		
		if(flag == 0)
		{
			for(IEventCallBack i : listCallBack)
				i.onMouseMove(event.asMouseEvent());
			
			CameraManager.activeCamera(true);
		}
		else
		{
			// on désactive la camera
			CameraManager.activeCamera(false);
		}
	}
	
	

	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub

	}
	
	// ajout d'un callback
	public static void addCallBack(IEventCallBack i)
	{
		// ajout dans la liste call back temporary
		listCallBackTemporary.add(i);
		
		
	}
	
	public static void removeCallBack(IEventCallBack r)
	{
		// ajout dans la liste des remove
		listCallBackRemove.add(r);
	}
	
	
	// ajout d'un callback pour une interface en premier plan
	public static void addInterfaceCallBack(IEventInterfaceCallBack i)
	{
		// ajout dans la liste call back temporary
		listInterfaceCallBackTemporary.add(i);
		
		
	}
	
	public static void removeInterfaceCallBack(IEventInterfaceCallBack r)
	{
		// ajout dans la liste des remove
		listCallBackRemove.add(r);
	}

}
