package coreCamera;

import org.jsfml.graphics.ConstView;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import coreEvent.IEventCallBack;
import ravage.FrameWork;
import ravage.IBaseRavage;

public class CameraManager implements IBaseRavage, IEventCallBack
{

	// cadre 
	private IntRect m_cadre;
	// espaceScroll
	private final int m_spaceScrolling = 16;
	// View
	private static View m_view;
	// rotation
	private float m_rot = 0f;
	// speed
	private  float m_speed = 1280f;
	// Centre solllicité
	private static Vector2f m_centerSought;
	// Centre 
	private static Vector2f m_center;
	// Zoom 
	private float m_zoom;
	// Zoom sollicité
	private float m_zoomSought;
	
	// centrer sauvergadé
	private static Vector2f m_centerBackup;
	
	// size
	private static Vector2f m_sizeInit;
	private static Vector2f m_size;
	private static Vector2f m_sizeSought;
	private static Vector2f m_sizeNormal;
	private static Vector2f m_sizeDown;
	private static Vector2f m_sizeUp;
	private static Vector2f m_sizeBackup;
	// value lerp size et scrolling
	private final float LERP_SIZE 		= 0.02f;
	private final float LERP_SCROLLING 	= 0.05f;
	
	// active fly mode
	private static boolean activeFlyMode = false;
	// active camera
	private static boolean m_active = true;
	
	
	public CameraManager(ConstView v)
	{
		// initialisation de la view
		this.setView((View)v);
	}
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		this.m_view.setCenter(new Vector2f(1024f,1024f));
		// initilisation de m_centerSought
		this.m_centerSought = this.m_view.getCenter();
		// zoom
		this.m_zoom = this.m_zoomSought = 1f;		
		// size
		this.m_sizeNormal = m_size = m_sizeSought = m_sizeBackup = m_sizeInit =  this.getView().getSize();
		this.m_sizeDown = Vector2f.mul(this.m_sizeNormal, 0.8f);
		this.m_sizeUp = Vector2f.mul(m_sizeNormal, 1.2f);
		
