package coreGuiRavage;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.KeyEvent;

public class Image extends Widget 

{
	
	public Image(Texture texture,IntRect textureRect,Vector2f position)
	{
		this.m_model = new ImageModel(position,null);
		this.m_view = new ImageView(texture,textureRect,position);
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
	public boolean onMouseMove(Vector2f position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMousePressed(Vector2f position, Button mouseType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseReleased(Vector2f position, Button mouseType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	class ImageModel extends Model
	{

		public ImageModel(Vector2f position, Vector2f size) {
			super(position, size);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public class ImageView extends View
	{
		// sprite
		public Sprite m_sprite;
				
		
		public ImageView(Texture texture,IntRect textureRect,Vector2f position) 
		{
			super();
		
			// texture spécifié
			if(texture != null)
			{
				m_sprite = new Sprite(texture);
				// position spécifiée
				m_sprite.setPosition(position);
				m_sprite.setOrigin(new Vector2f(textureRect.width * 0.5f,textureRect.height * 0.5f));
				m_sprite.setTextureRect(textureRect);
				m_sprite.setColor(Color.WHITE);
			}
			else
				m_sprite = new Sprite();
		}



		@Override
		public void draw(RenderTarget render, RenderStates state) 
		{
			// affichage
			render.draw(m_sprite);
			
		}
		
	}

}
