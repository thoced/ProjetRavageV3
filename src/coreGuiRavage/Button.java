package coreGuiRavage;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.KeyEvent;


public class Button extends Widget 
{

	public Button(Vector2f position,Vector2f size) 
	{
		super();
		
		this.setM_model(new ButtonModel(position,size));
		this.setM_view(new ButtonView());
		
		
	}
	
	public void setAction(String action)
	{
		// set l'action
		((ButtonModel)this.m_model).m_action = action;
	}
	
	public void setTexture(Texture texture)
	{
		// set texture
		if(texture != null)
		{
			Sprite sprite = ((ButtonView)m_view).m_spriteImage;
			sprite.setTexture(texture);
			sprite.setTextureRect(new IntRect(0,0,texture.getSize().x,texture.getSize().y));
			sprite.setOrigin(new Vector2f(sprite.getTexture().getSize().x * 0.5f,sprite.getTexture().getSize().y * 0.5f)); // origin = centre de la texture
		}
		
	}
	
	public void addListener(IButtonListener listener)
	{
		((ButtonModel)m_model).m_buttonListeners.add(listener);
	}
	
	@Override
	public boolean onMouse(Vector2f position,
			org.jsfml.window.Mouse.Button mouseType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyboard(KeyEvent keyboardEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseMove(Vector2f position) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onMouseReleased(Vector2f position,
			org.jsfml.window.Mouse.Button mouseType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMousePressed(Vector2f position,
			org.jsfml.window.Mouse.Button mouseType) {
		// si le clic est dans le bound, on lance l'action
		if(((ButtonModel)this.getM_model()).m_bound.contains(position))
		{
			// on boucle sur la liste des listeners
			for(IButtonListener listener : ((ButtonModel)this.m_model).m_buttonListeners)
			{
				listener.actionListener(((ButtonModel)this.m_model).m_action, this); // appel de l'action listener interface
			}
			return true;
		}
				
		return false;
	}


	class ButtonModel  extends Model
	{
		public List<IButtonListener> m_buttonListeners;
		// bounds
		public FloatRect m_bound;
		// action
		public String m_action;
		
		public ButtonModel(Vector2f position, Vector2f size)
		{
			super(position, size);
			// instance de m_buttonListeners
			m_buttonListeners = new ArrayList<IButtonListener>();
			// bounds
			m_bound = new FloatRect(position,size);
		}
		
	}
	
	class ButtonView extends View
	{
		
		// parent controller
		public Button m_controller;
		// shape
		public RectangleShape m_shape;
		// Color outline
		public Color m_colorOutLine;
		// Sprite pour l'image à ajouter sur le bouton
		public Sprite m_spriteImage;
		
		

		public ButtonView() 
		{
			super();
			m_controller = Button.this;
			// Color Outline
			m_colorOutLine = new Color(96,96,96,128);
			// instance du shape
			m_shape = new RectangleShape();
			m_shape.setPosition(m_controller.m_model.m_position);
			m_shape.setSize(m_controller.m_model.m_size);
			m_shape.setFillColor(Color.BLACK);
			m_shape.setOutlineThickness(-2f);
			m_shape.setOutlineColor(m_colorOutLine);
			// sprite image
			m_spriteImage = new Sprite();
			m_spriteImage.setPosition(Vector2f.add(m_controller.m_model.m_position, Vector2f.div(m_controller.m_model.m_size, 2))); // position = centre du bouton
		
		}


		@Override
		public void draw(RenderTarget render, RenderStates state) 
		{
			// affichage du bouton
			render.draw(m_shape);
			// affichage de l'image sur le bouton
			render.draw(m_spriteImage);
			
		}
	
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Time deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	
}