		// cadre
		m_cadre = new IntRect(m_spaceScrolling,m_spaceScrolling,(int)this.getView().getSize().x - m_spaceScrolling * 2,(int) this.getView().getSize().y - m_spaceScrolling * 2);
		
	}
	
	public static void activeCamera(boolean active)
	{
		// set active camera
		m_active = active;
	}

	@Override
	public void update(Time deltaTime) 
	{
		
		// center
		this.m_center = this.m_view.getCenter();
		// crÃ©ation de la valeur de multiplication
		float mul = this.m_speed * deltaTime.asSeconds();
		// positionnement du m_zooSought à 1.0f;
		this.m_zoomSought = 1f;
			
		if(Keyboard.isKeyPressed(Key.RIGHT))
			 this.m_centerSought = Vector2f.add(this.m_centerSought, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Keyboard.isKeyPressed(Key.LEFT))
			 this.m_centerSought = Vector2f.sub(this.m_centerSought, Vector2f.mul(new Vector2f(1f,0f), mul));
		if(Keyboard.isKeyPressed(Key.DOWN))
			 this.m_centerSought = Vector2f.add(this.m_centerSought, Vector2f.mul(new Vector2f(0f,1f), mul));
		if(Keyboard.isKeyPressed(Key.UP))
			 this.m_centerSought = Vector2f.sub(this.m_centerSought, Vector2f.mul(new Vector2f(0f,1f), mul));
	
		
		// SCROLLING souris avec LERP
		
		if(m_active && !activeFlyMode)
		{
			
			if(m_cadre.contains(Mouse.getPosition(FrameWork.getWindow()))) // si la souris est dans le cadre intérieur
			{
				this.m_sizeSought = this.m_sizeNormal;
				
			}
			else
			{
				// si la souris est à l'extérieur du cadre intérieur
				
				if(Mouse.getPosition(FrameWork.getWindow()).x > m_cadre.left + m_cadre.width)
				{
					 this.m_centerSought = Vector2f.add(this.m_centerSought, Vector2f.mul(new Vector2f(1f,0f), mul));
					 this.m_sizeSought = this.m_sizeUp;
					
				}
				
				if(Mouse.getPosition(FrameWork.getWindow()).x < m_cadre.left)
				{
					this.m_centerSought = Vector2f.sub(this.m_centerSought, Vector2f.mul(new Vector2f(1f,0f), mul));
					this.m_sizeSought = this.m_sizeUp;
					
				}
				
				if(Mouse.getPosition(FrameWork.getWindow()).y > m_cadre.top + m_cadre.height)
				{
					this.m_centerSought = Vector2f.add(this.m_centerSought, Vector2f.mul(new Vector2f(0f,1f), mul));
					this.m_sizeSought = this.m_sizeUp;
					
				}
				
				if(Mouse.getPosition(FrameWork.getWindow()).y < m_cadre.top)
				{
					this.m_centerSought = Vector2f.sub(this.m_centerSought, Vector2f.mul(new Vector2f(0f,1f), mul));
					this.m_sizeSought = this.m_sizeUp;
					
				}
			}
			
			
		
		}
		
		// mise à jour du m_center avec la fonction lerp
		this.m_center = this.lerp(LERP_SCROLLING, this.m_center, this.m_centerSought);
		// mise à jour du size (zoom)
		this.m_size = this.lerp(LERP_SIZE, this.m_size, this.m_sizeSought);
		
		// mise à jour du zoom
		this.m_view.setSize(m_size);
		// compute du View
		this.m_view.setCenter(this.m_center);
	}
	
	protected Vector2f lerp(float value, Vector2f start, Vector2f end)
	{
	  //  return start + (end - start) * value;
	    return Vector2f.add(Vector2f.mul(Vector2f.sub(end,start),value),start);
	    
	}
	
	protected float lerp(float value, float start, float end)
	{
		return start + (end - start) * value;
	}

	

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return m_view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView( View view) {
		this.m_view = view;
	}
	
	public static FloatRect getCameraBounds()
	{
		
		Vector2f  size = CameraManager.m_view.getSize();
		/*Vector2f centre = CameraManager.view.getCenter();
		Vector2f source = Vector2f.sub(CameraManager.view.getCenter(), Vector2f.div(size,2));*/
		return  new FloatRect(Vector2f.sub(CameraManager.m_view.getCenter(), Vector2f.div(size,2)),size);
		

	}
	
	public static void activeFlyView()
	{
		if(!activeFlyMode)
		{
		    // backup du centre
			m_centerBackup = m_center;
			// backup du size
			m_sizeBackup = m_size;
			// on positionne le centre au milieu de la map
			m_centerSought = new Vector2f(3000f,2000f);
			// on monte la hauteur
			m_sizeSought = new Vector2f(6000f,4000f);
			m_sizeNormal = m_sizeSought;
			// activation du fly mode
			activeFlyMode = true;
		}
		else
		{
			m_sizeNormal = m_sizeInit;
			// on bascule le backup
			m_centerSought = m_centerBackup;
			// on bascule la taille
			m_sizeSought = m_sizeBackup;
			// desactivatino du fly mode
			activeFlyMode = false;
		}
	
		
		
		
	}

	

	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) 
	{
		// TODO Auto-generated method stub
		if(keyboardEvent.key == Keyboard.Key.C )
		{
			this.m_centerSought = new Vector2f(640,640);
			return true;
			
		}
		
		// 
		if(keyboardEvent.key == Keyboard.Key.V)
		{
			activeFlyView();
			return true;
		}
		
		return false;
		
	}

	@Override
	public boolean onMouse(MouseEvent buttonEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseMove(MouseEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMousePressed(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseReleased(MouseButtonEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


	

	

	
	

}
