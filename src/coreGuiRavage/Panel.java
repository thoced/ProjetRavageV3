package coreGuiRavage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.json.stream.JsonParser.Event;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event.Type;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import ravage.FrameWork;
import CoreTexturesManager.TexturesManager;

 public class Panel extends Gui 
{
	 
	// différence entre la position du panel et la position du click
    private Vector2f m_diffPosClick;
   
	
	public Panel(float x,float y,Vector2f size) throws TextureCreationException {
		super();
		// transformation en pixels
		Vector2f pos = new Vector2f((FrameWork.getWindow().getSize().x * x) - size.x * 0.5f,(FrameWork.getWindow().getSize().y * y) - size.y * 0.5f);
		// instance du model et de la vue
		this.setM_model(new PanelModel(pos,size));
		this.setM_view(new PanelView(this));
		
	}	

	
	public boolean onMouseMove(Vector2f position) 
	{
		// si le gui n'est pas dépacable
		if(!this.isM_isMovable())
			return false;
		
			boolean ret = false;
			Vector2f p = this.m_model.m_position;
			Vector2f s = this.m_model.m_size;
			FloatRect bound = new FloatRect(p,s);
			//if(bound.contains(position))
			//{
			
			if(bound.contains(position))
				ret = true;
			
			if(this.getM_model().isPressed())
			{
				this.m_model.m_position = Vector2f.sub(position, this.m_diffPosClick);
				ret = true;
					
			}
				
			return ret;
		
		
	}



	public boolean onMouseReleased(Vector2f position, Button mouseType) 
	{
		this.m_model.setPressed(false);
		return false;
	}


	@Override
	public boolean onMousePressed(Vector2f position, Mouse.Button button)
	{
		Vector2f p = this.m_model.m_position;
		Vector2f s = this.m_model.m_size;
		FloatRect bound = new FloatRect(p,s);
		if(bound.contains(position))
		{
			// on modifie la posiiton pour la transformer en position relative
			Vector2f posRelative = Vector2f.sub(position, p);
			// appel au gui
			
				 for(Gui gui : ((PanelModel)m_model).m_guis)
					 gui.onMousePressed(posRelative, button);
			
			this.getM_model().setPressed(true); // on indique au gui qu'un coli 
			this.m_diffPosClick = Vector2f.sub(position, this.m_model.m_position);
			return true;
		}
		else
		{
			this.getM_model().setPressed(false);
			return false;
		}
	}


	/* (non-Javadoc)
	 * @see coreGuiRavage.Gui#addWidget(coreGuiRavage.Widget)
	 */
	@Override
	public void addWidget(Widget widget)
	{
		// ajout du widget dans le panel
		
		((PanelModel)this.m_model).m_guis.add(widget); 
		
	}

	

	@Override
	public void removeAllWidget() {
		// TODO Auto-generated method stub
		super.removeAllWidget();
		// suppression de tous les widget
		((PanelModel)this.m_model).m_guis.clear();
	}


	@Override
	public void removeWidget(Widget widget) {
		// TODO Auto-generated method stub
		super.removeWidget(widget);
		// suppresion du widget
		((PanelModel)this.m_model).m_guis.remove(widget);
	}


	@Override
	public boolean onMouse(Vector2f position, Button mouseType) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(Time deltaTime) 
	{
		
			for(Gui g : ((PanelModel)this.m_model).m_guis)
			{
				g.update(deltaTime);
			}
		
		
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}

class PanelModel extends Model
{
	// list des gui contenu dans le panel
	//List<Gui> m_guis;
	CopyOnWriteArrayList<Gui> m_guis;
	
	
	public PanelModel(Vector2f posPanel,Vector2f size)
	{
		super(posPanel,size);
		// instance de la liste
		m_guis = new CopyOnWriteArrayList<Gui>();
	}
	
	public void add(Gui g)
	{
		m_guis.add(g);
	}

}

class PanelView extends View
{
	// controller parent
	private Panel m_controller;
	// Sprite
	private Sprite m_spritePanel;
	private RectangleShape m_rectangle;
	// IntRect de la texture
	private IntRect m_textureIntRect;
	// couleur du contour du panel
	private Color m_colorOutline;
	
	public PanelView(Panel controller) throws TextureCreationException
	{
		
		// réception du parent controller
		m_controller = controller;
		
		// création du render texture
		m_render = new RenderTexture();
		m_render.create((int)controller.m_model.m_size.x, (int)controller.m_model.m_size.y);
		// création du sprite final du render
		m_spriteRender = new Sprite(m_render.getTexture());
		
		// chargement du sprite panelBackground.png
		m_rectangle = new RectangleShape();
		m_rectangle.setTexture(TexturesManager.GetTextureByName("panelBackground.png"));
		
		// initilisaion de l'origin
		this.m_origin = new Vector2f(0f,0f);
		if(m_spritePanel != null)
			m_spritePanel.setOrigin(this.m_origin);
		
		// couleur contour
		m_colorOutline = new Color(128,128,128,128);
		// on spécifie la couleur du rectangle
		m_rectangle.setOutlineColor(m_colorOutline);
		
		
	}
	
	@Override
	public void draw(RenderTarget render, RenderStates state) 
	{
		// affichage du panel
		Vector2f l_size = m_controller.m_model.m_size;
		Vector2f l_pos = m_controller.m_model.m_position;
		
		
		// rectangle
		m_rectangle.setSize(l_size);
		m_rectangle.setPosition(new Vector2f(0f,0f));
		m_rectangle.setOutlineThickness(-6f);
				
		// affichage de la texture du panel dans le rendertexture
		m_render.clear();
		m_render.draw(m_rectangle);
		
		// affichage des gui du panels
		for(Gui g : ((PanelModel)m_controller.m_model).m_guis)
		{
			g.m_view.draw(m_render, state);
		}
		
		// on renvoie le sprite du render texture dans le render appelant
		// positionnement du m_spriteRender
		m_spriteRender.setPosition(m_controller.m_model.m_position);
		m_render.display();
		render.draw(m_spriteRender);
		
		
		
	}
	
}
