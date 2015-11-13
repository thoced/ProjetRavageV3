package coreDrawable;

import java.util.Collection;
import java.util.List;

import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import coreDrawable.FogManager.IFogVector;
import coreEntity.UnityBaseController;
import ravage.IBaseRavage;

public class ForegroundEffectManager extends Thread implements IBaseRavage,Drawable 
{
	// Time to sleep
	private final int TIME_TO_SLEEP = 125;
	// back Buffer Render Texture 01
	private RenderTexture m_backBufferRender01;
	// back Buffer Render Texture 02
	private RenderTexture m_backBufferRender02;
	// back Buffer current Render;
	private RenderTexture m_backBufferCurrentRender;
	// back Buffer current Render;
	private RenderTexture m_backBufferReturn;
	
	
	// is flip fog
	private boolean m_isFlip = false;
	
	// Color Back Buffer
	private Color m_color = new Color(255,255,255,255);

	// Sprite d'afficahge
	private Sprite m_sprite;
	
	
	// object lock 
	private Object m_lock;
	
	// callback fog
	private IFogVector m_callBackFog;
	// CircleShape
	private CircleShape m_shape;

	// renderstate
	private RenderStates m_state;
	private RenderStates m_stateSprite;

	
	public ForegroundEffectManager(Vector2i sizeMap,IFogVector callBack) throws TextureCreationException,NullPointerException
	{
		
		// callback
		if(callBack == null)
			throw new NullPointerException();
		
		// callBackFog - objet où se trouve la liste des unités
		m_callBackFog = callBack;
		
		// Buffers Render 01
		m_backBufferRender01 = new RenderTexture();
		m_backBufferRender01.create(sizeMap.x * 4, sizeMap.y * 4 );
		// Buffers Render 02
		m_backBufferRender02 = new RenderTexture();
		m_backBufferRender02.create(sizeMap.x * 4, sizeMap.y * 4);
			
		// Color gris
		m_backBufferRender01.clear(m_color);
		m_backBufferRender02.clear(m_color);
		// objet lock
		m_lock = new Object();
				
		m_backBufferCurrentRender = m_backBufferRender01;
		m_backBufferReturn = m_backBufferRender02;
		// instance de circleshape
		m_shape = new CircleShape();
		m_shape.setFillColor(new Color(255,255,255,96));
		//m_shape.setOutlineColor(new Color(255,255,255,230));
		m_shape.setOutlineThickness(0f);
		
				// sprite
		m_sprite = new Sprite();
		m_sprite.setTexture(m_backBufferReturn.getTexture());
		// taille dus prite
		m_sprite.setOrigin(new Vector2f(0f,0f));
		m_sprite.setPosition(new Vector2f(0f,0f));
		m_sprite.setScale(new Vector2f(4f,4f));
		m_sprite.setTextureRect(new IntRect(0,0,sizeMap.x * 4,sizeMap.y * 4));
		

		
		// state
		m_state = new RenderStates(BlendMode.MULTIPLY);
		m_stateSprite = new RenderStates(BlendMode.NONE);
		
		
	
		
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		super.run();
		
		try
		{
			while(this.isAlive()) // temps que le thread est en vie, on continue
			{
				// on clear le current render
				m_backBufferCurrentRender.clear(m_color);
			
								// on fait dormir le thread pendant 250 ms
				Thread.sleep(TIME_TO_SLEEP);
				// le thread se réveille
				// récupération des objets (unités)
				Collection<UnityBaseController> objs = m_callBackFog.getObjectFog();
				for(UnityBaseController o : objs)
				{
					// on dessine dans 	le back buffer current render (Fog)
					m_shape.setRadius(10f * 4);
					m_shape.setOrigin(new Vector2f(10f * 4,10f * 4));
					m_shape.setPosition(new Vector2f(o.getModel().getPositionNode().x * 4,o.getModel().getPositionNode().y * 4));
					m_backBufferCurrentRender.draw(m_shape,m_stateSprite);
				
				}
				
				// display
				m_backBufferCurrentRender.display();
								
				// on inverse le back buffer return texture (fog et foreground)
				synchronized(m_lock)
				{
					
					if(m_backBufferReturn == m_backBufferRender01)
					{
						m_backBufferReturn = m_backBufferRender02;
						
					}
					else
					{
						m_backBufferReturn = m_backBufferRender01;
						
					}
							
				}
				
				// on indique que la texture peut être flipée dans le sprite
				m_isFlip = true;
				
				
				// on inverse le back buffer render
				if(m_backBufferCurrentRender == m_backBufferRender01)
				{
					m_backBufferCurrentRender = m_backBufferRender02;
					
				}
				else
				{
					m_backBufferCurrentRender = m_backBufferRender01;
					
				}
				
			
			}
			
			
			
		} catch (InterruptedException e) 
		{
			
			e.printStackTrace();
		}
	}



	@Override
	public void init() 
	{
		// lancement du thread
		this.start();

	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		if(m_isFlip)
		{
			synchronized(m_lock)
			{
				m_sprite.setTexture(m_backBufferReturn.getTexture());
			}
			m_isFlip = false;
		}
		
		arg0.draw(m_sprite,m_state);
		
	}
	
	

}
