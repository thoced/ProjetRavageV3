package coreGuiRavage;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.KeyEvent;

public class ProgressBar extends Widget 
{
	// active bar
	private boolean m_isActive = false;
	// callback
	private IProgressBarListener m_listener;
	
	public ProgressBar(Vector2f position,Vector2f size,float timeEndProgress,IProgressBarListener listener)
	{
		super();
		this.m_model = new ProgressBarModel(position,size,timeEndProgress);
		this.m_view = new ProgressBarView();
		m_listener = listener;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	

	public void startProgressBar()
	{
		m_isActive = true;
		((ProgressBarModel)this.m_model).m_sizeValue = new Vector2f(0f,((ProgressBarModel)this.m_model).m_sizeValue.y);
		((ProgressBarModel)this.m_model).m_timeValue = 0f; // remise à zero du timer
	}

	public boolean isInAction()
	{
		return m_isActive;
	}

	@Override
	public void update(Time deltaTime) 
	{
		// mise à jour du m_timeValue
		if(m_isActive)
		{
			((ProgressBarModel)this.m_model).m_timeValue += deltaTime.asSeconds();
			
			if(((ProgressBarModel)this.m_model).m_timeValue >= ((ProgressBarModel)this.m_model).m_timeEndProgress)
			{
				// on désactive le progress bar
					m_isActive = false;
					((ProgressBarModel)this.m_model).m_sizeValue = new Vector2f(0f,((ProgressBarModel)this.m_model).m_sizeValue.y);
					((ProgressBarModel)this.m_model).m_timeValue = 0; // remise à zero du timer
				// appel Call Back
				if(m_listener != null)
					m_listener.onActionProgressBar(this);
				
				
				
				
			}
			else
			{
				 // création de la couleur de progression
				 int alpha = (int) ((255 / ((ProgressBarModel)this.m_model).m_timeEndProgress) * ((ProgressBarModel)this.m_model).m_timeValue);
				 ((ProgressBarView)this.m_view).m_shape.setFillColor(new Color(128,128,128,alpha));
				 // création de la valeur x de progression
				 float x = (((ProgressBarModel)this.m_model).m_xValue / ((ProgressBarModel)this.m_model).m_timeEndProgress) * ((ProgressBarModel)this.m_model).m_timeValue;
				 ((ProgressBarModel)this.m_model).m_sizeValue = new Vector2f(x, ((ProgressBarModel)this.m_model).m_sizeValue.y);
			}
		}
		
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
	
	
	
	public class ProgressBarModel extends Model
	{
		private float m_timeEndProgress;
		
		private float m_timeValue;
		
		private Vector2f m_sizeValue;
		
		private float m_xValue;
	
		
		public void setValue(int value)
		{
		
			
		}
		
		
		public ProgressBarModel(Vector2f position, Vector2f size,float timeEndProgress) 
		{
			super(position, size);
			this.setM_timeEndProgress(timeEndProgress);
			this.setM_timeValue(0f); // initialisation du time value à 0
			this.m_sizeValue = new Vector2f(0f,size.y);
			this.m_xValue = size.x;
		
			// TODO Auto-generated constructor stub
		}
		

		public float getM_timeEndProgress() {
			return m_timeEndProgress;
		}


		public void setM_timeEndProgress(float m_timeEndProgress) {
			this.m_timeEndProgress = m_timeEndProgress;
		}


		public float getM_timeValue() {
			return m_timeValue;
		}


		public void setM_timeValue(float m_timeValue) {
			this.m_timeValue = m_timeValue;
		}


		
		
	}
	
	public class ProgressBarView extends View
	{
		public ProgressBar m_controller;
		
		public RectangleShape m_shape;
		
	
		
		public ProgressBarView()
		{
			m_controller = ProgressBar.this;
			m_shape = new RectangleShape();
			m_shape.setPosition(m_controller.m_model.m_position);
			m_shape.setFillColor(new Color(128,128,128,256));
			m_shape.setSize(((ProgressBarModel)m_controller.m_model).m_sizeValue);
			
		}
		
		@Override
		public void draw(RenderTarget render, RenderStates state) 
		{
			// mise à jour du size
			m_shape.setSize(((ProgressBarModel)m_controller.m_model).m_sizeValue);
			
			render.draw(m_shape);
			
		}
		
	}
	
	public interface IProgressBarListener
	{
		public void onActionProgressBar(ProgressBar owner);
	}

	

}


