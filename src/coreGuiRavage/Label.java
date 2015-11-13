package coreGuiRavage;

import java.io.IOException;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.KeyEvent;

public class Label extends Widget
{

	public Label(Vector2f position) throws IOException
	{
		super();
		// creatin de la view
		this.setM_model(new LabelModel(position,new Vector2f(0f,0f)));
		this.setM_view(new LabelView());
		
	}
	
	public void setText(String text)
	{
		// insértion du texte
		((LabelModel)this.m_model).setM_text(text);
		// update du texte à afficher
		((LabelView)this.m_view).updateTextSprite(text);
	}
	
	public void setColor(Color color)
	{
		// couleur du label
		((LabelView)this.m_view).setColor(color);
	}
	
	
	class LabelModel extends Model
	{
		private String m_text;
		
		
		public LabelModel(Vector2f position, Vector2f size) throws IOException 
		{
			super(position, size);
			
		}

		public String getM_text() {
			return m_text;
		}

		/**
		 * @param m_text the m_text to set
		 */
		public void setM_text(String m_text)
		{
			this.m_text = m_text;
			
		}

	}
	
	class LabelView extends View
	{

		private Label m_controller;
		
		private Text m_textSprite;
		
		private  Font m_fontText;
		
		private  Color m_color;
		
		
		public LabelView() throws IOException
		{
			m_controller = Label.this;
			
			// instance du Text
			m_fontText = new Font();
			m_fontText.loadFromStream(LabelModel.class.getResourceAsStream("/FONTS/Chantelli_Antiqua.ttf"));
			m_textSprite = new Text();
			m_textSprite.setCharacterSize(14);
			m_textSprite.setFont(m_fontText);
			m_textSprite.setColor(Color.WHITE);
			
			
		}
		
		public void updateTextSprite(String text)
		{
			m_textSprite.setString(text);
		}
		
		public void setColor(Color color)
		{
			// set the color
			m_color = color;
			m_textSprite.setColor(color);
		
		}
		
		@Override
		public void draw(RenderTarget render, RenderStates state) 
		{
			// on place la position
			m_textSprite.setPosition(m_controller.m_model.m_position);
			// on affiche le texte
			render.draw(m_textSprite);
			
		}
	
	
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
